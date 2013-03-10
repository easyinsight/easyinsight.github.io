package com.easyinsight.scheduler;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.analysis.ReportFault;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.database.ServerDatabaseConnection;
import com.easyinsight.email.UserStub;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import com.xerox.amazonws.sqs2.Message;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * User: James Boe
 * Date: Apr 15, 2009
 * Time: 5:35:53 PM
 */
@Entity
@Table(name="data_source_query")
@PrimaryKeyJoinColumn(name="scheduled_task_id")
public class DataSourceScheduledTask extends ScheduledTask {

    @Column(name="data_source_id")
    private long dataSourceID;
    private static FeedStorage feedStorage = new FeedStorage();

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    protected void execute(Date now, EIConnection conn) throws Exception {
        try {
            if (!validate(conn)) return;
            FeedDefinition base = feedStorage.getFeedDefinitionData(dataSourceID, conn);
            if (!(base instanceof IServerDataSourceDefinition)) {
                return;
            }
            IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition) base;
            UserStub dataSourceUser = null;
            List<FeedConsumer> owners = dataSource.getUploadPolicy().getOwners();
            for (FeedConsumer owner : owners){
                if (owner.type() == FeedConsumer.USER) {
                    dataSourceUser = (UserStub) owner;
                }
            }
            if (dataSourceUser == null) {
                LogClass.info("No user for data data source refresh.");
            } else {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT USERNAME, USER_ID, USER.ACCOUNT_ID, ACCOUNT.ACCOUNT_TYPE, USER.account_admin, USER.guest_user," +
                        "ACCOUNT.FIRST_DAY_OF_WEEK, USER.ANALYST FROM USER, ACCOUNT " +
                        "WHERE USER.ACCOUNT_ID = ACCOUNT.ACCOUNT_ID AND (ACCOUNT.account_state = ? OR ACCOUNT.ACCOUNT_STATE = ?) AND USER.USER_ID = ?");
                queryStmt.setInt(1, Account.ACTIVE);
                queryStmt.setInt(2, Account.TRIAL);
                queryStmt.setLong(3, dataSourceUser.getUserID());
                ResultSet rs = queryStmt.executeQuery();
                if (rs.next()) {
                    String userName = rs.getString(1);
                    long userID = rs.getLong(2);
                    long accountID = rs.getLong(3);
                    int accountType = rs.getInt(4);
                    boolean accountAdmin = rs.getBoolean(5);
                    boolean guestUser = rs.getBoolean(6);
                    int firstDayOfWeek = rs.getInt(7);
                    boolean analyst = rs.getBoolean(8);
                    PreparedStatement stmt = conn.prepareStatement("SELECT PERSONA.persona_name FROM USER, PERSONA WHERE USER.PERSONA_ID = PERSONA.PERSONA_ID AND USER.USER_ID = ?");
                    stmt.setLong(1, userID);
                    ResultSet personaRS = stmt.executeQuery();

                    String personaName = null;
                    if (personaRS.next()) {
                        personaName = personaRS.getString(1);
                    }
                    stmt.close();
                    SecurityUtil.populateThreadLocal(userName, userID, accountID, accountType, accountAdmin, firstDayOfWeek, personaName);
                    try {

                        if (DataSourceMutex.mutex().lock(dataSource.getDataFeedID())) {
                            try {
                                new DataSourceFactory().createSource(conn, new ArrayList<ReportFault>(), now, base, dataSource, null).invoke();
                                conn.commit();
                            } finally {
                                DataSourceMutex.mutex().unlock(dataSource.getDataFeedID());
                            }
                        }

                    } finally {
                        SecurityUtil.clearThreadLocal();
                    }
                }
            }
        } catch (ReportException re) {
            configurationProblem(conn, re.getReportFault().toString());
        } catch (Exception e) {
            LogClass.error("Data source " + dataSourceID + " had error " + e.getMessage() + " in trying to refresh data.");
            throw e;
        }
    }

    protected boolean validate(EIConnection conn) throws Exception {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT COUNT(DATA_SOURCE_PROBLEM_ID) FROM DATA_SOURCE_PROBLEM WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, dataSourceID);
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        return (count <= 1);
    }

    protected void configurationProblem(EIConnection conn, String message) throws Exception {
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DATA_SOURCE_PROBLEM (DATA_SOURCE_ID, PROBLEM_TEXT) VALUES (?, ?)");
        insertStmt.setLong(1, getDataSourceID());
        if (message != null && message.length() > 250) {
            message = message.substring(0, 250);
        }
        insertStmt.setString(2, message);
        insertStmt.execute();
    }

    public static class DataSourceFactory {
        public IUploadDataSource createSource(EIConnection conn, List<ReportFault> warnings, Date now, FeedDefinition sourceToRefresh, IServerDataSourceDefinition refreshable, String callID) {
            if (sourceToRefresh.getFeedType().getType() == FeedType.SERVER_MYSQL.getType() ||
                    sourceToRefresh.getFeedType().getType() == FeedType.SERVER_SQL_SERVER.getType() ||
                    sourceToRefresh.getFeedType().getType() == FeedType.ORACLE.getType() ||
                    sourceToRefresh.getFeedType().getType() == FeedType.SERVER_POSTGRES.getType()) {
                return new SQSUploadDataSource(sourceToRefresh.getDataFeedID(), (ServerDatabaseConnection) sourceToRefresh);
            } else {
                return new UploadDataSource(conn, warnings, now, sourceToRefresh, refreshable, callID);
            }
        }
    }

    public static interface IUploadDataSource {
        public boolean invoke() throws Exception;
    }

    public static class SQSUploadDataSource implements IUploadDataSource {

        private long dataSourceID;
        private ServerDatabaseConnection dataSource;

        private SQSUploadDataSource(long dataSourceID, ServerDatabaseConnection dataSource) {
            this.dataSourceID = dataSourceID;
            this.dataSource = dataSource;
        }

        public boolean invoke() throws Exception {
            MessageQueue msgQueue = SQSUtils.connectToQueue(ConfigLoader.instance().getDatabaseRequestQueue(), "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            MessageQueue responseQueue = SQSUtils.connectToQueue(ConfigLoader.instance().getDatabaseResponseQueue(), "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            msgQueue.sendMessage(dataSourceID + "|" + System.currentTimeMillis());
            boolean responded = false;
            boolean changed = false;
            int i = 0;
            while (!responded && i < (dataSource.getTimeout() * 60)) {
                Message message = responseQueue.receiveMessage();
                if (message == null) {
                    i++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        // ignore
                    }
                } else {
                    String body = message.getMessageBody();
                    String[] parts = body.split("\\|");
                    long sourceID = Long.parseLong(parts[0]);
                    System.out.println("got response with id = " + sourceID);
                    if (sourceID == dataSourceID) {
                        responseQueue.deleteMessage(message);
                        boolean successful = Boolean.parseBoolean(parts[1]);
                        if (successful) {
                            changed = Boolean.parseBoolean(parts[2]);
                            System.out.println("matched!");
                            responded = true;
                        } else {
                            String error = parts[2];
                            throw new ReportException(new DataSourceConnectivityReportFault(error, dataSource));
                        }
                    }
                }
            }
            return changed;
        }
    }

    public static class UploadDataSource implements IUploadDataSource {
        private EIConnection conn;
        private List<ReportFault> warnings;
        private Date now;
        private FeedDefinition sourceToRefresh;
        private IServerDataSourceDefinition refreshable;
        private String callID;

        public UploadDataSource(EIConnection conn, List<ReportFault> warnings, Date now, FeedDefinition sourceToRefresh, IServerDataSourceDefinition refreshable, String callID) {
            this.conn = conn;
            this.warnings = warnings;
            this.now = now;
            this.sourceToRefresh = sourceToRefresh;
            this.refreshable = refreshable;
            this.callID = callID;
        }

        public boolean invoke() throws Exception {
            boolean changed = refreshable.refreshData(SecurityUtil.getAccountID(), new Date(), conn, null, callID, sourceToRefresh.getLastRefreshStart(), false, warnings);
            sourceToRefresh.setVisible(true);
            sourceToRefresh.setLastRefreshStart(now);
            if (changed) {
                new DataSourceInternalService().updateFeedDefinition(sourceToRefresh, conn, true, true);
            } else {
                feedStorage.updateDataFeedConfiguration(sourceToRefresh, conn);
            }
            return changed;
        }
    }
}
