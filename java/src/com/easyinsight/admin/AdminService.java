package com.easyinsight.admin;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.outboundnotifications.BroadcastInfo;
import com.easyinsight.eventing.MessageUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.management.ThreadInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;

/**
 * User: James Boe
 * Date: Aug 16, 2008
 * Time: 10:57:40 AM
 */
public class AdminService {

    private static final String LOC_XML = "<url>\r\n\t<loc>{0}</loc>\r\n</url>\r\n";

    public void threadDump() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
        for (ThreadInfo threadInfo : threadInfos) {
            LogClass.info(threadInfo.toString());
        }
    }

    /*private void cleanOrphanKeys(EIConnection conn) throws SQLException {
        PreparedStatement query = conn.prepareStatement("select report_structure_id, analysis.analysis_id, analysis_item.analysis_item_id from report_structure left join analysis on report_structure.analysis_id = analysis.analysis_id left join analysis_item on report_structure.analysis_item_id = analysis_item.analysis_item_id and (analysis.analysis_id is null or analysis_item.analysis_item_id is null)");
        PreparedStatement nukeStmt = conn.prepareStatement("DELETE FROM ITEM_KEY WHERE ITEM_KEY_ID = ?");
        ResultSet rs = query.executeQuery();
        while (rs.next()) {
            long id = rs.getLong(1);
            nukeStmt.setLong(1, id);
            nukeStmt.executeUpdate();
        }
    }*/

    private void cleanOrphanItems(EIConnection conn) throws SQLException {
        PreparedStatement query = conn.prepareStatement("select report_structure_id, analysis.analysis_id, analysis_item.analysis_item_id " +
                    "from report_structure left join analysis on report_structure.analysis_id = analysis.analysis_id left join analysis_item on " +
                    "report_structure.analysis_item_id = analysis_item.analysis_item_id and analysis.analysis_id is null and " +
                    "analysis_item.analysis_item_id is null");
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

    public void clearOrphanData() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            cleanOrphanItems(conn);
            cleanOrphanHierarchies(conn);
            clearOrphanUploadPolicies(conn);
            clearOrphanStepStmt(conn);
            //cleanOrphanKeys(conn);
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
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        return new AdminProcessor().getHealthInfo();
    }
}
