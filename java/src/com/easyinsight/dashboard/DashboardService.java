package com.easyinsight.dashboard;

import com.easyinsight.analysis.*;
import com.easyinsight.benchmark.BenchmarkManager;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.email.UserStub;
import com.easyinsight.logging.LogClass;
import com.easyinsight.preferences.ApplicationSkin;
import com.easyinsight.preferences.ApplicationSkinSettings;
import com.easyinsight.scorecard.Scorecard;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.solutions.SolutionService;
import com.easyinsight.util.RandomTextGenerator;
import org.apache.jcs.JCS;
import org.hibernate.Session;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 1:24:52 PM
 */
public class DashboardService {

    private JCS dashboardCache = getCache("dashboards");

    private JCS getCache(String cacheName) {
        try {
            return JCS.getInstance(cacheName);
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

    private DashboardStorage dashboardStorage = new DashboardStorage();

    public String saveDashboardLink(long dashboardID, DashboardStackPositions dashboardStackPositions) {
        EIConnection conn = Database.instance().getConnection();
        try {
            long id = dashboardStackPositions.save(conn, dashboardID, 0);
            PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO DASHBOARD_LINK (dashboard_state_id, url_key) VALUES (?, ?)");
            saveStmt.setLong(1, id);
            String urlKey = RandomTextGenerator.generateText(40);
            saveStmt.setString(2, urlKey);
            saveStmt.execute();
            return urlKey;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public DashboardInfo retrieveFromDashboardLink(String urlKey) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DASHBOARD_STATE.dashboard_state_id, dashboard_id FROM DASHBOARD_LINK, DASHBOARD_STATE WHERE URL_KEY = ? AND " +
                    "DASHBOARD_LINK.dashboard_state_id = dashboard_state.dashboard_state_id");
            queryStmt.setString(1, urlKey);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long dashboardStateID = rs.getLong(1);
                DashboardStackPositions positions = new DashboardStackPositions();
                positions.retrieve(conn, dashboardStateID);
                DashboardInfo dashboardInfo = new DashboardInfo();
                dashboardInfo.setDashboardStackPositions(positions);
                dashboardInfo.setDashboardID(rs.getLong(2));
                return dashboardInfo;
            }
            return null;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public NewDashboardMetadata getDashboardEditorMetadata(long dataSourceID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            List<InsightDescriptor> reports = new AnalysisStorage().getInsightDescriptorsForDataSource(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), dataSourceID, conn);
            NewDashboardMetadata dashboardEditorMetadata = new NewDashboardMetadata();
            dashboardEditorMetadata.setAvailableReports(reports);
            Feed feed = FeedRegistry.instance().getFeed(dataSourceID, conn);
            dashboardEditorMetadata.setDataSourceInfo(feed.createSourceInfo(conn));
            return dashboardEditorMetadata;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public boolean isDashboardPublic(String urlKey) {
        boolean isPublic = false;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT public_visible FROM DASHBOARD WHERE URL_KEY = ?");
            stmt.setString(1, urlKey);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isPublic = rs.getBoolean(1);
            }
        } catch (SQLException se) {
            LogClass.error(se);
            throw new RuntimeException(se);
        } finally {
            Database.closeConnection(conn);
        }

        return isPublic;

    }

    public long canAccessDashboard(String urlKey) {
        try {
            long dashboardID = 0;
            EIConnection conn = Database.instance().getConnection();
            try {
                PreparedStatement stmt = conn.prepareStatement("SELECT DASHBOARD_ID FROM DASHBOARD WHERE URL_KEY = ?");
                stmt.setString(1, urlKey);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    dashboardID = rs.getLong(1);
                }
            } catch (SQLException se) {
                LogClass.error(se);
                throw new RuntimeException(se);
            } finally {
                Database.closeConnection(conn);
            }
            SecurityUtil.authorizeDashboard(dashboardID);
            return dashboardID;
        } catch (com.easyinsight.security.SecurityException e) {
            return 0;
        }
    }

    public List<DashboardDescriptor> getDashboards() {
        EIConnection conn = Database.instance().getConnection();
        try {
            boolean testAccountVisible = FeedService.testAccountVisible(conn);
            return dashboardStorage.getDashboards(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn, testAccountVisible).values();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void keepDashboard(long dashboardID, long sourceDashboardID) {
        SecurityUtil.authorizeDashboard(dashboardID);
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            Dashboard dashboard = getDashboard(dashboardID);
            Map<Long, AnalysisDefinition> reports = new HashMap<Long, AnalysisDefinition>();
            Map<Long, Dashboard> dashboards = new HashMap<Long, Dashboard>();
            SolutionService.recurseDashboard(reports, dashboards, dashboard, session, conn);

            for (AnalysisDefinition report : reports.values()) {
                report.setTemporaryReport(false);
                session.update(report);
            }
            session.flush();
            for (Dashboard tDashboard : dashboards.values()) {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE DASHBOARD SET TEMPORARY_DASHBOARD = ? WHERE dashboard_id = ?");
                updateStmt.setBoolean(1, false);
                updateStmt.setLong(2, tDashboard.getId());
                updateStmt.executeUpdate();
            }
            try {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT EXCHANGE_DASHBOARD_INSTALL_ID FROM EXCHANGE_DASHBOARD_INSTALL WHERE USER_ID = ? AND " +
                        "dashboard_id = ?");
                queryStmt.setLong(1, SecurityUtil.getUserID());
                queryStmt.setLong(2, dashboardID);
                ResultSet rs = queryStmt.executeQuery();
                if (rs.next()) {
                    long id = rs.getLong(1);
                    PreparedStatement updateTimeStmt = conn.prepareStatement("UPDATE EXCHANGE_DASHBOARD_INSTALL SET install_date = ? WHERE EXCHANGE_DASHBOARD_INSTALL_ID = ?");
                    updateTimeStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                    updateTimeStmt.setLong(2, id);
                    updateTimeStmt.executeUpdate();
                } else {
                    PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO EXCHANGE_DASHBOARD_INSTALL (USER_ID, DASHBOARD_ID, INSTALL_DATE) VALUES (?, ?, ?)");
                    insertStmt.setLong(1, SecurityUtil.getUserID());
                    insertStmt.setLong(2, sourceDashboardID);
                    insertStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    insertStmt.execute();
                }
            } catch (SQLException e) {
                LogClass.error("Error updating exchange info for dashboard " + sourceDashboardID, e);
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public Dashboard saveAs(Dashboard dashboard, String name) {
        SecurityUtil.authorizeDashboard(dashboard.getId());
        try {
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dashboard.getDataSourceID());
            UserStub userStub = new UserStub();
            userStub.setUserID(SecurityUtil.getUserID());
            dashboard.setAdministrators(Arrays.asList((FeedConsumer) userStub));

            Dashboard clonedDashboard = dashboard.cloneDashboard(new HashMap<Long, Scorecard>(), false, dataSource.getFields(), dataSource);
            clonedDashboard.setUrlKey(RandomTextGenerator.generateText(15));
            clonedDashboard.setCreationDate(new Date());
            clonedDashboard.setAuthorName(SecurityUtil.getUserName());
            clonedDashboard.setUpdateDate(new Date());
            clonedDashboard.setTemporary(false);
            clonedDashboard.setName(name);
            saveDashboard(clonedDashboard);
            return clonedDashboard;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public Dashboard saveDashboard(Dashboard dashboard) {
        if (dashboard.getId() > 0) {
            SecurityUtil.authorizeDashboard(dashboard.getId());
        }
        try {
            if (dashboard.getUrlKey() == null) {
                dashboard.setUrlKey(RandomTextGenerator.generateText(15));
                dashboard.setCreationDate(new Date());
                dashboard.setAuthorName(SecurityUtil.getUserName());
            }
            dashboard.setUpdateDate(new Date());
            if (dashboard.getAdministrators() == null || dashboard.getAdministrators().isEmpty()) {
                UserStub userStub = new UserStub();
                userStub.setUserID(SecurityUtil.getUserID());
                dashboard.setAdministrators(Arrays.asList((FeedConsumer) userStub));
            }
            dashboardStorage.saveDashboard(dashboard);
            String cacheKey = SecurityUtil.getUserID(false) + "-" + dashboard.getId();
            if (dashboardCache != null) {
                dashboardCache.remove(cacheKey);
            }

            Set<Long> reportIDs = dashboard.containedReports();
            EIConnection conn = Database.instance().getConnection();
            try {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT PUBLICLY_VISIBLE, ACCOUNT_VISIBLE FROM ANALYSIS WHERE ANALYSIS_ID = ?");
                boolean accessProblem = false;
                for (Long reportID : reportIDs) {
                    queryStmt.setLong(1, reportID);
                    ResultSet rs = queryStmt.executeQuery();
                    if (rs.next()) {
                        boolean publicVisible = rs.getBoolean(1);
                        boolean accountVisible = rs.getBoolean(2);
                        if (dashboard.isPublicVisible() && !publicVisible) {
                            accessProblem = true;
                        }
                        if (dashboard.isAccountVisible() && !accountVisible) {
                            accessProblem = true;
                        }
                    }
                }
                dashboard.setReportAccessProblem(accessProblem);
            } finally {
                Database.closeConnection(conn);
            }

            return dashboard;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void updateReportVisibility(long dashboardID) {
        SecurityUtil.authorizeDashboard(dashboardID);
        EIConnection conn = Database.instance().getConnection();
        try {
            Dashboard dashboard = dashboardStorage.getDashboard(dashboardID, conn);
            Set<Long> reportIDs = dashboard.containedReports();
            if (dashboard.isPublicVisible()) {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE ANALYSIS SET PUBLICLY_VISIBLE = ? WHERE ANALYSIS_ID = ?");
                for (long reportID : reportIDs) {
                    updateStmt.setBoolean(1, dashboard.isPublicVisible());
                    updateStmt.setLong(2, reportID);
                    updateStmt.executeUpdate();
                }
                updateStmt.close();
            }
            if (dashboard.isAccountVisible()) {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE ANALYSIS SET ACCOUNT_VISIBLE = ? WHERE ANALYSIS_ID = ?");
                for (long reportID : reportIDs) {
                    updateStmt.setBoolean(1, dashboard.isAccountVisible());
                    updateStmt.setLong(2, reportID);
                    updateStmt.executeUpdate();
                }
                updateStmt.close();
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void shareDashboard(long dashboardID) {
        SecurityUtil.authorizeDashboard(dashboardID);
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE DASHBOARD SET ACCOUNT_VISIBLE = ? WHERE DASHBOARD_ID = ?");
            updateStmt.setBoolean(1, true);
            updateStmt.setLong(2, dashboardID);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public ReportMetrics rateDashboard(long dashboardID, int rating) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT dashboard_user_rating_id FROM dashboard_user_rating WHERE " +
                    "user_id = ? AND dashboard_id = ?");
            queryStmt.setLong(1, SecurityUtil.getUserID());
            queryStmt.setLong(2, dashboardID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long id = rs.getLong(1);
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE dashboard_user_rating SET rating = ? WHERE dashboard_user_rating_id = ?");
                updateStmt.setInt(1, rating);
                updateStmt.setLong(2, id);
                updateStmt.executeUpdate();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO dashboard_user_rating (rating, user_id, dashboard_id) VALUES (?, ?, ?)");
                insertStmt.setInt(1, rating);
                insertStmt.setLong(2, SecurityUtil.getUserID());
                insertStmt.setLong(3, dashboardID);
                insertStmt.execute();
            }
            PreparedStatement ratingStmt = conn.prepareStatement("SELECT AVG(RATING), COUNT(RATING) FROM DASHBOARD_USER_RATING WHERE DASHBOARD_ID = ?");
            ratingStmt.setLong(1, dashboardID);
            ResultSet dashboardRS = ratingStmt.executeQuery();
            dashboardRS.next();
            double ratingAverage = dashboardRS.getDouble(1);
            int ratingCount = dashboardRS.getInt(2);
            return new ReportMetrics(ratingCount, ratingAverage, rating);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public Dashboard getDashboard(long dashboardID) {
        try {
            int role = SecurityUtil.authorizeDashboard(dashboardID);
            Dashboard dashboard = dashboardStorage.getDashboard(dashboardID);
            dashboard.setRole(role);
            return dashboard;
        } catch (Exception e) {
            LogClass.error("On retrieving dashboard " + dashboardID, e);
            throw new RuntimeException(e);
        }
    }

    public Dashboard getDashboardViewWithHeader(long dashboardID) {
        Dashboard dashboard = getDashboardView(dashboardID);
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT USER.USER_ID, USER.ACCOUNT_ID FROM USER_TO_DASHBOARD, USER WHERE USER_TO_DASHBOARD.DASHBOARD_ID = ? AND " +
                    "USER_TO_DASHBOARD.USER_ID = USER.USER_ID");
            stmt.setLong(1, dashboard.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                long userID = rs.getLong(1);
                long accountID = rs.getLong(2);
                ApplicationSkin skin = ApplicationSkinSettings.retrieveSkin(userID, session, accountID);
                dashboard.setHeaderImage(skin.getReportHeaderImage());
                dashboard.setHeaderBackgroundColor(skin.getReportBackgroundColor());
                dashboard.setHeaderTextColor(skin.getReportTextColor());
            }
            return dashboard;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException();
        } finally {
            session.close();
            Database.closeConnection(conn);
        }
    }

    public Dashboard getDashboardView(long dashboardID, @Nullable DashboardStackPositions dashboardStackPositions) {
        EIConnection conn = Database.instance().getConnection();
        try {
            return getDashboardView(dashboardID, dashboardStackPositions, conn);
        } catch (Exception e) {
            LogClass.error("On retrieving dashboard " + dashboardID, e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public Dashboard getDashboardView(long dashboardID, @Nullable DashboardStackPositions dashboardStackPositions, EIConnection conn) throws Exception {
        int role = SecurityUtil.authorizeDashboard(dashboardID);
        long startTime = System.currentTimeMillis();
        String cacheKey = SecurityUtil.getUserID(false) + "-" + dashboardID;
        /*if (dashboardCache != null) {
            Dashboard dashboard = (Dashboard) dashboardCache.get(cacheKey);
            if (dashboard != null) {
                return dashboard;
            }
        }*/
        Dashboard dashboard = dashboardStorage.getDashboard(dashboardID, conn);
        dashboard.setRole(role);
        Feed feed = FeedRegistry.instance().getFeed(dashboard.getDataSourceID(), conn);
        List<FilterDefinition> dlsFilters = DataService.addDLSFilters(dashboard.getDataSourceID(), conn);
        KeyDisplayMapper mapper = KeyDisplayMapper.create(feed.getFields());
        Map<String, List<AnalysisItem>> keyMap = mapper.getKeyMap();
        Map<String, List<AnalysisItem>> displayMap = mapper.getDisplayMap();
        if (dashboard.getMarmotScript() != null && !"".equals(dashboard.getMarmotScript().trim())) {
            StringTokenizer toker = new StringTokenizer(dashboard.getMarmotScript(), "\r\n");
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();
                new ReportCalculation(line).apply(dashboard, feed.getFields(), keyMap, displayMap, feed, conn, dlsFilters);
            }
        }
        dashboard.visit(new AnalysisItemFilterVisitor(feed, dlsFilters, conn));
        FilterVisitor filterVisitor = new FilterVisitor(dashboard.getDataSourceID(), dashboardID);
        dashboard.visit(filterVisitor);
        filterVisitor.done();
        if (dashboardStackPositions != null) {
            String key = "d";
            Map<String, FilterDefinition> filters = dashboardStackPositions.getFilterMap().get(key);
            // where do we actually perform the override?
            if (filters != null) {
                dashboard.setOverridenFilters(filters);
            }
            dashboard.visit(new StateVisitor(dashboardStackPositions));
        }
        if (dashboardCache != null) {
            dashboardCache.put(cacheKey, dashboard);
        }
        BenchmarkManager.recordBenchmarkForDashboard("DashboardView", System.currentTimeMillis() - startTime, SecurityUtil.getUserID(false), dashboardID);
        return dashboard;
    }

    public Dashboard getDashboardView(long dashboardID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            return getDashboardView(dashboardID, null, conn);
        } catch (Exception e) {
            LogClass.error("On retrieving dashboard " + dashboardID, e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private static class StateVisitor implements IDashboardVisitor {

        private DashboardStackPositions dashboardStackPositions;

        private StateVisitor(DashboardStackPositions dashboardStackPositions) {
            this.dashboardStackPositions = dashboardStackPositions;
        }

        public void accept(DashboardElement dashboardElement) {
            if (dashboardElement instanceof DashboardStack) {
                DashboardStack dashboardStack = (DashboardStack) dashboardElement;
                Integer position = dashboardStackPositions.getPositions().get(dashboardStack.getUrlKey());
                if (position != null) {
                    dashboardStack.setDefaultIndex(position);
                }
                // set default position to position
                String key = "s" + dashboardStack.getUrlKey();
                Map<String, FilterDefinition> filters = dashboardStackPositions.getFilterMap().get(key);
                if (filters != null) {
                    dashboardStack.setOverridenFilters(filters);
                }
            }
            if (dashboardElement instanceof DashboardReport) {
                DashboardReport dashboardReport = (DashboardReport) dashboardElement;
                // adjust filters, adjust reports...
                String key = "r" + dashboardReport.getUrlKey();
                Map<String, FilterDefinition> filters = dashboardStackPositions.getFilterMap().get(key);
                if (filters != null) {
                    dashboardReport.setOverridenFilters(filters);
                }
                InsightDescriptor report = dashboardStackPositions.getReports().get(dashboardReport.getUrlKey());
                dashboardReport.setReport(report);
            }
        }
    }

    private static class FilterVisitor implements IDashboardVisitor {

        private Map<AnalysisItem, List<FilterValueDefinition>> valueFilters = new HashMap<AnalysisItem, List<FilterValueDefinition>>();
        private Map<AnalysisItem, List<FlatDateFilter>> flatDateFilters = new HashMap<AnalysisItem, List<FlatDateFilter>>();

        private long dataSourceID;
        private long dashboardID;

        private FilterVisitor(long dataSourceID, long dashboardID) {
            this.dataSourceID = dataSourceID;
            this.dashboardID = dashboardID;
        }

        public void done() {
            for (Map.Entry<AnalysisItem, List<FilterValueDefinition>> entry : valueFilters.entrySet()) {
                AnalysisItemResultMetadata metadata = new DataService().getAnalysisItemMetadata(dataSourceID, entry.getKey(), 0, 0, dashboardID);
                for (FilterValueDefinition filterDefinition : entry.getValue()) {
                    filterDefinition.setCachedValues(metadata);
                }
            }
            for (Map.Entry<AnalysisItem, List<FlatDateFilter>> entry : flatDateFilters.entrySet()) {
                AnalysisDateDimensionResultMetadata metadata = (AnalysisDateDimensionResultMetadata) new DataService().getAnalysisItemMetadata(dataSourceID, entry.getKey(), 0, 0, dashboardID);
                metadata.setLatestDate(new Date());
                for (FlatDateFilter filterDefinition : entry.getValue()) {
                    filterDefinition.setCachedValues(metadata);
                }
            }
        }

        public void accept(DashboardElement dashboardElement) {
            if (dashboardElement instanceof DashboardStack) {
                DashboardStack dashboardStack = (DashboardStack) dashboardElement;
                for (FilterDefinition filter : dashboardStack.getFilters()) {
                    if (filter instanceof FilterValueDefinition) {
                        List<FilterValueDefinition> filters = valueFilters.get(filter.getField());
                        if (filters == null) {
                            filters = new ArrayList<FilterValueDefinition>();
                            valueFilters.put(filter.getField(), filters);
                        }
                        filters.add((FilterValueDefinition) filter);
                    } else if (filter instanceof FlatDateFilter) {
                        List<FlatDateFilter> filters = flatDateFilters.get(filter.getField());
                        if (filters == null) {
                            filters = new ArrayList<FlatDateFilter>();
                            flatDateFilters.put(filter.getField(), filters);
                        }
                        filters.add((FlatDateFilter) filter);
                    }
                }
            }
        }
    }

    private static class AnalysisItemFilterVisitor implements IDashboardVisitor {

        private Feed feed;
        private List<FilterDefinition> dlsFilters;
        private EIConnection conn;
        private Map<String, List<AnalysisItem>> keyMap;
        private Map<String, List<AnalysisItem>> displayMap;

        private AnalysisItemFilterVisitor(Feed feed, List<FilterDefinition> dlsFilters, EIConnection conn) throws SQLException {
            this.feed = feed;
            this.dlsFilters = dlsFilters;
            this.conn = conn;
            keyMap = new HashMap<String, List<AnalysisItem>>();
            displayMap = new HashMap<String, List<AnalysisItem>>();
            for (AnalysisItem analysisItem : feed.getFields()) {
                List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    keyMap.put(analysisItem.getKey().toKeyString(), items);
                }
                items.add(analysisItem);
            }


            for (AnalysisItem analysisItem : feed.getFields()) {
                List<AnalysisItem> items = displayMap.get(analysisItem.toDisplay());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    displayMap.put(analysisItem.toDisplay(), items);
                }
                items.add(analysisItem);
            }
        }

        public void accept(DashboardElement dashboardElement) {
            if (dashboardElement instanceof DashboardStack) {
                DashboardStack dashboardStack = (DashboardStack) dashboardElement;
                if (dashboardStack.getFilters() != null) {
                    for (FilterDefinition filterDefinition : dashboardStack.getFilters()) {
                        if (filterDefinition.getMarmotScript() != null && !"".equals(filterDefinition.getMarmotScript().trim())) {
                            StringTokenizer toker = new StringTokenizer(filterDefinition.getMarmotScript(), "\r\n");
                            while (toker.hasMoreTokens()) {
                                String line = toker.nextToken();
                                new ReportCalculation(line).apply(filterDefinition, feed.getFields(), keyMap, displayMap, feed, conn, dlsFilters);
                            }
                        }
                        if (feed.getDataSource().getMarmotScript() != null) {
                            StringTokenizer toker = new StringTokenizer(feed.getDataSource().getMarmotScript(), "\r\n");
                            while (toker.hasMoreTokens()) {
                                String line = toker.nextToken();
                                new ReportCalculation(line).apply(filterDefinition, feed.getFields(), keyMap, displayMap, feed, conn, dlsFilters);
                            }
                        }
                    }
                }
            }
        }
    }

    public void deleteDashboard(long dashboardID) {
        SecurityUtil.authorizeDashboard(dashboardID);
        try {
            dashboardStorage.deleteDashboard(dashboardID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
