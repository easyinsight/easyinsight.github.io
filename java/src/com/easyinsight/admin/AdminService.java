package com.easyinsight.admin;

import com.easyinsight.analysis.*;
import com.easyinsight.audit.*;
import com.easyinsight.cache.MemCachedManager;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.email.SendGridEmail;
import com.easyinsight.logging.LogClass;
import com.easyinsight.outboundnotifications.BroadcastInfo;
import com.easyinsight.eventing.MessageUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.management.ThreadInfo;
import java.sql.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.Date;

import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DatabaseManager;
import com.easyinsight.tag.Tag;
import com.easyinsight.users.Account;
import com.easyinsight.users.AccountCreditCardBillingInfo;
import com.easyinsight.users.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * User: James Boe
 * Date: Aug 16, 2008
 * Time: 10:57:40 AM
 */
public class AdminService {

    private static final String LOC_XML = "<url>\r\n\t<loc>{0}</loc>\r\n</url>\r\n";

    public void outputStackOnDatabase() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Database.outputStackElements();
    }

    public void emails(long userID) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement userStmt = conn.prepareStatement("SELECT FIRST_NAME, EMAIL FROM USER WHERE USER_ID = ?");
            userStmt.setLong(1, userID);
            ResultSet rs = userStmt.executeQuery();
            rs.next();
            String firstName = rs.getString(1);
            String email = rs.getString(2);
            new LeadNurtureShell().generate(conn, userID, email, LeadNurtureShell.FIRST_EMAIL, firstName);
            new LeadNurtureShell().generate(conn, userID, email, LeadNurtureShell.SECOND_EMAIL, firstName);
            new LeadNurtureShell().generate(conn, userID, email, LeadNurtureShell.THIRD_EMAIL, firstName);
            new LeadNurtureShell().generate(conn, userID, email, LeadNurtureShell.FOURTH_EMAIL, firstName);
            new LeadNurtureShell().generate(conn, userID, email, LeadNurtureShell.FIFTH_EMAIL, firstName);
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    /*public void testUserPasswords() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT EMAIL, USERNAME, USER_ID FROM USER WHERE ACCOUNT_ID = ?");
            PreparedStatement userStmt = conn.prepareStatement("SELECT USER_ID, PASSWORD, EMAIL FROM USER WHERE EMAIL = ?");
            //PreparedStatement updateStmt = conn.prepareStatement("UPDATE USER SET PASSWORD = ? WHERE USER_ID = ?");
            stmt.setLong(1, 6378);

            // for each user in account 6378, find the email, username, ID

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String targetEmail = rs.getString(1);
                String targetUserName = rs.getString(2);
                long targetUserID = rs.getLong(3);
                if (targetEmail.endsWith("copy")) {
                    String searchEmail = targetEmail.substring(0, targetEmail.length() - "copy".length());
                    targetUserName = targetUserName.substring(0, targetUserName.length() - "copy".length());

                    userStmt.setString(1, searchEmail);
                    ResultSet searchRS = userStmt.executeQuery();
                    if (searchRS.next()) {
                        long sourceUserID = searchRS.getLong(1);
                        String password = searchRS.getString(2);
                        String sourceEmail = searchRS.getString(3);
                        System.out.println("Will copy the password of " + sourceEmail + " with user ID " + sourceUserID + " to " +
                                targetEmail + " with user ID " + targetUserID + ", rename source user, update username and email of target user.");
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }*/

    public void moveSource(long sourceID) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE FEED_PERSISTENCE_METADATA SET DATABASE_NAME = ? WHERE FEED_ID = ?");
            PreparedStatement dsStmt = conn.prepareStatement("UPDATE DATA_FEED SET LAST_REFRESH_START = NULL WHERE DATA_FEED_ID = ?");
            dsStmt.setLong(1, sourceID);
            dsStmt.executeUpdate();
            dsStmt.close();
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(sourceID, conn);
            if (dataSource instanceof CompositeFeedDefinition) {
                CompositeFeedDefinition comp = (CompositeFeedDefinition) dataSource;
                for (CompositeFeedNode node : comp.getCompositeFeedNodes()) {
                    long id = node.getDataFeedID();
                    updateStmt.setString(1, "storage5");
                    updateStmt.setLong(2, id);
                    updateStmt.executeUpdate();
                }
            } else {

                updateStmt.setString(1, "storage5");
                updateStmt.setLong(2, sourceID);
                updateStmt.executeUpdate();

            }
            updateStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testUserPasswords() {

        Database storageDatabase = DatabaseManager.instance().getDatabase("storage5");
        EIConnection storageConn = storageDatabase.getConnection();
        try {
            PreparedStatement errorStmt = storageConn.prepareStatement("SELECT * FROM STL_LOAD_ERRORS");
            ResultSet rs = errorStmt.executeQuery();
            while (rs.next()) {
                int columnCount = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    switch (rs.getMetaData().getColumnType(i)) {
                        case Types.BIGINT:
                        case Types.TINYINT:
                        case Types.SMALLINT:
                        case Types.INTEGER:
                        case Types.NUMERIC:
                        case Types.FLOAT:
                        case Types.DOUBLE:
                        case Types.DECIMAL:
                        case Types.REAL:
                            System.out.println(columnName + " = " + rs.getDouble(i));
                            break;

                        case Types.BOOLEAN:
                        case Types.BIT:
                        case Types.CHAR:
                        case Types.NCHAR:
                        case Types.NVARCHAR:
                        case Types.VARCHAR:
                        case Types.LONGVARCHAR:
                            System.out.println(columnName + " = " + rs.getString(i));
                            break;
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(storageConn);
        }
    }

    public void copyUserPasswords() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement("SELECT EMAIL, USERNAME, USER_ID FROM USER WHERE ACCOUNT_ID = ?");
            PreparedStatement userStmt = conn.prepareStatement("SELECT USER_ID, USERNAME FROM USER WHERE EMAIL = ? AND ACCOUNT_ID = ?");
            //PreparedStatement updateNameStmt = conn.prepareStatement("UPDATE USER SET EMAIL = ?, USERNAME = ? WHERE USER_ID = ?");
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE USER SET USERNAME = ? WHERE USER_ID = ?");
            stmt.setLong(1, 1917);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String targetEmail = rs.getString(1);
                String targetUserName = rs.getString(2);
                long targetUserID = rs.getLong(3);
                if (targetEmail.endsWith("copied")) {
                    String searchEmail = targetEmail.substring(0, targetEmail.length() - "copied".length());
                    targetUserName = targetUserName.substring(0, targetUserName.length() - "copied".length());

                    userStmt.setString(1, searchEmail);
                    userStmt.setLong(2, 6378);
                    //System.out.println("looking for email " + searchEmail);
                    ResultSet searchRS = userStmt.executeQuery();
                    if (searchRS.next()) {
                        long sourceUserID = searchRS.getLong(1);
                        //String password = searchRS.getString(2);
                        //String sourceEmail = searchRS.getString(3);
                        String userName = searchRS.getString(2);
                        if (userName.equals(targetUserName)) {
                            continue;
                        }
                        System.out.println("Will copy the username of " + targetUserName + " from user ID " + targetUserID + " to " +
                                sourceUserID + " with email  " + searchEmail + " who has username as is of " + userName);

                        updateStmt.setString(1, targetUserName);
                        updateStmt.setLong(2, sourceUserID);
                        int rows2 = updateStmt.executeUpdate();
                        System.out.println("rows2 = " + rows2);
                    }
                }
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public List<EIDescriptor> getConnectionReports(int dataSourceType) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            return getConnectionReports(dataSourceType, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public ReportResults getAvailableReports(int dataSourceType) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            List<InsightDescriptor> reports = new ArrayList<InsightDescriptor>();
            List<DashboardDescriptor> dashboards = new ArrayList<DashboardDescriptor>();

            PreparedStatement accountStmt = conn.prepareStatement("SELECT ACCOUNT_ID, NAME FROM ACCOUNT WHERE EXCHANGE_AUTHOR = ?");

            PreparedStatement reportStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE FROM ANALYSIS, USER_TO_ANALYSIS, USER, DATA_FEED WHERE " +
                    "ANALYSIS.ANALYSIS_ID = USER_TO_ANALYSIS.ANALYSIS_ID AND USER_TO_ANALYSIS.USER_ID = USER.USER_ID AND USER.ACCOUNT_ID = ? AND " +
                    "ANALYSIS.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND DATA_FEED.FEED_TYPE = ?");
            PreparedStatement dashboardStmt = conn.prepareStatement("SELECT DASHBOARD.DASHBOARD_ID, DASHBOARD_NAME FROM DASHBOARD, USER_TO_DASHBOARD, USER, DATA_FEED WHERE " +
                    "DASHBOARD.DASHBOARD_ID = USER_TO_DASHBOARD.DASHBOARD_ID AND USER_TO_DASHBOARD.USER_ID = USER.USER_ID AND USER.ACCOUNT_ID = ? AND " +
                    "DASHBOARD.DATA_SOURCE_ID = DATA_FEED.DATA_FEED_ID AND DATA_FEED.FEED_TYPE = ?");

            PreparedStatement findTagStmt = conn.prepareStatement("SELECT ACCOUNT_TAG_ID, TAG_NAME FROM ACCOUNT_TAG WHERE ACCOUNT_ID = ? AND REPORT_TAG = ?");
            PreparedStatement findTagForReportStmt = conn.prepareStatement("SELECT REPORT_ID, ACCOUNT_TAG_ID, TAG_NAME " +
                    "FROM REPORT_TO_TAG, ACCOUNT_TAG WHERE TAG_ID = ACCOUNT_TAG.ACCOUNT_TAG_ID AND " +
                    "ACCOUNT_TAG.ACCOUNT_ID = ?");
            PreparedStatement findTagForDashboardStmt = conn.prepareStatement("SELECT DASHBOARD_ID, ACCOUNT_TAG_ID, TAG_NAME FROM " +
                    "DASHBOARD_TO_TAG, ACCOUNT_TAG WHERE TAG_ID = ACCOUNT_TAG.ACCOUNT_TAG_ID AND ACCOUNT_TAG.ACCOUNT_ID = ?");

            accountStmt.setBoolean(1, true);
            ResultSet rs = accountStmt.executeQuery();

            int i = 0;
            Map<String, Tag> fakeTagMap = new HashMap<String, Tag>();

            while (rs.next()) {
                long accountID = rs.getLong(1);
                String accountName = rs.getString(2);



                Map<Long, List<Tag>> reportTagMap = new HashMap<Long, List<Tag>>();
                Map<Long, List<Tag>> dashboardTagMap = new HashMap<Long, List<Tag>>();
                findTagStmt.setLong(1, accountID);
                findTagStmt.setBoolean(2, true);
                findTagStmt.executeQuery();
                ResultSet tagRS = findTagStmt.executeQuery();
                while (tagRS.next()) {
                    long tagID = tagRS.getLong(1);
                    String tagName = tagRS.getString(2);
                    Tag tag = fakeTagMap.get(tagName);
                    if (tag == null) {
                        tag = new Tag(i++, tagName, false, false, false);
                        fakeTagMap.put(tagName, tag);
                    }
                }

                findTagForReportStmt.setLong(1, accountID);
                ResultSet reportTagRS = findTagForReportStmt.executeQuery();
                while (reportTagRS.next()) {
                    long reportID = reportTagRS.getLong(1);
                    String tagName = reportTagRS.getString(3);
                    Tag tag = fakeTagMap.get(tagName);
                    List<Tag> tags = reportTagMap.get(reportID);
                    if (tags == null) {
                        tags = new ArrayList<Tag>();
                        reportTagMap.put(reportID, tags);
                    }
                    tags.add(tag);
                }

                findTagForDashboardStmt.setLong(1, accountID);
                ResultSet dashboardTagRS = findTagForDashboardStmt.executeQuery();
                while (dashboardTagRS.next()) {
                    long reportID = dashboardTagRS.getLong(1);
                    String tagName = dashboardTagRS.getString(3);
                    Tag tag = fakeTagMap.get(tagName);
                    List<Tag> tags = dashboardTagMap.get(reportID);
                    if (tags == null) {
                        tags = new ArrayList<Tag>();
                        dashboardTagMap.put(reportID, tags);
                    }
                    tags.add(tag);
                }

                reportStmt.setLong(1, accountID);
                reportStmt.setInt(2, dataSourceType);
                ResultSet reportRS = reportStmt.executeQuery();
                while (reportRS.next()) {
                    long reportID = reportRS.getLong(1);
                    String reportName = reportRS.getString(2);
                    InsightDescriptor insightDescriptor = new InsightDescriptor();
                    insightDescriptor.setId(reportID);
                    insightDescriptor.setName(reportName);
                    List<Tag> tags = reportTagMap.get(reportID);
                    if (tags != null) {
                        insightDescriptor.setTags(tags);
                    }
                    reports.add(insightDescriptor);
                }

                dashboardStmt.setLong(1, accountID);
                dashboardStmt.setInt(2, dataSourceType);
                ResultSet dashboardRS = dashboardStmt.executeQuery();
                while (dashboardRS.next()) {
                    long reportID = dashboardRS.getLong(1);
                    String reportName = dashboardRS.getString(2);
                    DashboardDescriptor dashboardDescriptor = new DashboardDescriptor();
                    dashboardDescriptor.setId(reportID);
                    dashboardDescriptor.setName(reportName);
                    List<Tag> tags = dashboardTagMap.get(reportID);
                    if (tags != null) {
                        dashboardDescriptor.setTags(tags);
                    }
                    dashboards.add(dashboardDescriptor);
                }
            }
            ReportResults reportResults = new ReportResults();
            reportResults.setReports(reports);
            reportResults.setDashboards(dashboards);
            reportResults.setReportTags(new ArrayList<Tag>(fakeTagMap.values()));
            return reportResults;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private List<EIDescriptor> getConnectionReports(int dataSourceType, EIConnection conn) throws SQLException {
        List<EIDescriptor> descriptors = new ArrayList<EIDescriptor>();
        PreparedStatement ps = conn.prepareStatement("SELECT ANALYSIS_ID, TITLE FROM ANALYSIS, DATA_FEED WHERE ANALYSIS.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID " +
                "AND FEED_TYPE = ? AND RECOMMENDED_EXCHANGE = ?");
        ps.setInt(1, dataSourceType);
        ps.setBoolean(2, true);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            long reportID = rs.getLong(1);
            String title = rs.getString(2);
            InsightDescriptor insightDescriptor = new InsightDescriptor();
            insightDescriptor.setId(reportID);
            insightDescriptor.setName(title);
            descriptors.add(insightDescriptor);
        }
        ps.close();
        PreparedStatement dashboardPS = conn.prepareStatement("SELECT DASHBOARD_ID, DASHBOARD_NAME FROM DASHBOARD, DATA_FEED WHERE " +
                "DASHBOARD.DATA_SOURCE_ID = DATA_FEED.DATA_FEED_ID AND DATA_FEED.FEED_TYPE = ? AND RECOMMENDED_EXCHANGE = ?");
        dashboardPS.setInt(1, dataSourceType);
        dashboardPS.setBoolean(2, true);
        ResultSet dashboardRS = dashboardPS.executeQuery();
        while (dashboardRS.next()) {
            long reportID = dashboardRS.getLong(1);
            String title = dashboardRS.getString(2);
            DashboardDescriptor dashboardDescriptor = new DashboardDescriptor();
            dashboardDescriptor.setId(reportID);
            dashboardDescriptor.setName(title);
            descriptors.add(dashboardDescriptor);
        }
        dashboardPS.close();
        return descriptors;
    }

    public void updateConnectionReports(int dataSourceType, List<EIDescriptor> descriptors) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            List<EIDescriptor> existingConnectionReports = getConnectionReports(dataSourceType, conn);
            Set<EIDescriptor> set = new HashSet<EIDescriptor>(existingConnectionReports);
            PreparedStatement ps = conn.prepareStatement("UPDATE ANALYSIS SET RECOMMENDED_EXCHANGE = ? WHERE ANALYSIS_ID = ?");
            PreparedStatement dashboardUpdate = conn.prepareStatement("UPDATE DASHBOARD SET RECOMMENDED_EXCHANGE = ? WHERE DASHBOARD_ID = ?");
            for (EIDescriptor desc : descriptors) {
                if (existingConnectionReports.remove(desc)) {

                } else {
                    if (desc.getType() == EIDescriptor.REPORT) {
                        ps.setBoolean(1, true);
                        ps.setLong(2, desc.getId());
                        ps.executeUpdate();
                    } else if (desc.getType() == EIDescriptor.DASHBOARD) {
                        dashboardUpdate.setBoolean(1, true);
                        dashboardUpdate.setLong(2, desc.getId());
                        dashboardUpdate.executeUpdate();
                    }
                }
            }
            for (EIDescriptor remaining : set) {
                if (remaining.getType() == EIDescriptor.REPORT) {
                    ps.setBoolean(1, false);
                    ps.setLong(2, remaining.getId());
                    ps.executeUpdate();
                } else if (remaining.getType() == EIDescriptor.DASHBOARD) {
                    dashboardUpdate.setBoolean(1, false);
                    dashboardUpdate.setLong(2, remaining.getId());
                    dashboardUpdate.executeUpdate();
                }
            }
            ps.close();
            dashboardUpdate.close();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void forceSemaphoreRelease() {

        UserThreadMutex.release();
    }

    public void disableGenerators() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            /*
            update task_generator set task_generator.disabled_generator = ?
            where task_generator.task_generator_id in
            (select data_activity_task_generator.task_generator_id from data_activity_task_generator, SCHEDULED_DATA_SOURCE_REFRESH, upload_policy_users, user, account
            where data_activity_task_generator.scheduled_activity_id = SCHEDULED_DATA_SOURCE_REFRESH.scheduled_account_activity_id and
            SCHEDULED_DATA_SOURCE_REFRESH.data_source_id = upload_policy_users.feed_id and
            upload_policy_users.user_id = user.user_id and user.account_id = account.account_id and (account.account_state = ?))
             */
            PreparedStatement disableStmt = conn.prepareStatement("update task_generator set task_generator.disabled_generator = ? where task_generator.task_generator_id in (select data_activity_task_generator.task_generator_id from data_activity_task_generator, SCHEDULED_DATA_SOURCE_REFRESH, upload_policy_users, user, account where data_activity_task_generator.scheduled_activity_id = SCHEDULED_DATA_SOURCE_REFRESH.scheduled_account_activity_id and SCHEDULED_DATA_SOURCE_REFRESH.data_source_id = upload_policy_users.feed_id and upload_policy_users.user_id = user.user_id and user.account_id = account.account_id and (account.account_state = ?))");
            disableStmt.setBoolean(1, true);
            disableStmt.setLong(2, Account.DELINQUENT);
            disableStmt.executeUpdate();

            disableStmt.setBoolean(1, false);
            disableStmt.setLong(2, Account.ACTIVE);
            disableStmt.executeUpdate();

            disableStmt.setBoolean(1, false);
            disableStmt.setLong(2, Account.TRIAL);
            disableStmt.executeUpdate();
            disableStmt.close();

        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void flushCache() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        MemCachedManager.flush();
    }

    public void applyLocks() {

    }

    public void resetLocks() {

    }

    public void lockStatus() {

    }

    public void fixTrialAccounts() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("select account.account_id, name, creation_date from account left join account_timed_state on account.account_id = account_timed_state.account_id where pricing_model = 1 and account.account_state = 9 and account_timed_state.state_change_time is null");
            ResultSet accounts = stmt.executeQuery();
            PreparedStatement addBelatedStmt = conn.prepareStatement("insert into account_timed_state (state_change_time, account_id, account_state) values (?, ?, ?)");
            while (accounts.next()) {
                long accountID = accounts.getLong(1);
                String name = accounts.getString(2);
                Date creationDate = accounts.getTimestamp(3);
                Calendar cal = Calendar.getInstance();
                cal.setTime(creationDate);
                cal.add(Calendar.DAY_OF_YEAR, 30);
                Date changeTime = cal.getTime();
                System.out.println("For " + name + " created at " + creationDate + " adding timed state change of " + changeTime);
                addBelatedStmt.setTimestamp(1, new Timestamp(changeTime.getTime()));
                addBelatedStmt.setLong(2, accountID);
                addBelatedStmt.setInt(3, Account.ACTIVE);
                addBelatedStmt.execute();
            }
            stmt.close();
            addBelatedStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void forceTrialExpire() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement insert = conn.prepareStatement("INSERT INTO REVISED_ACTION_LOG (GENERAL_ACTION_TYPE, ACTION_TYPE, ACTION_DATE, " +
                    "DASHBOARD_ID, REPORT_ID, DATA_SOURCE_ID, USER_ID) VALUES (?, ?, ?, ?, ?, ?, ?)");
            Session session = Database.instance().createSession(conn);
            List<ActionLog> actionLogs = session.createQuery("from ActionLog").list();
            for (ActionLog actionLog : actionLogs) {
                try {
                    if (actionLog instanceof ActionReportLog) {
                        ActionReportLog actionReportLog = (ActionReportLog) actionLog;
                        insert.setInt(1, REPORT);
                        insert.setInt(2, actionLog.getActionType());
                        insert.setTimestamp(3, new Timestamp(actionLog.getActionDate().getTime()));
                        insert.setNull(4, Types.BIGINT);
                        insert.setLong(5, actionReportLog.getReportID());
                        insert.setNull(6, Types.BIGINT);
                        insert.setLong(7, actionLog.getUserID());
                        insert.execute();
                    } else if (actionLog instanceof ActionDashboardLog) {
                        ActionDashboardLog actionDashboardLog = (ActionDashboardLog) actionLog;
                        insert.setInt(1, DASHBOARD);
                        insert.setInt(2, actionLog.getActionType());
                        insert.setTimestamp(3, new Timestamp(actionLog.getActionDate().getTime()));
                        insert.setLong(4, actionDashboardLog.getDashboardID());
                        insert.setNull(5, Types.BIGINT);
                        insert.setNull(6, Types.BIGINT);
                        insert.setLong(7, actionLog.getUserID());
                        insert.execute();
                    } else if (actionLog instanceof ActionDataSourceLog) {
                        ActionDataSourceLog actionDataSourceLog = (ActionDataSourceLog) actionLog;
                        insert.setInt(1, DATA_SOURCE);
                        insert.setInt(2, actionLog.getActionType());
                        insert.setTimestamp(3, new Timestamp(actionLog.getActionDate().getTime()));
                        insert.setNull(4, Types.BIGINT);
                        insert.setNull(5, Types.BIGINT);
                        insert.setLong(6, actionDataSourceLog.getDataSourceID());
                        insert.setLong(7, actionLog.getUserID());
                        insert.execute();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            session.close();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void syncInvoices() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        Session s = Database.instance().createSession(conn);
        try {

            List<Account> l = (List<Account>) s.createQuery("from Account where pricingModel = ?").setInteger(0, Account.NEW).list();

            for(Account a : l) {
                if (a.getAccountState() != Account.DELINQUENT && a.getAccountState() != Account.BILLING_FAILED) {
                    Transaction t = s.beginTransaction();
                    try {
                        a.syncState();
                        s.update(a);
                        s.flush();
                        t.commit();
                    } catch (Exception e) {
                        LogClass.error(e);
                        t.rollback();
                    }
                }
            }

        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            s.close();
            Database.closeConnection(conn);
        }
    }

    public void requestEnterpriseAccess(final String email, final String phone, final boolean salesforce, final boolean quickbase) {
        final long accountID = SecurityUtil.getAccountID();
        new Thread(new Runnable() {

            public void run() {
                String body = "Account: " + accountID + "\nEmail: " + email + "\nPhone: " + phone +
                        "\nSalesforce: " + salesforce + "\nQuickBase: " + quickbase;
                try {
                    new SendGridEmail().sendEmail("sales@easy-insight.com", "Request for Enterprise Connection", body, "donotreply@easy-insight.com", false, "Easy Insight");
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        }).start();
    }

    public void addNews(NewsEntry newsEntry) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            if (newsEntry.getId() == 0) {
                PreparedStatement insertNewStmt = conn.prepareStatement("INSERT INTO NEWS_ENTRY (NEWS_ENTRY_TEXT, ENTRY_TIME, NEWS_ENTRY_TITLE, TAGS, NEWS_AUTHOR) VALUES (?, ?, ?, ?, ?)");
                insertNewStmt.setString(1, newsEntry.getNews());
                insertNewStmt.setTimestamp(2, new Timestamp(newsEntry.getDate().getTime()));
                insertNewStmt.setString(3, newsEntry.getTitle());
                insertNewStmt.setString(4, newsEntry.getTags());
                insertNewStmt.setString(5, newsEntry.getAuthor());
                insertNewStmt.execute();
            } else {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE NEWS_ENTRY SET NEWS_ENTRY_TEXT = ?, ENTRY_TIME = ?, NEWS_ENTRY_TITLE = ?, TAGS = ?, NEWS_AUTHOR = ? WHERE " +
                        "NEWS_ENTRY_ID = ?");
                updateStmt.setString(1, newsEntry.getNews());
                updateStmt.setTimestamp(2, new Timestamp(newsEntry.getDate().getTime()));
                updateStmt.setString(3, newsEntry.getTitle());
                updateStmt.setString(4, newsEntry.getTags());
                updateStmt.setString(5, newsEntry.getAuthor());
                updateStmt.setLong(6, newsEntry.getId());
                updateStmt.executeUpdate();
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<String> getNewsTags() {
        List<String> tags = new ArrayList<String>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("");
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return tags;
    }

    public List<NewsEntry> getNews() {
        List<NewsEntry> entries = new ArrayList<NewsEntry>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT NEWS_ENTRY_TEXT, ENTRY_TIME, NEWS_ENTRY_ID, NEWS_ENTRY_TITLE, TAGS, NEWS_AUTHOR FROM NEWS_ENTRY");
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                NewsEntry newsEntry = new NewsEntry();
                newsEntry.setNews(rs.getString(1));
                newsEntry.setDate(new Date(rs.getTimestamp(2).getTime()));
                newsEntry.setId(rs.getLong(3));
                newsEntry.setTitle(rs.getString(4));
                newsEntry.setTags(rs.getString(5));
                newsEntry.setAuthor(rs.getString(6));
                entries.add(newsEntry);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return entries;
    }

    public void applyDays() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List infos = session.createQuery("from AccountCreditCardBillingInfo ").list();
            for (Object infoObj : infos) {
                AccountCreditCardBillingInfo info = (AccountCreditCardBillingInfo) infoObj;
                if ("100".equals(info.getResponseCode())) {
                    double amount = Double.parseDouble(info.getAmount());
                    if (amount == 275) {
                        info.setDays(365);
                    } else if (amount == 825) {
                        info.setDays(365);
                    } else if (amount == 200) {
                        info.setDays(28);
                    } else if (amount == 75) {
                        info.setDays(28);
                    } else if (amount == 25) {
                        info.setDays(28);
                    }
                }
                session.update(info);
            }
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void logAction(ActionLog actionLog) {
        if (actionLog instanceof ActionScorecardLog) {
            return;
        }
        long userID = SecurityUtil.getUserID(false);
        if (userID == 0) {
            return;
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO REVISED_ACTION_LOG (GENERAL_ACTION_TYPE, ACTION_TYPE, ACTION_DATE, USER_ID, " +
                    "DATA_SOURCE_ID, REPORT_ID, DASHBOARD_ID) VALUES (?, ?, ?, ?, ?, ?, ?)");
            insertStmt.setInt(2, actionLog.getActionType());
            insertStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            insertStmt.setLong(4, userID);
            if (actionLog instanceof ActionReportLog) {
                ActionReportLog actionReportLog = (ActionReportLog) actionLog;
                insertStmt.setInt(1, REPORT);
                insertStmt.setNull(5, Types.BIGINT);
                insertStmt.setLong(6, actionReportLog.getReportID());
                insertStmt.setNull(7, Types.BIGINT);
            } else if (actionLog instanceof ActionDashboardLog) {
                ActionDashboardLog actionDashboardLog = (ActionDashboardLog) actionLog;
                insertStmt.setInt(1, DASHBOARD);
                insertStmt.setNull(5, Types.BIGINT);
                insertStmt.setNull(6, Types.BIGINT);
                insertStmt.setLong(7, actionDashboardLog.getDashboardID());
            } else if (actionLog instanceof ActionDataSourceLog) {
                ActionDataSourceLog actionDataSourceLog = (ActionDataSourceLog) actionLog;
                insertStmt.setInt(1, DATA_SOURCE);
                insertStmt.setLong(5, actionDataSourceLog.getDataSourceID());
                insertStmt.setNull(6, Types.BIGINT);
                insertStmt.setNull(7, Types.BIGINT);
            }
            insertStmt.execute();
            insertStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }


    public void constantContactSync() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    ConstantContactSync.updateContactLists();
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        }).start();

    }
    
    public void adminClearDataSource(long dataSourceID) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            LogClass.error("INVOCATION OF ADMIN CLEAR DATA SOURCE ON " + dataSourceID);
            FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
            try {
                feedDefinition.delete(conn);
            } catch (HibernateException e) {
                LogClass.error(e);
                PreparedStatement manualDeleteStmt = conn.prepareStatement("DELETE FROM DATA_FEED WHERE DATA_FEED_ID = ?");
                manualDeleteStmt.setLong(1, dataSourceID);
                manualDeleteStmt.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    /*public void archiveOldDataSources() {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement getAccounts = conn.prepareStatement("SELECT ACCOUNT_ID FROM ACCOUNT WHERE ACCOUNT_STATE = ? AND ACCOUNT.CREATION_DATE < ?");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -6);
            getAccounts.setInt(1, Account.DELINQUENT);
            getAccounts.setTimestamp(2, new Timestamp(cal.getTime().getTime()));
            ResultSet accounts = getAccounts.executeQuery();
            PreparedStatement getDataSourcesStmt = conn.prepareStatement("SELECT DATA_FEED_ID FROM DATA_FEED, UPLOAD_POLICY_USERS, USER WHERE " +
                    "USER.ACCOUNT_ID = ? AND USER.USER_ID = UPLOAD_POLICY_USERS.USER_ID AND UPLOAD_POLICY_USERS.FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                    "DATA_FEED.VISIBLE = ?");
            PreparedStatement getScorecardStmt = conn.prepareStatement("SELECT SCORECARD_ID FROM SCORECARD, USER WHERE USER.ACCOUNT_ID = ? AND " +
                    "SCORECARD.USER_ID = USER.USER_ID");
            PreparedStatement getDashboardStmt = conn.prepareStatement("SELECT DASHBOARD_ID FROM DASHBOARD, USER, USER_TO_DASHBOARD WHERE USER.ACCOUNT_ID = ? AND " +
                    "SCORECARD.USER_ID = USER.USER_ID AND USER_TO_DASHBOARD.USER_ID = USER.USER_ID AND USER.USER_");
            PreparedStatement hideDataSourceStmt = conn.prepareStatement("UPDATE DATA_FEED SET VISIBLE = ?, ARCHIVED = ? WHERE DATA_FEED.DATA_FEED_ID = ?");
            PreparedStatement hideReportStmt = conn.prepareStatement("UPDATE ANALYSIS SET TEMPORARY_REPORT = ?, ARCHIVED = ? WHERE DATA_FEED.DATA_FEED_ID = ?");
            PreparedStatement hideDashboardStmt = conn.prepareStatement("UPDATE DASHBOARD SET TEMPORARY_DASHBOARD = ?, archived = ? WHERE dashboard.data_source_id = ?");
            PreparedStatement hideScorecardStmt = conn.prepareStatement("UPDATE SCORECARD SET scorecard_visible = ?, archived = ? where scorecard.data_source_id = ?");
            while (accounts.next()) {
                long accountID = accounts.getLong(1);
                getDataSourcesStmt.setLong(1, accountID);
                ResultSet dataSources = getDataSourcesStmt.executeQuery();
                while (dataSources.next()) {
                    long dataSourceID = dataSources.getLong(1);
                    hideDataSourceStmt.setBoolean(1, true);
                    hideDataSourceStmt.setBoolean(2, true);
                    hideDataSourceStmt.setLong(3, dataSourceID);
                    hideDataSourceStmt.executeUpdate();
                    hideReportStmt.setBoolean(1, false);
                    hideReportStmt.setBoolean(2, true);
                    hideReportStmt.setLong(3, dataSourceID);
                    hideReportStmt.executeUpdate();
                    hideDashboardStmt.setBoolean(1, false);
                    hideDashboardStmt.setBoolean(2, true);
                    hideDashboardStmt.setLong(3, dataSourceID);
                    hideDashboardStmt.executeUpdate();
                    hideScorecardStmt.setBoolean(1, false);
                    hideScorecardStmt.setBoolean(2, true);
                    hideScorecardStmt.setLong(3, dataSourceID);
                    hideScorecardStmt.executeUpdate();
                }
                getScorecardStmt.setLong(1, accountID);
                ResultSet scorecards = getScorecardStmt.executeQuery();
                while (scorecards.next()) {
                    long scorecardID = scorecards.getLong(1);

                }

            }


            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }*/

    public static final int REPORT = 1;
    public static final int DATA_SOURCE = 2;
    public static final int DASHBOARD = 3;

    private static class RecentActionInfo {
        int generalActionType;
        int actionType;
        Date actionDate;
        long dataSourceID;
        long reportID;
        long dashboardID;

        private RecentActionInfo(int generalActionType, int actionType, Date actionDate, long dataSourceID, long report, long dashboardID) {
            this.generalActionType = generalActionType;
            this.actionType = actionType;
            this.actionDate = actionDate;
            this.dataSourceID = dataSourceID;
            this.reportID = report;
            this.dashboardID = dashboardID;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RecentActionInfo recentActionInfo = (RecentActionInfo) o;

            if (actionType != recentActionInfo.actionType) return false;
            if (dashboardID != recentActionInfo.dashboardID) return false;
            if (dataSourceID != recentActionInfo.dataSourceID) return false;
            if (generalActionType != recentActionInfo.generalActionType) return false;
            if (reportID != recentActionInfo.reportID) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = generalActionType;
            result = 31 * result + actionType;
            result = 31 * result + (int) (dataSourceID ^ (dataSourceID >>> 32));
            result = 31 * result + (int) (reportID ^ (reportID >>> 32));
            result = 31 * result + (int) (dashboardID ^ (dashboardID >>> 32));
            return result;
        }
    }

    public Collection<ActionLog> getRecentActions() {
        long start = System.currentTimeMillis();

        EIConnection conn = Database.instance().getConnection();
        try {

            PreparedStatement queryActionStmt = conn.prepareStatement("SELECT GENERAL_ACTION_TYPE, ACTION_TYPE, ACTION_DATE, DATA_SOURCE_ID, REPORT_ID, DASHBOARD_ID FROM REVISED_ACTION_LOG " +
                    "WHERE REVISED_ACTION_LOG.USER_ID = ? ORDER BY REVISED_ACTION_LOG.ACTION_DATE DESC LIMIT 30");
            PreparedStatement dashboardStmt = conn.prepareStatement("SELECT DASHBOARD_NAME, url_key, data_source_id FROM DASHBOARD WHERE DASHBOARD_ID = ?");
            PreparedStatement reportStmt = conn.prepareStatement("SELECT DATA_FEED_ID, REPORT_TYPE, TITLE, URL_KEY FROM ANALYSIS WHERE ANALYSIS_ID = ?");
            PreparedStatement dataSourceStmt = conn.prepareStatement("SELECT FEED_NAME FROM DATA_FEED WHERE DATA_FEED_ID = ?");
            queryActionStmt.setLong(1, SecurityUtil.getUserID());
            ResultSet rs = queryActionStmt.executeQuery();
            Collection<RecentActionInfo> recentActionInfos = new LinkedHashSet<RecentActionInfo>();
            while (rs.next()) {
                int generalActionType = rs.getInt(1);
                int actionType = rs.getInt(2);
                Date actionDate = new Date(rs.getTimestamp(3).getTime());
                long dataSourceID = rs.getLong(4);
                long reportID = rs.getLong(5);
                long dashboardID = rs.getLong(6);
                recentActionInfos.add(new RecentActionInfo(generalActionType, actionType, actionDate, dataSourceID, reportID, dashboardID));
            }
            List<RecentActionInfo> subsetRecentActionInfos = new ArrayList<RecentActionInfo>(recentActionInfos);
            if (subsetRecentActionInfos.size() > 10) {
                subsetRecentActionInfos = subsetRecentActionInfos.subList(0, 10);
            }
            Collection<ActionLog> actions = new ArrayList<ActionLog>();
            for (RecentActionInfo recentActionInfo : subsetRecentActionInfos) {
                if (recentActionInfo.generalActionType == DASHBOARD) {
                    dashboardStmt.setLong(1, recentActionInfo.dashboardID);
                    ResultSet dsRS = dashboardStmt.executeQuery();
                    if (dsRS.next()) {
                        String dashboardName = dsRS.getString(1);
                        String urlKey = dsRS.getString(2);
                        long dsID = dsRS.getLong(3);
                        actions.add(new ActionDashboardLog(new DashboardDescriptor(dashboardName, recentActionInfo.dashboardID, urlKey, dsID, Roles.OWNER, null, false), recentActionInfo.actionType, recentActionInfo.actionDate));
                    }
                } else if (recentActionInfo.generalActionType == DATA_SOURCE) {
                    dataSourceStmt.setLong(1, recentActionInfo.dataSourceID);
                    ResultSet dsRS = dataSourceStmt.executeQuery();
                    if (dsRS.next()) {
                        String dataSourceName = dsRS.getString(1);
                        actions.add(new ActionDataSourceLog(recentActionInfo.dataSourceID, dataSourceName, recentActionInfo.actionType, recentActionInfo.actionDate));
                    }
                } else if (recentActionInfo.generalActionType == REPORT) {
                    reportStmt.setLong(1, recentActionInfo.reportID);
                    ResultSet dsRS = reportStmt.executeQuery();
                    if (dsRS.next()) {
                        long dsID = dsRS.getLong(1);
                        int reportType = dsRS.getInt(2);
                        String reportName = dsRS.getString(3);
                        String urlKey = dsRS.getString(4);
                        actions.add(new ActionReportLog(new InsightDescriptor(recentActionInfo.reportID, reportName, dsID, reportType, urlKey, Roles.OWNER, false), recentActionInfo.actionType, recentActionInfo.actionDate));
                    }
                }
            }
            queryActionStmt.close();
            dashboardStmt.close();
            reportStmt.close();
            dataSourceStmt.close();

            System.out.println((System.currentTimeMillis() - start) + " for actions");
            return actions;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }

    }

    public Collection<ActionLog> getRecentHTMLActions() {
        long start = System.currentTimeMillis();

        EIConnection conn = Database.instance().getConnection();
        try {

            PreparedStatement queryActionStmt = conn.prepareStatement("SELECT GENERAL_ACTION_TYPE, ACTION_TYPE, ACTION_DATE, DATA_SOURCE_ID, REPORT_ID, DASHBOARD_ID FROM REVISED_ACTION_LOG " +
                    "WHERE REVISED_ACTION_LOG.USER_ID = ? AND REVISED_ACTION_LOG.ACTION_TYPE = ? ORDER BY REVISED_ACTION_LOG.ACTION_DATE DESC LIMIT 30");
            PreparedStatement dashboardStmt = conn.prepareStatement("SELECT DASHBOARD_NAME, url_key, data_source_id FROM DASHBOARD WHERE DASHBOARD_ID = ?");
            PreparedStatement reportStmt = conn.prepareStatement("SELECT DATA_FEED_ID, REPORT_TYPE, TITLE, URL_KEY FROM ANALYSIS WHERE ANALYSIS_ID = ?");
            PreparedStatement dataSourceStmt = conn.prepareStatement("SELECT FEED_NAME FROM DATA_FEED WHERE DATA_FEED_ID = ?");
            queryActionStmt.setLong(1, SecurityUtil.getUserID());
            queryActionStmt.setInt(2, 2);
            ResultSet rs = queryActionStmt.executeQuery();
            Collection<RecentActionInfo> recentActionInfos = new LinkedHashSet<RecentActionInfo>();
            while (rs.next()) {
                int generalActionType = rs.getInt(1);
                int actionType = rs.getInt(2);
                Date actionDate = new Date(rs.getTimestamp(3).getTime());
                long dataSourceID = rs.getLong(4);
                long reportID = rs.getLong(5);
                long dashboardID = rs.getLong(6);
                recentActionInfos.add(new RecentActionInfo(generalActionType, actionType, actionDate, dataSourceID, reportID, dashboardID));
            }
            List<RecentActionInfo> subsetRecentActionInfos = new ArrayList<RecentActionInfo>(recentActionInfos);
            if (subsetRecentActionInfos.size() > 10) {
                subsetRecentActionInfos = subsetRecentActionInfos.subList(0, 10);
            }
            Collection<ActionLog> actions = new ArrayList<ActionLog>();
            for (RecentActionInfo recentActionInfo : subsetRecentActionInfos) {
                if (recentActionInfo.generalActionType == DASHBOARD) {
                    dashboardStmt.setLong(1, recentActionInfo.dashboardID);
                    ResultSet dsRS = dashboardStmt.executeQuery();
                    if (dsRS.next()) {
                        String dashboardName = dsRS.getString(1);
                        String urlKey = dsRS.getString(2);
                        long dsID = dsRS.getLong(3);
                        actions.add(new ActionDashboardLog(new DashboardDescriptor(dashboardName, recentActionInfo.dashboardID, urlKey, dsID, Roles.OWNER, null, false), recentActionInfo.actionType, recentActionInfo.actionDate));
                    }
                } else if (recentActionInfo.generalActionType == DATA_SOURCE) {
                    dataSourceStmt.setLong(1, recentActionInfo.dataSourceID);
                    ResultSet dsRS = dataSourceStmt.executeQuery();
                    if (dsRS.next()) {
                        String dataSourceName = dsRS.getString(1);
                        actions.add(new ActionDataSourceLog(recentActionInfo.dataSourceID, dataSourceName, recentActionInfo.actionType, recentActionInfo.actionDate));
                    }
                } else if (recentActionInfo.generalActionType == REPORT) {
                    reportStmt.setLong(1, recentActionInfo.reportID);
                    ResultSet dsRS = reportStmt.executeQuery();
                    if (dsRS.next()) {
                        long dsID = dsRS.getLong(1);
                        int reportType = dsRS.getInt(2);
                        String reportName = dsRS.getString(3);
                        String urlKey = dsRS.getString(4);
                        actions.add(new ActionReportLog(new InsightDescriptor(recentActionInfo.reportID, reportName, dsID, reportType, urlKey, Roles.OWNER, false), recentActionInfo.actionType, recentActionInfo.actionDate));
                    }
                }
            }
            queryActionStmt.close();
            dashboardStmt.close();
            reportStmt.close();
            dataSourceStmt.close();

            System.out.println((System.currentTimeMillis() - start) + " for actions");
            return actions;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void logAction(ActionLog actionLog, EIConnection conn) {
        Session session = Database.instance().createSession(conn);
        try {
            session.save(actionLog);
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            session.close();
        }
    }

    public String describeReport(String urlKey) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS_ID FROM ANALYSIS WHERE URL_KEY = ?");
            queryStmt.setString(1, urlKey);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long analysisID = rs.getLong(1);
                AnalysisDefinition report = new AnalysisStorage().getPersistableReport(analysisID, session);
                XMLMetadata xmlMetadata = new XMLMetadata();
                xmlMetadata.setConn(conn);
                return report.toXML(xmlMetadata);
            } else {
                throw new RuntimeException("Couldn't find report " + urlKey);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            session.close();
            Database.closeConnection(conn);
        }
    }

    public String runAllReports(int startAccount, int endAccount) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        int count = 0;
        long analysisID = 0;
        try {
            for (int i = startAccount; i < endAccount; i++) {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID FROM ANALYSIS, USER_TO_ANALYSIS, USER WHERE USER_TO_ANALYSIS.user_id = user.user_id AND " +
                        "USER_TO_ANALYSIS.ANALYSIS_ID = ANALYSIS.ANALYSIS_ID AND USER.ACCOUNT_ID = ?");
                queryStmt.setLong(1, i);
                ResultSet rs = queryStmt.executeQuery();
                while (rs.next()) {
                    analysisID = rs.getLong(1);
                    try {
                        WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(analysisID);
                        new DataService().list(report, new InsightRequestMetadata());
                        count++;
                    } catch (Exception e) {
                        LogClass.error(e.getMessage() + " on " + analysisID, e);
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error("Running report " + analysisID, e);
            return e.getMessage();
        } finally {
            Database.closeConnection(conn);
        }
        return "Successfully ran " + count + " reports";
    }

    public String forceRun(String urlKey) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS_ID FROM ANALYSIS WHERE URL_KEY = ?");
            queryStmt.setString(1, urlKey);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long analysisID = rs.getLong(1);
                WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(analysisID);
                ListDataResults results = (ListDataResults) new DataService().list(report, new InsightRequestMetadata());
                return "Ran Successfully with " + results.getRows().length + " rows";
            } else {
                throw new RuntimeException("Couldn't find report " + urlKey);
            }

        } catch (Exception e) {
            LogClass.error(e);
            return e.getMessage();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void updateZips(byte[] bytes) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        try {
            new ZipGeocodeCache().saveFile(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void clearOrphanData() {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement basecampStmt = conn.prepareStatement("select basecamp.basecamp_id, data_feed.data_feed_id from basecamp left join data_feed on basecamp.data_feed_id = data_feed.data_feed_id where data_feed.data_feed_id is null");
            PreparedStatement basecampCleanupStmt = conn.prepareStatement("DELETE FROM BASECAMP WHERE BASECAMP.BASECAMP_ID = ?");
            ResultSet basecampRS = basecampStmt.executeQuery();
            while (basecampRS.next()) {
                basecampCleanupStmt.setLong(1, basecampRS.getLong(1));
                basecampCleanupStmt.executeUpdate();
            }

            PreparedStatement daQuery1Stmt = conn.prepareStatement("select data_activity_task_generator_id from data_activity_task_generator left join task_generator on data_activity_task_generator.task_generator_id = task_generator.task_generator_id and task_generator.task_generator_id is null");
            PreparedStatement daQuery2Stmt = conn.prepareStatement(" select data_activity_task_generator_id from data_activity_task_generator left join scheduled_account_activity on data_activity_task_generator.scheduled_activity_id = scheduled_account_activity.scheduled_account_activity_id and scheduled_account_activity.scheduled_account_activity_id is null");
            PreparedStatement daQueryCleanupStmt = conn.prepareStatement("DELETE FROM data_activity_task_generator WHERE data_activity_task_generator.data_activity_task_generator_id = ?");
            ResultSet da1RS = daQuery1Stmt.executeQuery();
            while (da1RS.next()) {
                daQueryCleanupStmt.setLong(1, da1RS.getLong(1));
                daQueryCleanupStmt.executeUpdate();
            }
            ResultSet da2RS = daQuery2Stmt.executeQuery();
            while (da2RS.next()) {
                daQueryCleanupStmt.setLong(1, da2RS.getLong(1));
                daQueryCleanupStmt.executeUpdate();
            }

            PreparedStatement dsQuery1Stmt = conn.prepareStatement("select data_source_query_id from data_source_query left join scheduled_task on data_source_query.scheduled_task_id = scheduled_task.scheduled_task_id and scheduled_task.scheduled_task_id is null");
            PreparedStatement dsQuery2Stmt = conn.prepareStatement(" select data_source_query_id from data_source_query left join data_feed on data_source_query.data_source_id = data_feed.data_feed_id and data_feed.data_feed_id is null");
            PreparedStatement dsQueryCleanupStmt = conn.prepareStatement("DELETE FROM DATA_SOURCE_QUERY WHERE DATA_SOURCE_QUERY.data_source_query_id = ?");
            ResultSet ds1RS = dsQuery1Stmt.executeQuery();
            while (ds1RS.next()) {
                dsQueryCleanupStmt.setLong(1, ds1RS.getLong(1));
                dsQueryCleanupStmt.executeUpdate();
            }
            ResultSet ds2RS = dsQuery2Stmt.executeQuery();
            while (ds2RS.next()) {
                dsQueryCleanupStmt.setLong(1, ds2RS.getLong(1));
                dsQueryCleanupStmt.executeUpdate();
            }
            PreparedStatement dtQuery1Stmt = conn.prepareStatement("select data_source_task_generator.data_source_task_generator_id from data_source_task_generator left join task_generator on data_source_task_generator.data_source_task_generator_id = task_generator.task_generator_id where task_generator.task_generator_id is null");
            PreparedStatement dtQuery2Stmt = conn.prepareStatement("select data_source_task_generator.data_source_task_generator_id from data_source_task_generator left join data_feed on data_source_task_generator.data_source_id = data_feed.data_feed_id where data_feed.data_feed_id is null");
            PreparedStatement dtQueryCleanupStmt = conn.prepareStatement("DELETE FROM data_source_task_generator WHERE data_source_task_generator.data_source_task_generator_id = ?");
            ResultSet dt1RS = dtQuery1Stmt.executeQuery();
            while (dt1RS.next()) {
                dtQueryCleanupStmt.setLong(1, dt1RS.getLong(1));
                dtQueryCleanupStmt.executeUpdate();
            }
            ResultSet dt2RS = dtQuery2Stmt.executeQuery();
            while (dt2RS.next()) {
                dtQueryCleanupStmt.setLong(1, dt2RS.getLong(1));
                dtQueryCleanupStmt.executeUpdate();
            }
            PreparedStatement taskStmt = conn.prepareStatement("select scheduled_task.scheduled_task_id from scheduled_task left join task_generator on scheduled_task.task_generator_id = task_generator.task_generator_id where task_generator.task_generator_id is null");
            PreparedStatement taskCleanupStmt = conn.prepareStatement("delete from scheduled_task where scheduled_task_id = ?");
            ResultSet taskRS = taskStmt.executeQuery();
            while (taskRS.next()) {
                taskCleanupStmt.setLong(1, taskRS.getLong(1));
                taskCleanupStmt.executeUpdate();
            }
            PreparedStatement uiStmt = conn.prepareStatement("SELECT ui_visibility_setting.ui_visibility_setting_id from ui_visibility_setting left join persona on ui_visibility_setting.persona_id = persona.persona_id where persona.persona_id is null");
            PreparedStatement uiCleanupStmt = conn.prepareStatement("DELETE FROM ui_visibility_setting where ui_visibility_setting.ui_visibility_setting_id = ?");
            ResultSet uiRS = uiStmt.executeQuery();
            while (uiRS.next()) {
                uiCleanupStmt.setLong(1, uiRS.getLong(1));
                uiCleanupStmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void userAudit(String message) {
        long userID = SecurityUtil.getUserID();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO USER_ACTION_AUDIT (USER_ID, action_name, action_date) values (?, ?, ?)");
            insertStmt.setLong(1, userID);
            insertStmt.setString(2, message);
            insertStmt.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
            insertStmt.execute();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void welcomeEmail(long userID) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        User user;
        Session session = Database.instance().createSession();
        try {
            user = (User) session.createQuery("from User where userID = ?").setLong(0, userID).list().get(0);
        } finally {
            session.close();
        }
        try {
            new SendGridEmail().sendEmail(user.getUserID(), user.getEmail(), "Welcome", "Welcome to Easy Insight!", MessageFormat.format(SendGridEmail.BASIC_OR_PRO_EMAIL_WELCOME,
                    (user.getFirstName() != null ? user.getFirstName() : user.getName())));
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);  
        }
    }

    public void threadDump() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
        for (ThreadInfo threadInfo : threadInfos) {
            LogClass.info(threadInfo.toString());
        }
    }

    private void cleanOrphanKeys(EIConnection conn) throws SQLException {
        PreparedStatement query = conn.prepareStatement("select report_structure_id, analysis.analysis_id, analysis_item.analysis_item_id from report_structure left join analysis on report_structure.analysis_id = analysis.analysis_id left join analysis_item on report_structure.analysis_item_id = analysis_item.analysis_item_id and (analysis.analysis_id is null or analysis_item.analysis_item_id is null)");
        PreparedStatement nukeStmt = conn.prepareStatement("DELETE FROM ITEM_KEY WHERE ITEM_KEY_ID = ?");
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            long id = rs.getLong(1);
            nukeStmt.setLong(1, id);
            nukeStmt.executeUpdate();
        }
    }

    private void cleanOrphanDataSourceKeys(EIConnection conn) throws SQLException {
        PreparedStatement query = conn.prepareStatement("select item_key.item_key_id from item_key left join named_item_key on item_key.item_key_id = named_item_key.named_item_key_id " +
                "left join derived_item_key on item_key.item_key_id = derived_item_key.item_key_id where " +
                "named_item_key.named_item_key_id is null and derived_item_key.derived_item_key_id is null");
        PreparedStatement nukeStmt = conn.prepareStatement("DELETE FROM ITEM_KEY WHERE ITEM_KEY_ID = ?");
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            long id = rs.getLong(1);
            nukeStmt.setLong(1, id);
            nukeStmt.executeUpdate();
        }
    }

    private void cleanOrphanItems(EIConnection conn) throws SQLException {
        PreparedStatement query = conn.prepareStatement("select report_structure_id, analysis.analysis_id, analysis_item.analysis_item_id " +
                    "from report_structure left join analysis on report_structure.analysis_id = analysis.analysis_id left join analysis_item on " +
                    "report_structure.analysis_item_id = analysis_item.analysis_item_id WHERE (analysis.analysis_id is null or " +
                    "analysis_item.analysis_item_id is null)");
        PreparedStatement nukeStmt = conn.prepareStatement("DELETE FROM REPORT_STRUCTURE WHERE REPORT_STRUCTURE_ID = ?");
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            long id = rs.getLong(1);
            nukeStmt.setLong(1, id);
            nukeStmt.executeUpdate();
        }
    }

    private void cleanOrphanHierarchies(EIConnection conn) throws SQLException {
        PreparedStatement query = conn.prepareStatement("select analysis_hierarchy_item.analysis_hierarchy_item_id " +
                    "from analysis_hierarchy_item left join hierarchy_level on analysis_hierarchy_item.hierarchy_level_id = hierarchy_level.hierarchy_level_id where " +
                    "analysis_hierarchy_item.hierarchy_level_id is null");
        PreparedStatement nukeStmt = conn.prepareStatement("DELETE FROM ANALYSIS_HIERARCHY_ITEM WHERE ANALYSIS_HIERARCHY_ITEM_ID = ?");
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            long id = rs.getLong(1);
            nukeStmt.setLong(1, id);
            nukeStmt.executeUpdate();
        }
    }

    private void clearOrphanUploadPolicies(EIConnection conn) throws SQLException {
        PreparedStatement query = conn.prepareStatement("select upload_policy_users.upload_policy_users_id from upload_policy_users " +
                "left join data_feed on upload_policy_users.feed_id = data_feed.data_feed_id where data_feed.data_feed_id is null");
        PreparedStatement nukeStmt = conn.prepareStatement("DELETE FROM UPLOAD_POLICY_USERS WHERE UPLOAD_POLICY_USERS_ID = ?");
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            long id = rs.getLong(1);
            nukeStmt.setLong(1, id);
            nukeStmt.executeUpdate();
        }
    }

    private void clearOrphanStepStmt(EIConnection conn) throws SQLException {
        PreparedStatement nukeStmt = conn.prepareStatement("DELETE FROM ANALYSIS_STEP WHERE ANALYSIS_STEP_ID = ?");
        PreparedStatement query1 = conn.prepareStatement("SELECT ANALYSIS_STEP.analysis_step_id FROM ANALYSIS_STEP LEFT JOIN ANALYSIS_ITEM ON " +
                "ANALYSIS_STEP.correlation_dimension_id = ANALYSIS_ITEM.ANALYSIS_ITEM_ID WHERE ANALYSIS_ITEM.ANALYSIS_ITEM_ID iS NULL");
        ResultSet rs1 = query1.executeQuery();
        while (rs1.next()) {
            nukeStmt.setLong(1, rs1.getLong(1));
            nukeStmt.executeUpdate();
        }
        PreparedStatement query2 = conn.prepareStatement("SELECT ANALYSIS_STEP.analysis_step_id FROM ANALYSIS_STEP LEFT JOIN ANALYSIS_ITEM ON " +
                "ANALYSIS_STEP.start_date_dimension_id = ANALYSIS_ITEM.ANALYSIS_ITEM_ID WHERE ANALYSIS_ITEM.ANALYSIS_ITEM_ID iS NULL");
        ResultSet rs2 = query2.executeQuery();
        while (rs2.next()) {
            nukeStmt.setLong(1, rs2.getLong(1));
            nukeStmt.executeUpdate();
        }
        PreparedStatement query3 = conn.prepareStatement("SELECT ANALYSIS_STEP.analysis_step_id FROM ANALYSIS_STEP LEFT JOIN ANALYSIS_ITEM ON " +
                "ANALYSIS_STEP.end_date_dimension_id = ANALYSIS_ITEM.ANALYSIS_ITEM_ID WHERE ANALYSIS_ITEM.ANALYSIS_ITEM_ID iS NULL");
        ResultSet rs3 = query3.executeQuery();
        while (rs3.next()) {
            nukeStmt.setLong(1, rs3.getLong(1));
            nukeStmt.executeUpdate();
        }
    }

    private void cleanRawUploads(EIConnection conn) throws SQLException {
        PreparedStatement cleanStmt = conn.prepareStatement("TRUNCATE USER_UPLOAD");
        cleanStmt.execute();
    }

    public void clearOrphanData2() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            cleanOrphanItems(conn);
            cleanOrphanHierarchies(conn);
            clearOrphanUploadPolicies(conn);
            clearOrphanStepStmt(conn);
            cleanOrphanKeys(conn);
            cleanOrphanDataSourceKeys(conn);
            cleanRawUploads(conn);
            conn.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public static void main(String[] args) {
        Database.initialize();
        new AdminService().clearOrphanData();
    }

    public String generateSitemap() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        StringBuilder sitemapBuilder = new StringBuilder();
        sitemapBuilder.append("<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"\n" +
                "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "\txsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9\n" +
                "\t\t\t    http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\">");
        // retrieve all public data sources
        // create a sitemap entry for each
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED_ID, ANALYSIS_ID, REPORT_TYPE, TITLE FROM ANALYSIS " +
                    "WHERE PUBLICLY_VISIBLE = ? AND MARKETPLACE_VISIBLE = ?");
            queryStmt.setBoolean(1, true);
            queryStmt.setBoolean(2, true);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long analysisID = rs.getLong(2);
                String title = rs.getString(4);
                title = title.replaceAll("[ @\"&*#$%^~]", "-");
                String url = "https://www.easy-insight.com/reports/" + title + "-" + analysisID;                
                sitemapBuilder.append(MessageFormat.format(LOC_XML, url));
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        sitemapBuilder.append("</urlset>");
        return sitemapBuilder.toString();
    }

    public void sendShutdownNotification(String s) {
        BroadcastInfo info = new BroadcastInfo();
        if(s == null || s.isEmpty())
            info.setMessage("Please be aware that the server is going down shortly, and you will not be able to access your data.");
        else
            info.setMessage(s);
        MessageUtils.sendMessage("generalNotifications", info);        
    }

    public static final String MAX_MEMORY = "Max Memory";
    public static final String TOTAL_MEMORY = "Allocated Memory";
    public static final String FREE_UNALLOCATED = "Free Unallocated Memory";
    public static final String FREE_MEMORY = "Free Memory";
    public static final String CURRENT_MEMORY = "Current Memory";
    public static final String THREAD_COUNT = "Thread Count";
    public static final String SYSTEM_LOAD = "System Load Average";
    public static final String COMPILATION_TIME = "Compilation Time";
    public static final String MINOR_COLLECTION_COUNT = "Minor Collection Count";
    public static final String MINOR_COLLECTION_TIME = "Minor Collection Time";
    public static final String MAJOR_COLLECTION_COUNT = "Major Collection Count";
    public static final String MAJOR_COLLECTION_TIME = "Major Collection Time";
    public static final String CLIENT_COUNT = "Client Count";

    public HealthInfo getHealthInfo() {
        //SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        return new AdminProcessor().getHealthInfo();
    }

    public void delinquentToExpired() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement("SELECT ACCOUNT_ID FROM ACCOUNT WHERE ACCOUNT_STATE = ?");
            stmt.setInt(1, Account.DELINQUENT);
            PreparedStatement billing = conn.prepareStatement("SELECT COUNT(*) FROM ACCOUNT_CREDIT_CARD_BILLING_INFO WHERE ACCOUNT_ID = ?");
            PreparedStatement update = conn.prepareStatement("UPDATE ACCOUNT SET ACCOUNT_STATE = ? WHERE ACCOUNT_ID = ?");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                long accountID = rs.getLong(1);
                billing.setLong(1, accountID);
                ResultSet billingResults = billing.executeQuery();
                if (billingResults.next()) {
                    int count = billingResults.getInt(1);
                    if (count > 0) {
                        update.setInt(1, Account.BILLING_FAILED);
                        update.setLong(2, accountID);
                        update.executeUpdate();
                    }
                }
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }
}
