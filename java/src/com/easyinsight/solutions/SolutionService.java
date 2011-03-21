package com.easyinsight.solutions;

import com.easyinsight.core.EIDescriptor;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.dashboard.DashboardStorage;
import com.easyinsight.datafeeds.*;
import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.exchange.ExchangeItem;
import com.easyinsight.export.ExportService;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.AuthorizationManager;
import com.easyinsight.security.AuthorizationRequirement;
import com.easyinsight.goals.*;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.core.Key;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.users.Account;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.sql.*;
import java.io.ByteArrayInputStream;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 12:32:59 AM
 */
public class SolutionService {

    public void addKPIData(SolutionKPIData solutionKPIData) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(solutionKPIData.getDataSourceID(), conn);
            dataSource.setAccountVisible(true);
            new FeedStorage().updateDataFeedConfiguration(dataSource, conn);
            if (solutionKPIData.getActivity() != null) {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT SCHEDULED_DATA_SOURCE_REFRESH.SCHEDULED_DATA_SOURCE_REFRESH_ID FROM SCHEDULED_DATA_SOURCE_REFRESH, DATA_FEED, SCHEDULED_ACCOUNT_ACTIVITY WHERE " +
                        "SCHEDULED_DATA_SOURCE_REFRESH.data_source_id = data_feed.data_feed_id and data_feed.feed_type = ? AND " +
                        "scheduled_data_source_refresh.scheduled_account_activity_id = scheduled_account_activity.scheduled_account_activity_id and " +
                        "scheduled_account_activity.account_id = ?");
                queryStmt.setInt(1, dataSource.getFeedType().getType());
                queryStmt.setLong(2, SecurityUtil.getAccountID());
                ResultSet rs = queryStmt.executeQuery();
                if (!rs.next()) {
                    new ExportService().addOrUpdateSchedule(solutionKPIData.getActivity(), solutionKPIData.getUtcOffset(), conn);
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

    public List<Long> getInstalledConnections() {
        EIConnection conn = Database.instance().getConnection();
        try {
            Set<Long> connectionIDs = new HashSet<Long>();
            List<DataSourceDescriptor> dataSources = new FeedStorage().getDataSources(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn);
            if (dataSources.isEmpty()) {
                return new ArrayList<Long>();
            }
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT SOLUTION.SOLUTION_ID FROM SOLUTION WHERE DATA_SOURCE_TYPE IN (");
            //noinspection UnusedDeclaration
            for (DataSourceDescriptor dataSourceDescriptor : dataSources) {
                queryBuilder.append("?,");
            }
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
            queryBuilder.append(")");
            PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString());
            int i = 1;
            for (DataSourceDescriptor dataSourceDescriptor : dataSources) {
                stmt.setInt(i++, dataSourceDescriptor.getDataSourceType());
            }
            ResultSet userRS = stmt.executeQuery();
            while (userRS.next()) {
                long connectionID = userRS.getLong(1);
                connectionIDs.add(connectionID);
            }
            return new ArrayList<Long>(connectionIDs);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public Solution retrieveSolution(long solutionID) {
        try {
            return getSolution(solutionID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void deleteSolution(long solutionID) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM SOLUTION WHERE SOLUTION_ID = ?");
            stmt.setLong(1, solutionID);
            stmt.executeUpdate();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public byte[] getSolutionArchive(long solutionID) {
        byte[] bytes;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ARCHIVE, SOLUTION_ARCHIVE_NAME FROM SOLUTION WHERE SOLUTION_ID = ?");
            queryStmt.setLong(1, solutionID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                bytes = rs.getBytes(1);
            } else {
                throw new RuntimeException("No data found for that file ID.");
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return bytes;
    }

    public void addSolutionArchive(byte[] archive, long solutionID, String solutionArchiveName) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement updateArchiveStmt = conn.prepareStatement("UPDATE SOLUTION SET ARCHIVE = ?, SOLUTION_ARCHIVE_NAME = ? WHERE SOLUTION_ID = ?");
            ByteArrayInputStream bais = new ByteArrayInputStream(archive);

            updateArchiveStmt.setBinaryStream(1, bais, archive.length);
            //updateArchiveStmt.setBytes(1, archive);
            updateArchiveStmt.setString(2, solutionArchiveName);
            updateArchiveStmt.setLong(3, solutionID);
            updateArchiveStmt.executeUpdate();
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public long addSolution(Solution solution) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement insertSolutionStmt = conn.prepareStatement("INSERT INTO SOLUTION (NAME, DESCRIPTION, INDUSTRY, AUTHOR, COPY_DATA, " +
                    "SOLUTION_TIER, CATEGORY, logo_link, data_source_type) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            insertSolutionStmt.setString(i++, solution.getName());
            insertSolutionStmt.setString(i++, solution.getIndustry());
            insertSolutionStmt.setBoolean(i++, solution.isCopyData());
            insertSolutionStmt.setInt(i++, solution.getSolutionTier());
            insertSolutionStmt.setInt(i++, solution.getCategory());
            insertSolutionStmt.setString(i++, solution.getLogoLink());
            insertSolutionStmt.setInt(i, solution.getDataSourceType());
            insertSolutionStmt.execute();
            long solutionID = Database.instance().getAutoGenKey(insertSolutionStmt);
            conn.commit();
            return solutionID;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(false);
            Database.closeConnection(conn);
        }
    }

    public void updateSolution(Solution solution) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement updateSolutionStmt = conn.prepareStatement("UPDATE SOLUTION SET NAME = ?, INDUSTRY = ?,  " +
                    "COPY_DATA = ?, SOLUTION_TIER = ?, CATEGORY = ?, " +
                    "logo_link = ?, DATA_SOURCE_TYPE = ? WHERE SOLUTION_ID = ?",
                    Statement.RETURN_GENERATED_KEYS);
            updateSolutionStmt.setString(1, solution.getName());
            updateSolutionStmt.setString(2, solution.getIndustry());
            updateSolutionStmt.setBoolean(3, solution.isCopyData());
            updateSolutionStmt.setInt(4, solution.getSolutionTier());
            updateSolutionStmt.setInt(5, solution.getCategory());
            updateSolutionStmt.setString(6, solution.getLogoLink());
            updateSolutionStmt.setInt(7, solution.getDataSourceType());
            updateSolutionStmt.setLong(8, solution.getSolutionID());
            updateSolutionStmt.executeUpdate();
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

    public AuthorizationRequirement determineAuthorizationRequirements(int feedType, long solutionID) {
        FeedType type = new FeedType(feedType);
        return new AuthorizationManager().authorize(type, solutionID);        
    }

    public long connectionInstalled(long solutionID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_SOURCE_TYPE FROM SOLUTION WHERE SOLUTION_ID = ?");
            queryStmt.setLong(1, solutionID);
            ResultSet solRS = queryStmt.executeQuery();
            solRS.next();
            int dataSourceType = solRS.getInt(1);
            List<DataSourceDescriptor> dataSources = new FeedStorage().getDataSources(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn);
            for (DataSourceDescriptor dataSource : dataSources) {
                if (dataSource.getDataSourceType() == dataSourceType) {
                    return dataSource.getId();
                }
            }
            return 0;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<DataSourceDescriptor> determineDataSourceForEntity(EIDescriptor descriptor) {
        EIConnection conn = Database.instance().getConnection();
        try {
            if (descriptor.getType() == EIDescriptor.REPORT) {
                return determineDataSourceForReport(descriptor.getId(), conn).descriptors;
            } else if (descriptor.getType() == EIDescriptor.DASHBOARD) {
                return determineDataSourceForDashboard(descriptor.getId(), conn).descriptors;
            } else {
                throw new UnsupportedOperationException("Unsupported exchange type " + descriptor.getType());
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private Blah determineDataSourceForDashboard(long dashboardID, EIConnection conn) throws SQLException {
        PreparedStatement dashboardQuery = conn.prepareStatement("SELECT DASHBOARD.data_source_id FROM DASHBOARD WHERE DASHBOARD_ID = ?");
        dashboardQuery.setLong(1, dashboardID);
        ResultSet dashboardRS = dashboardQuery.executeQuery();
        if (dashboardRS.next()) {
            long dataSourceID = dashboardRS.getLong(1);
            return determineDataSources(conn, dataSourceID);
        }
        return null;
    }

    private static class Blah {
        private List<DataSourceDescriptor> descriptors;
        private long connectionID;
        private String connectionName;

        private Blah(List<DataSourceDescriptor> descriptors, long connectionID, String connectionName) {
            this.descriptors = descriptors;
            this.connectionID = connectionID;
            this.connectionName = connectionName;
        }
    }

    public ReportTemplateInfo determineDataSourceForURLKey(String urlKey) {
        EIConnection conn = Database.instance().getConnection();
        try {
            ReportTemplateInfo reportTemplateInfo = new ReportTemplateInfo();
            ExchangeItem srei;
            PreparedStatement reportQueryStmt = conn.prepareStatement("SELECT ANALYSIS_ID, DATA_FEED_ID, TITLE, CREATE_DATE, AUTHOR_NAME, DESCRIPTION " +
                    "FROM ANALYSIS WHERE ANALYSIS.solution_visible = ? AND ANALYSIS.url_key = ?");
            reportQueryStmt.setBoolean(1, true);
            reportQueryStmt.setString(2, urlKey);
            Blah blah;
            ResultSet rs = reportQueryStmt.executeQuery();
            if (rs.next()) {
                long reportID = rs.getLong(1);
                long dataSourceID = rs.getLong(2);
                String reportName = rs.getString(3);
                Date dateCreated = new Date(rs.getTimestamp(4).getTime());
                String authorName = rs.getString(5);
                String description = rs.getString(6);
                EIDescriptor descriptor = new InsightDescriptor(reportID, null, dataSourceID, 0, null, Roles.NONE);
                PreparedStatement ratingStmt = conn.prepareStatement("SELECT count(exchange_report_install_id) from " +
                    "exchange_report_install where report_id = ?");
                ratingStmt.setLong(1, reportID);
                ResultSet ratingRS = ratingStmt.executeQuery();
                ratingRS.next();
                int installs = ratingRS.getInt(1);
                blah = determineDataSourceForReport(reportID, conn);
                srei = new ExchangeItem(reportName, reportID, installs, dateCreated, description,
                        authorName, descriptor, blah.connectionID, blah.connectionName);
            } else {
                PreparedStatement dashboardQueryStmt = conn.prepareStatement("SELECT DASHBOARD_ID, URL_KEY, DASHBOARD_NAME, CREATION_DATE," +
                        "AUTHOR_NAME, DESCRIPTION FROM DASHBOARD WHERE DASHBOARD.exchange_visible = ? and " +
                        "DASHBOARD.url_key = ?");
                dashboardQueryStmt.setBoolean(1, true);
                dashboardQueryStmt.setString(2, urlKey);
                ResultSet dashboardRS = dashboardQueryStmt.executeQuery();
                if (dashboardRS.next()) {
                    long dashboardID = dashboardRS.getLong(1);
                    String dashboardURLKey = dashboardRS.getString(2);
                    String dashboardName = dashboardRS.getString(3);
                    Date dateCreated = new Date(dashboardRS.getTimestamp(4).getTime());
                    String authorName = dashboardRS.getString(5);
                    String description = dashboardRS.getString(6);
                    EIDescriptor descriptor = new DashboardDescriptor(null, dashboardID, dashboardURLKey, 0, Roles.NONE, null);
                    blah = determineDataSourceForDashboard(dashboardID, conn);
                    PreparedStatement ratingStmt = conn.prepareStatement("SELECT count(exchange_dashboard_install_id) FROM EXCHANGE_DASHBOARD_INSTALL WHERE DASHBOARD_ID = ?");
                    ratingStmt.setLong(1, dashboardID);
                    ResultSet ratingRS = ratingStmt.executeQuery();
                    ratingRS.next();
                    int installs = ratingRS.getInt(1);
                    srei = new ExchangeItem(dashboardName, dashboardID, installs, dateCreated, description,
                            authorName, descriptor, blah.connectionID, blah.connectionName);
                } else {
                    return null;
                }
            }
            reportTemplateInfo.setExchangeData(srei);
            reportTemplateInfo.setDataSources(blah.descriptors);
            return reportTemplateInfo;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private Blah determineDataSourceForReport(long reportID, EIConnection conn) throws SQLException {
        PreparedStatement reportQuery = conn.prepareStatement("SELECT ANALYSIS.data_feed_id " +
                "FROM ANALYSIS WHERE ANALYSIS_ID = ?");
        reportQuery.setLong(1, reportID);
        ResultSet reportRS = reportQuery.executeQuery();
        if (reportRS.next()) {
            long dataSourceID = reportRS.getLong(1);
            return determineDataSources(conn, dataSourceID);
        }
        return null;
    }

    private Blah determineDataSources(EIConnection conn, long originalDataSourceID) throws SQLException {
        List<DataSourceDescriptor> descriptors = new ArrayList<DataSourceDescriptor>();
        List<DataSourceDescriptor> dataSources = new FeedStorage().getDataSources(SecurityUtil.getUserID(), SecurityUtil.getAccountID());

        PreparedStatement connStmt = conn.prepareStatement("SELECT SOLUTION.SOLUTION_ID, SOLUTION.NAME FROM SOLUTION, DATA_FEED WHERE " +
                "SOLUTION.data_source_type = DATA_FEED.feed_type AND DATA_FEED.data_feed_id = ?");
        connStmt.setLong(1, originalDataSourceID);
        ResultSet connRS = connStmt.executeQuery();
        connRS.next();
        long connectionID = connRS.getLong(1);
        String connectionName = connRS.getString(2);

        PreparedStatement solutionStmt = conn.prepareStatement("SELECT DATA_FEED.feed_type FROM DATA_FEED WHERE DATA_FEED_ID = ?");
        solutionStmt.setLong(1, originalDataSourceID);
        ResultSet sourceRS = solutionStmt.executeQuery();
        sourceRS.next();
        int dataSourceType = sourceRS.getInt(1);

        for (DataSourceDescriptor dataSource : dataSources) {
            if (dataSource.getDataSourceType() == dataSourceType) {
                descriptors.add(dataSource);
            }
        }
        if (descriptors.size() > 1) {
            describe(descriptors, conn);
        }
        return new Blah(descriptors, connectionID, connectionName);
    }

    private static class ExtraDataSourceInfo {
        private Date creationDate;
        private int feedType;

        private ExtraDataSourceInfo(Date creationDate, int feedType) {
            this.creationDate = creationDate;
            this.feedType = feedType;
        }
    }

    private void describe(List<DataSourceDescriptor> descriptors, EIConnection conn) throws SQLException {
        PreparedStatement queryReportCountStmt = conn.prepareStatement("SELECT COUNT(ANALYSIS_ID) FROM ANALYSIS WHERE " +
                "ANALYSIS.data_feed_id = ? AND ANALYSIS.temporary_report = ?");
        for (int i = 0; i < descriptors.size(); i++) {
            DataSourceDescriptor descriptor = descriptors.get(i); 

            String description;
            queryReportCountStmt.setLong(1, descriptor.getId());
            queryReportCountStmt.setBoolean(2, false);
            ResultSet rs = queryReportCountStmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            String dateString = SimpleDateFormat.getDateInstance().format(descriptor.getCreationDate());
            if (count == 0) {
                description = "this data source was created on " + dateString + " and has no reports.";
            } else if (count == 1) {
                description = "this data source was created on " + dateString + " and has 1 report.";
            } else {
                description = "this data source was created on " + dateString + " and has " + count + " reports.";
            }
            descriptor.setDescription(description);
        }
    }

    public EIDescriptor installEntity(EIDescriptor descriptor, long dataSourceID) {
        if (descriptor.getType() == EIDescriptor.DASHBOARD) {
            return installDashboard(descriptor.getId(), dataSourceID);
        } else if (descriptor.getType() == EIDescriptor.REPORT) {
            return installReport(descriptor.getId(), dataSourceID);
        } else {
            throw new RuntimeException("Unknown descriptor type " + descriptor.getType());
        }
    }

    public DashboardDescriptor installDashboard(long dashboardID, long dataSourceID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            Session session = Database.instance().createSession(conn);
            DashboardStorage dashboardStorage = new DashboardStorage();
            FeedStorage feedStorage = new FeedStorage();
            Dashboard dashboard = dashboardStorage.getDashboard(dashboardID, conn);
            Set<Long> reportIDs = dashboard.containedReports();
            List<AnalysisDefinition> reports = new ArrayList<AnalysisDefinition>();

            for (Long containedReportID : reportIDs) {
                AnalysisDefinition report = new AnalysisStorage().getPersistableReport(containedReportID, session);
                reports.add(report);
                Set<Long> containedReportIDs = report.containedReportIDs();
                for (Long childReportID : containedReportIDs) {
                    reports.add(new AnalysisStorage().getPersistableReport(childReportID, session));
                }
            }

            Map<Long, AnalysisDefinition> reportReplacementMap = new HashMap<Long, AnalysisDefinition>();

            FeedDefinition targetDataSource = feedStorage.getFeedDefinitionData(dataSourceID, conn);
            
            List<AnalysisDefinition> reportList = new ArrayList<AnalysisDefinition>();
            for (AnalysisDefinition child : reports) {
                AnalysisDefinition copyReport = copyReportToDataSource(targetDataSource, child);
                reportReplacementMap.put(child.getAnalysisID(), copyReport);
                reportList.add(copyReport);
            }

            for (AnalysisDefinition copiedReport : reportList) {
                new AnalysisStorage().saveAnalysis(copiedReport, session);
            }

            for (AnalysisDefinition copiedReport : reportReplacementMap.values()) {
                copiedReport.updateReportIDs(reportReplacementMap);
            }

            for (AnalysisDefinition copiedReport : reportList) {
                new AnalysisStorage().saveAnalysis(copiedReport, session);
            }

            Dashboard copiedDashboard = dashboard.cloneDashboard(reportReplacementMap, true, targetDataSource.getFields(), targetDataSource);

            copiedDashboard.setDataSourceID(dataSourceID);

            dashboardStorage.saveDashboard(copiedDashboard, conn);

            DashboardDescriptor dashboardDescriptor = new DashboardDescriptor(copiedDashboard.getName(), copiedDashboard.getId(), copiedDashboard.getUrlKey(), 0, Roles.NONE, null);

            session.flush();

            conn.commit();
            session.close();

            return dashboardDescriptor;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public InsightDescriptor installReport(long reportID, long dataSourceID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            FeedStorage feedStorage = new FeedStorage();


            Session session = Database.instance().createSession(conn);
            AnalysisDefinition originalBaseReport = new AnalysisStorage().getPersistableReport(reportID, session);
            //FeedDefinition sourceDataSource = feedStorage.getFeedDefinitionData(originalBaseReport.getDataFeedID(), conn);
            // okay, we might have multiple reports here...
            // find all the other reports in the dependancy graph here
            List<AnalysisDefinition> reports = originalBaseReport.containedReports(session);
            Set<Long> containedReportIDs = originalBaseReport.containedReportIDs();
            for (Long containedReportID : containedReportIDs) {
                reports.add(new AnalysisStorage().getPersistableReport(containedReportID, session));
            }
            reports.add(originalBaseReport);
            FeedDefinition targetDataSource = feedStorage.getFeedDefinitionData(dataSourceID, conn);
            Map<Long, AnalysisDefinition> reportReplacementMap = new HashMap<Long, AnalysisDefinition>();
            List<AnalysisDefinition> reportList = new ArrayList<AnalysisDefinition>();
            for (AnalysisDefinition child : reports) {

                //Map<Key, Key> keyReplacementMap = createKeyReplacementMap(targetDataSource, sourceDataSource);
                AnalysisDefinition copyReport = copyReportToDataSource(targetDataSource, child);
                reportReplacementMap.put(child.getAnalysisID(), copyReport);
                reportList.add(copyReport);
            }

            for (AnalysisDefinition copiedReport : reportList) {
                new AnalysisStorage().saveAnalysis(copiedReport, session);
            }

            for (AnalysisDefinition copiedReport : reportReplacementMap.values()) {
                copiedReport.updateReportIDs(reportReplacementMap);
            }

            for (AnalysisDefinition copiedReport : reportList) {
                new AnalysisStorage().saveAnalysis(copiedReport, session);
            }

            session.flush();

            AnalysisDefinition copiedBaseReport = reportReplacementMap.get(reportID);
            InsightDescriptor insightDescriptor = new InsightDescriptor(copiedBaseReport.getAnalysisID(), copiedBaseReport.getTitle(),
                    copiedBaseReport.getDataFeedID(), copiedBaseReport.getReportType(), copiedBaseReport.getUrlKey(), Roles.OWNER);

            conn.commit();
            session.close();
            return insightDescriptor;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    private Map<Key, Key> createKeyReplacementMap(FeedDefinition localDefinition, FeedDefinition sourceDefinition) {
        Map<Key, Key> keys = new HashMap<Key, Key>();
        for (AnalysisItem sourceField : sourceDefinition.getFields()) {
            for (AnalysisItem targetField : localDefinition.getFields()) {
                if (sourceField.toDisplay().equals(targetField.toDisplay())) {
                    keys.put(sourceField.getKey(), targetField.getKey());
                    break;
                }
            }
        }
        return keys;
    }

    private Map<Long, Long> createDataSourceReplacementMap(FeedDefinition localDefinition, FeedDefinition sourceDefinition, EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT FEED_TYPE FROM DATA_FEED WHERE DATA_FEED_ID = ?");
        Map<Long, Long> map = new HashMap<Long, Long>();
        Map<Integer, Long> typeMap = new HashMap<Integer, Long>();
        if (localDefinition instanceof CompositeFeedDefinition) {
            CompositeFeedDefinition localCompositeFeedDefinition = (CompositeFeedDefinition) localDefinition;
            CompositeFeedDefinition sourceCompositeFeedDefinition = (CompositeFeedDefinition) sourceDefinition;
            for (CompositeFeedNode node : localCompositeFeedDefinition.getCompositeFeedNodes()) {
                queryStmt.setLong(1, node.getDataFeedID());
                ResultSet rs = queryStmt.executeQuery();
                rs.next();
                int type = rs.getInt(1);
                typeMap.put(type, node.getDataFeedID());
            }
            for (CompositeFeedNode node : sourceCompositeFeedDefinition.getCompositeFeedNodes()) {
                queryStmt.setLong(1, node.getDataFeedID());
                ResultSet rs = queryStmt.executeQuery();
                rs.next();
                int type = rs.getInt(1);
                long id = typeMap.get(type);
                map.put(id, node.getDataFeedID());
            }
        }
        map.put(localDefinition.getDataFeedID(), sourceDefinition.getDataFeedID());
        return map;
    }

    private AnalysisDefinition copyReportToDataSource(FeedDefinition localDefinition, AnalysisDefinition report) throws CloneNotSupportedException {
        AnalysisDefinition clonedReport = report.clone(localDefinition, localDefinition.getFields(), true);
        clonedReport.setSolutionVisible(false);
        clonedReport.setAnalysisPolicy(AnalysisPolicy.PRIVATE);
        clonedReport.setDataFeedID(localDefinition.getDataFeedID());

        // what to do here...

        clonedReport.setUserBindings(Arrays.asList(new UserToAnalysisBinding(SecurityUtil.getUserID(), UserPermission.OWNER)));
        clonedReport.setTemporaryReport(true);
        return clonedReport;
    }

    public List<ExchangeItem> getSolutionReports() {
        List<ExchangeItem> reports = new ArrayList<ExchangeItem>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement analysisQueryStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE, ANALYSIS.DATA_FEED_ID, ANALYSIS.REPORT_TYPE, " +
                    "analysis.create_date, ANALYSIS.DESCRIPTION, DATA_FEED.FEED_NAME, ANALYSIS.AUTHOR_NAME," +
                    "DATA_FEED.PUBLICLY_VISIBLE, SOLUTION.NAME, SOLUTION.SOLUTION_ID, ANALYSIS.url_key FROM DATA_FEED, SOLUTION, ANALYSIS " +
                    " WHERE ANALYSIS.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                    "DATA_FEED.feed_type = SOLUTION.data_source_type AND ANALYSIS.SOLUTION_VISIBLE = ?");

            PreparedStatement getReportRatingStmt = conn.prepareStatement("SELECT count(exchange_report_install_id) from " +
                    "exchange_report_install where report_id = ?");

            analysisQueryStmt.setBoolean(1, true);
            ResultSet analysisRS = analysisQueryStmt.executeQuery();
            while (analysisRS.next()) {
                long analysisID = analysisRS.getLong(1);
                if (analysisID == 0) {
                    continue;
                }
                String title = analysisRS.getString(2);
                long dataSourceID = analysisRS.getLong(3);
                int reportType = analysisRS.getInt(4);

                Date created = null;
                java.sql.Date date = analysisRS.getDate(5);
                if (date != null) {
                    created = new Date(date.getTime());
                }

                String description = analysisRS.getString(6);
                String authorName = analysisRS.getString(8);
                String connectionName = analysisRS.getString(10);
                long connectionID = analysisRS.getLong(11);
                String urlKey = analysisRS.getString(12);
                getReportRatingStmt.setLong(1, analysisID);
                ResultSet ratingRS = getReportRatingStmt.executeQuery();
                ratingRS.next();
                int installs = ratingRS.getInt(1);
                InsightDescriptor insightDescriptor = new InsightDescriptor(analysisID, title, dataSourceID, reportType, urlKey, Roles.NONE);
                ExchangeItem item = new ExchangeItem(title, analysisID,
                        installs, created, description, authorName, insightDescriptor, connectionID, connectionName);
                reports.add(item);
            }

            PreparedStatement dashboardQueryStmt = conn.prepareStatement("SELECT DASHBOARD.DASHBOARD_ID, DASHBOARD.DASHBOARD_NAME, " +
                    "SOLUTION.NAME, SOLUTION.SOLUTION_ID, dashboard.creation_date, dashboard.description," +
                    "dashboard.author_name, dashboard.url_key FROM DATA_FEED, SOLUTION, dashboard " +
                    " WHERE dashboard.data_source_id = DATA_FEED.DATA_FEED_ID AND " +
                    "data_feed.feed_type = solution.data_source_type AND dashboard.exchange_visible = ? " +
                    "AND dashboard.temporary_dashboard = ?");
            PreparedStatement getRatingDashboardStmt = conn.prepareStatement("SELECT count(exchange_dashboard_install_id) from " +
                    "exchange_dashboard_install where dashboard_id = ?");
            dashboardQueryStmt.setBoolean(1, true);
            dashboardQueryStmt.setBoolean(2, false);

            ResultSet dashboardRS = dashboardQueryStmt.executeQuery();
            while (dashboardRS.next()) {
                long dashboardID = dashboardRS.getLong(1);
                String dashboardName = dashboardRS.getString(2);
                String connectionName = dashboardRS.getString(3);
                long connectionID = dashboardRS.getLong(4);
                Date createdDate = new Date(dashboardRS.getTimestamp(5).getTime());
                String description = dashboardRS.getString(6);
                String authorName = dashboardRS.getString(7);
                String urlKey = dashboardRS.getString(8);

                getRatingDashboardStmt.setLong(1, dashboardID);
                ResultSet ratingRS = getRatingDashboardStmt.executeQuery();
                ratingRS.next();

                int installs = ratingRS.getInt(1);
                DashboardDescriptor dashboardDescriptor = new DashboardDescriptor(dashboardName, dashboardID, urlKey, 0, Roles.NONE, null);
                ExchangeItem item = new ExchangeItem(dashboardName, dashboardID, installs,
                        createdDate, description, authorName, dashboardDescriptor, connectionID, connectionName);
                reports.add(item);
            }
            Collections.sort(reports, new Comparator<ExchangeItem>() {

                public int compare(ExchangeItem solutionReportExchangeItem, ExchangeItem solutionReportExchangeItem1) {
                    return ((Integer)solutionReportExchangeItem1.getInstalls()).compareTo(solutionReportExchangeItem.getInstalls());
                }
            });
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return reports;
    }

    public FeedDefinition installSolution(long solutionID) {
        // establish the connection from the account/user to the solution
        // retrieve the feeds for this solution
        // retrieve the insights matching that feed
        // clone the feed/insights
        Solution solution = getSolution(solutionID);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            InstallationSystem installationSystem = new InstallationSystem(conn);
            FeedDefinition dataSource = installationSystem.installConnection(solution);
            conn.commit();
            return dataSource;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    private Solution getSolution(long solutionID) {
        Connection conn = Database.instance().getConnection();
        try {
            return getSolution(solutionID, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public Solution getSolution(long solutionID, Connection conn) throws SQLException {
        int accountType = 0;
        if (SecurityUtil.getUserID(false) > 0) {
            accountType = SecurityUtil.getAccountTier();
        }
        PreparedStatement getSolutionsStmt = conn.prepareStatement("SELECT SOLUTION_ID, NAME, INDUSTRY, COPY_DATA, SOLUTION_ARCHIVE_NAME," +
                "solution_image, solution_tier, logo_link, data_source_type FROM SOLUTION WHERE SOLUTION_ID = ?");
        getSolutionsStmt.setLong(1, solutionID);
        ResultSet rs = getSolutionsStmt.executeQuery();
        if (rs.next()) {
            String name = rs.getString(2);
            String industry = rs.getString(3);
            boolean copyData = rs.getBoolean(4);
            String solutionArchiveName = rs.getString(5);
            Solution solution = new Solution();
            solution.setName(name);
            solution.setSolutionID(solutionID);
            solution.setIndustry(industry);
            solution.setCopyData(copyData);
            solution.setSolutionArchiveName(solutionArchiveName);
            solution.setImage(rs.getBytes(6));
            solution.setSolutionTier(rs.getInt(7));
            solution.setLogoLink(rs.getString(8));
            solution.setDataSourceType(rs.getInt(9));
            solution.setAccessible(solution.getSolutionTier() <= accountType);
            solution.setInstallable(solution.getDataSourceType() > 0);
            return solution;
        } else {
            throw new RuntimeException();
        }
    }

    public List<Solution> getSolutions() {
        int accountType = 0;
        if (SecurityUtil.getUserID(false) > 0) {
            accountType = SecurityUtil.getAccountTier();
        }
        List<Solution> solutions = new ArrayList<Solution>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement getSolutionsStmt = conn.prepareStatement("SELECT SOLUTION_ID, NAME, INDUSTRY, COPY_DATA, SOLUTION_ARCHIVE_NAME, SOLUTION_TIER," +
                    "solution_image, CATEGORY, logo_link, data_source_type FROM SOLUTION WHERE SOLUTION_TIER <= ?");
            getSolutionsStmt.setInt(1, Account.ADMINISTRATOR);

            ResultSet rs = getSolutionsStmt.executeQuery();
            while (rs.next()) {
                long solutionID = rs.getLong(1);
                String name = rs.getString(2);
                String industry = rs.getString(3);
                boolean copyData = rs.getBoolean(4);
                String solutionArchiveName = rs.getString(5);
                int solutionTier = rs.getInt(6);
                byte[] solutionImage = rs.getBytes(7);
                Solution solution = new Solution();
                solution.setName(name);
                solution.setAccessible(solutionTier <= accountType);
                solution.setSolutionID(solutionID);
                solution.setIndustry(industry);
                solution.setCopyData(copyData);
                solution.setSolutionArchiveName(solutionArchiveName);
                solution.setSolutionTier(solutionTier);
                solution.setImage(solutionImage);
                solution.setCategory(rs.getInt(8));
                solution.setLogoLink(rs.getString(9));
                solution.setDataSourceType(rs.getInt(10));
                solution.setInstallable(solution.getDataSourceType() > 0);
                solutions.add(solution);
            }
            Collections.sort(solutions, new Comparator<Solution>() {

                public int compare(Solution o1, Solution o2) {
                    if (o1.isAccessible() && !o2.isAccessible()) {
                        return -1;
                    } else if (!o1.isAccessible() && o2.isAccessible()) {
                        return 1;
                    } else {
                        return o1.getName().compareTo(o2.getName());
                    }
                }
            });
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return solutions;
    }

    public void uninstallSolution(long solutionID, boolean deleteFeeds) {
        long userID = SecurityUtil.getUserID();
        Connection conn = Database.instance().getConnection();
        try {
            if (deleteFeeds) {

            }
            PreparedStatement uninstallStmt = conn.prepareStatement("DELETE FROM USER_TO_SOLUTION WHERE SOLUTION_ID = ? AND USER_ID = ?");
            uninstallStmt.setLong(1, solutionID);
            uninstallStmt.setLong(2, userID);
            uninstallStmt.executeUpdate();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public byte[] getArchive(long solutionID) {
        Connection conn = Database.instance().getConnection();
        byte[] bytes;
        try {
            Statement getArchiveStmt = conn.createStatement();
            ResultSet rs = getArchiveStmt.executeQuery("SELECT ARCHIVE FROM SOLUTION WHERE SOLUTION_ID = " + solutionID);
            if (rs.next()) {
                bytes = rs.getBytes(1);
            } else {
                throw new RuntimeException("Couldn't find archive for solution " + solutionID);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return bytes;
    }

    public List<Solution> getSolutionsWithTags(List<Tag> tags) {
        List<Solution> solutions = new ArrayList<Solution>();
        Connection conn = Database.instance().getConnection();
        try {
            StringBuilder queryBuilder = new StringBuilder("SELECT SOLUTION.SOLUTION_ID FROM SOLUTION, SOLUTION_TAG WHERE SOLUTION_TAG.TAG_NAME IN (");
            Iterator<Tag> tagIter = tags.iterator();
            while (tagIter.hasNext()) {
                tagIter.next();
                queryBuilder.append("?");
                if (tagIter.hasNext()) {
                    queryBuilder.append(",");
                }
            }
            queryBuilder.append(") AND SOLUTION_TAG.SOLUTION_ID = SOLUTION.SOLUTION_ID");
            PreparedStatement querySolutionsStmt = conn.prepareStatement(queryBuilder.toString());
            int i = 1;
            for (Tag tag : tags) {
                querySolutionsStmt.setString(i++, tag.getTagName());
            }
            ResultSet solutionIDRS = querySolutionsStmt.executeQuery();
            while (solutionIDRS.next()) {
                solutions.add(getSolution(solutionIDRS.getLong(1)));
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return solutions;
    }

    public void addSolutionImage(byte[] bytes, long solutionID) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement updateArchiveStmt = conn.prepareStatement("UPDATE SOLUTION SET SOLUTION_IMAGE = ? WHERE SOLUTION_ID = ?");
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            updateArchiveStmt.setBinaryStream(1, bais, bytes.length);
            updateArchiveStmt.setLong(2, solutionID);
            updateArchiveStmt.executeUpdate();
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
