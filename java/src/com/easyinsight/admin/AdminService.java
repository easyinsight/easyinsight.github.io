package com.easyinsight.admin;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.outboundnotifications.BroadcastInfo;
import com.easyinsight.eventing.MessageUtils;

import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.JMX;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.GarbageCollectorMXBean;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import flex.management.runtime.messaging.client.FlexClientManagerControlMBean;

/**
 * User: James Boe
 * Date: Aug 16, 2008
 * Time: 10:57:40 AM
 */
public class AdminService {

    private static final String LOC_XML = "<url>\r\n\t<loc>{0}</loc>\r\n</url>\r\n";

    public void threadDump() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
        for (ThreadInfo threadInfo : threadInfos) {
            LogClass.info(threadInfo.toString());
        }
    }

    public String generateSitemap() {
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
                long dataSourceID = rs.getLong(1);
                long analysisID = rs.getLong(2);
                int reportType = rs.getInt(3);
                String title = rs.getString(4);
                String url = "https://www.easy-insight.com/app/ReportView.jsp?dataSourceID=" + dataSourceID + "&reportID=" + analysisID +
                        "&reportType=" + reportType + "&reportName=" + title;
                url = URLEncoder.encode(url, "UTF-8");
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
        try {
            HealthInfo healthInfo = new HealthInfo();
            healthInfo.setActiveDBConnections(Database.instance().getActiveConnections());
            healthInfo.setIdleDBConnections(Database.instance().getIdleConnections());
            Runtime runtime = Runtime.getRuntime();
            long maxMemory = runtime.maxMemory();
            long totalAllocatedMemory = runtime.totalMemory();
            long freeUnallocated = maxMemory - totalAllocatedMemory;
            long freeMemory = runtime.freeMemory() + freeUnallocated;
            healthInfo.setFreeMemory(freeMemory);
            long currentMemory = maxMemory - freeMemory;
            healthInfo.setCurrentMemory(currentMemory);
            healthInfo.setMaxMemory(maxMemory);
            healthInfo.setServer("");
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            healthInfo.setThreadCount(threadMXBean.getThreadCount());
            healthInfo.setSystemLoadAverage(ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
            healthInfo.setCompilationTime(ManagementFactory.getCompilationMXBean().getTotalCompilationTime());
            for (GarbageCollectorMXBean garbageBean : ManagementFactory.getGarbageCollectorMXBeans()) { 
                if ("Copy".equals(garbageBean.getName())) {
                    healthInfo.setMinorCollectionCount(garbageBean.getCollectionCount());
                    healthInfo.setMinorCollectionTime(garbageBean.getCollectionTime());
                } else {
                    healthInfo.setMajorCollectionCount(garbageBean.getCollectionCount());
                    healthInfo.setMajorCollectionTime(garbageBean.getCollectionTime());
                }
            }
            MBeanServer platformServer = ManagementFactory.getPlatformMBeanServer();
            try {
                FlexClientManagerControlMBean mb = JMX.newMBeanProxy(platformServer, new ObjectName("flex.runtime.app:type=MessageBroker.FlexClientManager,MessageBroker=MessageBroker1,id=FlexClientManager"),
                        FlexClientManagerControlMBean.class);
                healthInfo.setClientCount(mb.getFlexClientCount());
            } catch (Exception e) {
                LogClass.error(e);
            }
            return healthInfo;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
