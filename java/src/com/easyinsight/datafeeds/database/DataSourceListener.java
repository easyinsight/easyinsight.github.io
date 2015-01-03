package com.easyinsight.datafeeds.database;

import com.easyinsight.analysis.AsyncReport;
import com.easyinsight.cache.MemCachedManager;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;
import com.easyinsight.email.UserStub;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import com.easyinsight.userupload.DataSourceThreadPool;
import com.easyinsight.userupload.UserUploadService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 12/3/12
 * Time: 10:03 PM
 */
public class DataSourceListener {

    public static void dataSource(long sourceID, String callID, long frequestID) {
        DataSourceThreadPool.instance().addActivity(() -> {
            boolean changed;
            Exception exception = null;
            EIConnection conn = Database.instance().getConnection();
            try {
                conn.setAutoCommit(false);
                FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(sourceID, conn);
                UserStub dataSourceUser = null;
                List<FeedConsumer> owners = dataSource.getUploadPolicy().getOwners();
                for (FeedConsumer owner : owners) {
                    if (owner.type() == FeedConsumer.USER) {
                        dataSourceUser = (UserStub) owner;
                    }
                }
                PreparedStatement queryStmt = conn.prepareStatement("SELECT USERNAME, USER_ID, USER.ACCOUNT_ID, ACCOUNT.ACCOUNT_TYPE, USER.account_admin, USER.guest_user," +
                        "ACCOUNT.FIRST_DAY_OF_WEEK, USER.ANALYST FROM USER, ACCOUNT " +
                        "WHERE USER.ACCOUNT_ID = ACCOUNT.ACCOUNT_ID AND (ACCOUNT.account_state = ? OR ACCOUNT.ACCOUNT_STATE = ?) AND USER.USER_ID = ?");
                queryStmt.setInt(1, Account.ACTIVE);
                queryStmt.setInt(2, Account.TRIAL);
                queryStmt.setLong(3, dataSourceUser.getUserID());
                ResultSet rs = queryStmt.executeQuery();
                rs.next();
                String userName = rs.getString(1);
                long userID = rs.getLong(2);
                long accountID = rs.getLong(3);
                int accountType = rs.getInt(4);
                boolean accountAdmin = rs.getBoolean(5);

                int firstDayOfWeek = rs.getInt(7);

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
                    UserUploadService.UploadDataSource source = new UserUploadService.UploadDataSource(conn, new ArrayList<>(), new Date(),
                            dataSource, (IServerDataSourceDefinition) dataSource, callID, null);
                    changed = source.invoke().isChanged();
                    conn.commit();
                } finally {
                    SecurityUtil.clearThreadLocal();
                }

                MemCachedManager.instance().add("async" + frequestID, 100, changed);
                PreparedStatement u = conn.prepareStatement("UPDATE async_report_request SET request_state = ? WHERE async_report_request_id = ?");
                u.setInt(1, AsyncReport.FINISHED);
                u.setLong(2, frequestID);
                u.executeUpdate();
                u.close();

            } catch (Exception e) {
                exception = e;
                //LogClass.error(e);
                if (!conn.getAutoCommit()) {
                    conn.rollback();
                }
                MemCachedManager.instance().add("async" + frequestID, 100, e);
                //throw new RuntimeException(e);
            } finally {
                conn.setAutoCommit(true);
                Database.closeConnection(conn);
            }

            if (exception != null) {
                conn = Database.instance().getConnection();
                try {
                    PreparedStatement u = conn.prepareStatement("UPDATE async_report_request SET request_state = ? WHERE async_report_request_id = ?");
                    u.setInt(1, AsyncReport.FINISHED);
                    u.setLong(2, frequestID);
                    u.executeUpdate();
                    u.close();
                } catch (Exception e1) {
                    LogClass.error(e1);
                } finally {
                    Database.closeConnection(conn);
                }
            }
        });
    }
}
