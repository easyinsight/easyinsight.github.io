package com.easyinsight.dashboard;

import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.email.UserStub;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.solutions.SolutionService;
import com.easyinsight.util.RandomTextGenerator;
import org.hibernate.Session;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 1:24:52 PM
 */
public class DashboardService {

    private DashboardStorage dashboardStorage = new DashboardStorage();

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
            return dashboardStorage.getDashboards(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn).values();
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
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE DASHBOARD SET TEMPORARY_DASHBOARD = ? where dashboard_id = ?");
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
            return dashboard;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void shareDashboard(long dashboardID) {
        SecurityUtil.authorizeDashboard(dashboardID);
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE DASHBOARD SET ACCOUNT_VISIBLE = ? WHERE DASHBOARD_ID = ?");
            updateStmt.setBoolean(1, false);
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
            PreparedStatement queryStmt = conn.prepareStatement("SELECT dashboard_user_rating_id from dashboard_user_rating where " +
                    "user_id = ? and dashboard_id = ?");
            queryStmt.setLong(1, SecurityUtil.getUserID());
            queryStmt.setLong(2, dashboardID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long id = rs.getLong(1);
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE dashboard_user_rating set rating = ? where dashboard_user_rating_id = ?");
                updateStmt.setInt(1, rating);
                updateStmt.setLong(2, id);
                updateStmt.executeUpdate();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO dashboard_user_rating (rating, user_id, dashboard_id) values (?, ?, ?)");
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

    public Dashboard getDashboardView(long dashboardID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            int role = SecurityUtil.authorizeDashboard(dashboardID);
            Dashboard dashboard = dashboardStorage.getDashboard(dashboardID, conn);
            dashboard.setRole(role);
            Feed feed = FeedRegistry.instance().getFeed(dashboard.getDataSourceID(), conn);
            List<FilterDefinition> dlsFilters = DataService.addDLSFilters(dashboard.getDataSourceID(), conn);
            Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
            Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
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
            return dashboard;
        } catch (Exception e) {
            LogClass.error("On retrieving dashboard " + dashboardID, e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
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
