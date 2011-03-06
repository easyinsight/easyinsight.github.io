package com.easyinsight.admin;

import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.email.SendGridEmail;
import com.easyinsight.logging.LogClass;
import com.easyinsight.outboundnotifications.BroadcastInfo;
import com.easyinsight.eventing.MessageUtils;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.management.ThreadInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import com.easyinsight.users.User;
import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Aug 16, 2008
 * Time: 10:57:40 AM
 */
public class AdminService {

    private static final String LOC_XML = "<url>\r\n\t<loc>{0}</loc>\r\n</url>\r\n";

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
                return report.toXML();
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
}
