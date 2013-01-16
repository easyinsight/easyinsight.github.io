package com.easyinsight.datafeeds.database;

import com.easyinsight.analysis.ReportFault;
import com.easyinsight.config.ConfigLoader;
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
import com.easyinsight.userupload.UserUploadService;
import com.xerox.amazonws.sqs2.Message;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSUtils;

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
public class DatabaseListener implements Runnable {

    /*public static final String DB_REQUEST = "EIDBRequestDev";
    public static final String DB_RESPONSE = "EIDBResponseDev";*/

    private boolean running;

    private static DatabaseListener instance;

    public static DatabaseListener instance() {
        return instance;
    }

    public static void initialize() {
        instance = new DatabaseListener();

        new Thread(instance).start();
    }

    public void stop() {
        running = false;
    }

    public void blah() throws Exception {
        running = true;
        MessageQueue messageQueue = SQSUtils.connectToQueue(ConfigLoader.instance().getDatabaseRequestQueue(), "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
        while (running) {
            try {
                Message message = messageQueue.receiveMessage();
                if (message == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        // ignore
                    }
                } else {
                    String body = message.getMessageBody();
                    messageQueue.deleteMessage(message);
                    String[] tokens = body.split("\\|");
                    final long sourceID = Long.parseLong(tokens[0]);
                    long time = Long.parseLong(tokens[1]);

                    if (time < (System.currentTimeMillis() - (1000 * 60 * 60))) {
                        System.out.println("Dropping old message from " + new Date(time));
                        continue;
                    }
                    System.out.println("firing up a refresh on " + sourceID);
                    new Thread(new Runnable() {

                        public void run() {
                            boolean changed;
                            EIConnection conn = Database.instance().getConnection();
                            try {
                                conn.setAutoCommit(false);
                                FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(sourceID, conn);
                                UserStub dataSourceUser = null;
                                List<FeedConsumer> owners = dataSource.getUploadPolicy().getOwners();
                                for (FeedConsumer owner : owners){
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
                                    UserUploadService.UploadDataSource source = new UserUploadService.UploadDataSource(conn, new ArrayList<ReportFault>(), new Date(),
                                            dataSource, (IServerDataSourceDefinition) dataSource, null);
                                    changed = source.invoke();
                                    conn.commit();
                                } finally {
                                    SecurityUtil.clearThreadLocal();
                                }
                            } catch (Exception e) {
                                LogClass.error(e);
                                conn.rollback();
                                try {
                                    MessageQueue responseQueue = SQSUtils.connectToQueue(ConfigLoader.instance().getDatabaseResponseQueue(), "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
                                    if (e.getCause() == null) {
                                        responseQueue.sendMessage(String.valueOf(sourceID) + "|false|" + e.getMessage());
                                    } else {
                                        responseQueue.sendMessage(String.valueOf(sourceID) + "|false|" + e.getCause().getMessage());
                                    }
                                } catch (Exception e1) {
                                    LogClass.error(e1);
                                }
                                throw new RuntimeException(e);
                            } finally {
                                conn.setAutoCommit(true);
                                Database.closeConnection(conn);
                            }
                            try {
                                MessageQueue responseQueue = SQSUtils.connectToQueue(ConfigLoader.instance().getDatabaseResponseQueue(), "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
                                responseQueue.sendMessage(String.valueOf(sourceID) + "|true|" + changed);
                            } catch (Exception e) {
                                LogClass.error(e);
                            }
                        }
                    }).start();
                }
            } catch (Exception e) {
                LogClass.error(e);
            }
        }
    }

    public void run() {
        try {
            blah();
        } catch (Exception e) {
            LogClass.error(e);
        }
    }
}
