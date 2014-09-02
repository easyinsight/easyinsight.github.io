package com.easyinsight.dashboard;

import com.easyinsight.analysis.*;
import com.easyinsight.benchmark.BenchmarkManager;
import com.easyinsight.cache.MemCachedManager;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.email.UserStub;
import com.easyinsight.html.FilterUtils;
import com.easyinsight.logging.LogClass;
import com.easyinsight.preferences.ApplicationSkin;
import com.easyinsight.preferences.ApplicationSkinSettings;
import com.easyinsight.preferences.PreferencesService;
import com.easyinsight.scorecard.Scorecard;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.tag.Tag;
import com.easyinsight.util.RandomTextGenerator;
import org.hibernate.Session;
import org.jetbrains.annotations.Nullable;

import java.io.*;
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

    private void authorizeConfig(String urlKey, EIConnection conn) throws SQLException {
        PreparedStatement getStmt = conn.prepareStatement("SELECT report_id, dashboard_id FROM dashboard_state, saved_configuration WHERE " +
                "saved_configuration.dashboard_state_id = dashboard_state.dashboard_state_id AND saved_configuration.url_key = ?");
        getStmt.setString(1, urlKey);
        ResultSet rs = getStmt.executeQuery();
        rs.next();
        long reportID = rs.getLong(1);
        if (rs.wasNull()) {
            long dashboardID = rs.getLong(2);
            SecurityUtil.authorizeDashboard(dashboardID);
        } else {
            SecurityUtil.authorizeInsight(reportID);
        }
        getStmt.close();
    }

    public void deleteConfiguration(SavedConfiguration savedConfiguration) {
        EIConnection conn = Database.instance().getConnection();
        try {
            authorizeConfig(savedConfiguration.getUrlKey(), conn);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM saved_configuration WHERE saved_configuration_id = ?");
            stmt.setLong(1, savedConfiguration.getId());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public SavedConfiguration getConfiguration(String urlKey) {
        EIConnection conn = Database.instance().getConnection();
        try {
            authorizeConfig(urlKey, conn);
            PreparedStatement stmt = conn.prepareStatement("SELECT dashboard_state_id, saved_configuration_id, configuration_name FROM " +
                    "saved_configuration WHERE saved_configuration.url_key = ?");
            stmt.setString(1, urlKey);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            long stateID = rs.getLong(1);
            long configurationID = rs.getLong(2);
            String configurationName = rs.getString(3);
            DashboardStackPositions positions = new DashboardStackPositions();
            positions.retrieve(conn, stateID);
            stmt.close();
            SavedConfiguration configuration = new SavedConfiguration();
            configuration.setUrlKey(urlKey);
            configuration.setDashboardStackPositions(positions);
            configuration.setId(configurationID);
            configuration.setName(configurationName);
            return configuration;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public DashboardInfo getConfigurationForDashboard(String urlKey) {
        DashboardInfo dashboardInfo = new DashboardInfo();

        EIConnection conn = Database.instance().getConnection();
        try {
            authorizeConfig(urlKey, conn);
            PreparedStatement ps = conn.prepareStatement("SELECT saved_configuration.saved_configuration_id, saved_configuration.configuration_name," +
                    "saved_configuration.dashboard_state_id, dashboard_state.dashboard_id FROM " +
                    "saved_configuration, dashboard_state WHERE saved_configuration.url_key = ? AND saved_configuration.dashboard_state_id = dashboard_State.dashboard_state_id");
            ps.setString(1, urlKey);
            ResultSet rs = ps.executeQuery();
            rs.next();
            SavedConfiguration savedConfiguration = new SavedConfiguration();
            savedConfiguration.setUrlKey(urlKey);
            savedConfiguration.setId(rs.getLong(1));
            savedConfiguration.setName(rs.getString(2));
            long stateID = rs.getLong(3);
            long dashboardID = rs.getLong(4);
            dashboardInfo.setDashboardID(dashboardID);
            DashboardStackPositions dashboardStackPositions = new DashboardStackPositions();
            dashboardStackPositions.retrieve(conn, stateID);
            savedConfiguration.setDashboardStackPositions(dashboardStackPositions);
            dashboardInfo.setSavedConfiguration(savedConfiguration);
            return dashboardInfo;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public DashboardInfo getConfigurationForReport(String urlKey) {
        EIConnection conn = Database.instance().getConnection();
        try {
            return getConfigurationForReport(urlKey, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public DashboardInfo getConfigurationForReport(long configurationID, EIConnection conn) throws SQLException {
        DashboardInfo dashboardInfo = new DashboardInfo();
        PreparedStatement ps = conn.prepareStatement("SELECT saved_configuration.saved_configuration_id, saved_configuration.configuration_name, " +
                "saved_configuration.dashboard_state_id, dashboard_state.report_id, analysis.title, analysis.url_key, analysis.report_type, analysis.data_feed_id FROM " +
                "saved_configuration, dashboard_state, analysis WHERE saved_configuration.saved_configuration_id = ? AND saved_configuration.dashboard_state_id = dashboard_state.dashboard_state_id AND " +
                "dashboard_state.report_id = analysis.analysis_id");
        ps.setLong(1, configurationID);
        ResultSet rs = ps.executeQuery();
        rs.next();
        SavedConfiguration savedConfiguration = new SavedConfiguration();
        savedConfiguration.setId(rs.getLong(1));
        savedConfiguration.setName(rs.getString(2));
        long stateID = rs.getLong(3);
        long reportID = rs.getLong(4);
        String reportName = rs.getString(5);
        String reportURLKey = rs.getString(6);
        int reportType = rs.getInt(7);
        long dataSourceID = rs.getLong(8);
        dashboardInfo.setReport(new InsightDescriptor(reportID, reportName, dataSourceID, reportType, reportURLKey, 0, false));
        DashboardStackPositions positions = new DashboardStackPositions();
        positions.retrieve(conn, stateID);
        savedConfiguration.setDashboardStackPositions(positions);
        dashboardInfo.setSavedConfiguration(savedConfiguration);
        return dashboardInfo;
    }

    public DashboardInfo getConfigurationForReport(String urlKey, EIConnection conn) throws SQLException {
        DashboardInfo dashboardInfo = new DashboardInfo();
        PreparedStatement ps = conn.prepareStatement("SELECT saved_configuration.saved_configuration_id, saved_configuration.configuration_name, " +
                "saved_configuration.dashboard_state_id, dashboard_state.report_id, analysis.title, analysis.url_key, analysis.report_type, analysis.data_feed_id FROM " +
                "saved_configuration, dashboard_state, analysis WHERE saved_configuration.url_key = ? AND saved_configuration.dashboard_state_id = dashboard_state.dashboard_state_id AND " +
                "dashboard_state.report_id = analysis.analysis_id");
        ps.setString(1, urlKey);
        ResultSet rs = ps.executeQuery();
        rs.next();
        SavedConfiguration savedConfiguration = new SavedConfiguration();
        savedConfiguration.setId(rs.getLong(1));
        savedConfiguration.setName(rs.getString(2));
        savedConfiguration.setUrlKey(urlKey);
        long stateID = rs.getLong(3);
        long reportID = rs.getLong(4);
        String reportName = rs.getString(5);
        String reportURLKey = rs.getString(6);
        int reportType = rs.getInt(7);
        long dataSourceID = rs.getLong(8);
        dashboardInfo.setReport(new InsightDescriptor(reportID, reportName, dataSourceID, reportType, reportURLKey, 0, false));
        DashboardStackPositions positions = new DashboardStackPositions();
        positions.retrieve(conn, stateID);
        savedConfiguration.setDashboardStackPositions(positions);
        dashboardInfo.setSavedConfiguration(savedConfiguration);
        return dashboardInfo;
    }

    public SavedConfiguration saveConfigurationForDashboard(SavedConfiguration savedConfiguration, long dashboardID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            long id = savedConfiguration.getDashboardStackPositions().save(conn, dashboardID, 0);
            if (savedConfiguration.getId() == 0) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO saved_configuration (dashboard_state_id, configuration_name, url_key) values (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                stmt.setLong(1, id);
                stmt.setString(2, savedConfiguration.getName());
                String urlKey = RandomTextGenerator.generateText(30);
                savedConfiguration.setUrlKey(urlKey);
                stmt.setString(3, urlKey);
                stmt.execute();
                savedConfiguration.setId(Database.instance().getAutoGenKey(stmt));
                stmt.close();
            } else {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE saved_configuration SET dashboard_state_id = ?, configuration_name = ? WHERE saved_configuration_id = ?");
                updateStmt.setLong(1, id);
                updateStmt.setString(2, savedConfiguration.getName());
                updateStmt.setLong(3, savedConfiguration.getId());
                updateStmt.executeUpdate();
                updateStmt.close();
            }

            return savedConfiguration;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public SavedConfiguration saveConfigurationForReport(SavedConfiguration savedConfiguration, long reportID) {
        SecurityUtil.authorizeReport(reportID, Roles.SHARER);
        EIConnection conn = Database.instance().getConnection();
        try {
            long id = savedConfiguration.getDashboardStackPositions().save(conn, 0, reportID);
            if (savedConfiguration.getId() == 0) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO saved_configuration (dashboard_state_id, configuration_name, url_key) values (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                stmt.setLong(1, id);
                stmt.setString(2, savedConfiguration.getName());
                String urlKey = RandomTextGenerator.generateText(30);
                savedConfiguration.setUrlKey(urlKey);
                stmt.setString(3, urlKey);
                stmt.execute();
                savedConfiguration.setId(Database.instance().getAutoGenKey(stmt));
                stmt.close();
            } else {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE saved_configuration SET dashboard_state_id = ?, configuration_name = ? WHERE saved_configuration_id = ?");
                updateStmt.setLong(1, id);
                updateStmt.setString(2, savedConfiguration.getName());
                updateStmt.setLong(3, savedConfiguration.getId());
                updateStmt.executeUpdate();
                updateStmt.close();
            }

            return savedConfiguration;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void deleteConfigurations(List<SavedConfiguration> configurations) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM saved_configuration WHERE saved_configuration_id = ?");
            for (SavedConfiguration configuration : configurations) {
                authorizeConfig(configuration.getUrlKey(), conn);
                stmt.setLong(1, configuration.getId());
                stmt.executeUpdate();
            }
            stmt.close();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<SavedConfiguration> getConfigurationsForReport(long reportID) {
        SecurityUtil.authorizeInsight(reportID);
        EIConnection conn = Database.instance().getConnection();
        try {
            List<SavedConfiguration> savedConfigurations = new ArrayList<SavedConfiguration>();
            PreparedStatement queryStmt = conn.prepareStatement("SELECT saved_configuration_id, configuration_name, SAVED_CONFIGURATION.dashboard_state_id, saved_configuration.url_key FROM " +
                    "DASHBOARD_STATE, SAVED_CONFIGURATION WHERE DASHBOARD_STATE.report_id = ? AND DASHBOARD_STATE.DASHBOARD_STATE_ID = SAVED_CONFIGURATION.dashboard_state_id");
            queryStmt.setLong(1, reportID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long configurationID = rs.getLong(1);
                String configurationName = rs.getString(2);
                SavedConfiguration savedConfiguration = new SavedConfiguration();
                savedConfiguration.setName(configurationName);
                savedConfiguration.setId(configurationID);
                savedConfiguration.setUrlKey(rs.getString(4));
                savedConfigurations.add(savedConfiguration);
            }
            return savedConfigurations;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<SavedConfiguration> getConfigurationsForDashboard(long dashboardID) {
        SecurityUtil.authorizeDashboard(dashboardID);
        EIConnection conn = Database.instance().getConnection();
        try {
            List<SavedConfiguration> savedConfigurations = new ArrayList<SavedConfiguration>();
            PreparedStatement queryStmt = conn.prepareStatement("SELECT saved_configuration_id, configuration_name, SAVED_CONFIGURATION.dashboard_state_id, saved_configuration.url_key FROM " +
                    "DASHBOARD_STATE, SAVED_CONFIGURATION WHERE DASHBOARD_STATE.dashboard_id = ? AND DASHBOARD_STATE.DASHBOARD_STATE_ID = SAVED_CONFIGURATION.dashboard_state_id");
            queryStmt.setLong(1, dashboardID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long configurationID = rs.getLong(1);
                String configurationName = rs.getString(2);
                SavedConfiguration savedConfiguration = new SavedConfiguration();
                savedConfiguration.setName(configurationName);
                savedConfiguration.setId(configurationID);
                savedConfiguration.setUrlKey(rs.getString(4));
                savedConfigurations.add(savedConfiguration);
            }
            return savedConfigurations;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public String saveDashboardLink(long dashboardID, DashboardStackPositions dashboardStackPositions) {
        SecurityUtil.authorizeDashboard(dashboardID);
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
            authorizeConfig(urlKey, conn);
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
        SecurityUtil.authorizeFeedAccess(dataSourceID);
        EIConnection conn = Database.instance().getConnection();
        try {
            List<InsightDescriptor> reports = new AnalysisStorage().getInsightDescriptorsForDataSource(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), dataSourceID, conn, true);
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

    //TODO: ADD TAGS
    public DashboardDescriptor getDashboardDescriptor(String urlKey, EIConnection conn) {
        long dashboardID = canAccessDashboard(urlKey);
        if(dashboardID == 0)
            return null;
        DashboardDescriptor dd = new DashboardDescriptor();
        dd.setId(dashboardID);
        return dd;
    }

    public ReportResults getDashboardWithTags() {
        return getDashboardWithTags(new ArrayList<String>());
    }

    public ReportResults getDashboardWithTags(List<String> reqTags) {
        long userID = SecurityUtil.getUserID();
        EIConnection conn = Database.instance().getConnection();
        try {
            boolean testAccountVisible = FeedService.testAccountVisible(conn);
            List<DashboardDescriptor> dashboards = dashboardStorage.getDashboards(userID, SecurityUtil.getAccountID(), conn, testAccountVisible).values();
            List<DataSourceDescriptor> dataSources = new FeedStorage().getDataSources(userID, SecurityUtil.getAccountID(), conn, testAccountVisible);
            PreparedStatement getTagsStmt = conn.prepareStatement("SELECT ACCOUNT_TAG_ID, TAG_NAME, DATA_SOURCE_TAG, REPORT_TAG, FIELD_TAG FROM ACCOUNT_TAG WHERE ACCOUNT_ID = ?");

            getTagsStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet tagRS = getTagsStmt.executeQuery();
            Map<Long, Tag> tags = new HashMap<Long, Tag>();
            List<Tag> reportTags = new ArrayList<Tag>();
            while (tagRS.next()) {
                Tag tag = new Tag(tagRS.getLong(1), tagRS.getString(2), tagRS.getBoolean(3), tagRS.getBoolean(4), tagRS.getBoolean(5));
                if (tag.isReport()) {
                    reportTags.add(tag);
                }
                tags.put(tagRS.getLong(1), tag);
            }

            PreparedStatement getTagsToReportsStmt = conn.prepareStatement("SELECT dashboard_to_tag.TAG_ID, dashboard_id FROM dashboard_to_tag, account_tag WHERE " +
                    "account_tag.account_tag_id = dashboard_to_tag.tag_id and account_tag.account_id = ?");

            getTagsToReportsStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet dsTagRS = getTagsToReportsStmt.executeQuery();
            Map<Long, List<Tag>> reportToTagMap = new HashMap<Long, List<Tag>>();

            while (dsTagRS.next()) {
                long reportID = dsTagRS.getLong(2);
                long tagID = dsTagRS.getLong(1);
                Tag tag = tags.get(tagID);
                List<Tag> t = reportToTagMap.get(reportID);
                if (t == null) {
                    t = new ArrayList<Tag>();
                    reportToTagMap.put(reportID, t);
                }
                t.add(tag);
            }
            getTagsStmt.close();
            getTagsToReportsStmt.close();

            List<DashboardDescriptor> filtered = new ArrayList<DashboardDescriptor>();
            for (DashboardDescriptor dashboard : dashboards) {
                List<Tag> tagList = reportToTagMap.get(dashboard.getId());
                if (tagList == null) {
                    tagList = new ArrayList<Tag>();
                }
                dashboard.setTags(tagList);
                if(reqTags != null && reqTags.size() > 0) {
                    boolean found = false;
                    for(Tag t : dashboard.getTags()) {
                        if(reqTags.contains(t.getName())) {
                            found = true;
                        }
                    }
                    if(found)
                        filtered.add(dashboard);
                } else {
                    filtered.add(dashboard);
                }
            }
            Collections.sort(filtered, new Comparator<DashboardDescriptor>() {

                public int compare(DashboardDescriptor insightDescriptor, DashboardDescriptor insightDescriptor1) {
                    return insightDescriptor.getName().compareToIgnoreCase(insightDescriptor1.getName());
                }
            });
            ReportResults reportResults = new ReportResults();
            reportResults.setReportTags(reportTags);
            reportResults.setDashboards(dashboards);
            reportResults.setDataSources(dataSources);
            return reportResults;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
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

    public Dashboard saveAs(Dashboard dashboard, String name) {
        SecurityUtil.authorizeDashboard(dashboard.getId());
        SecurityUtil.authorizeFeedAccess(dashboard.getDataSourceID());
        try {
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dashboard.getDataSourceID());
            UserStub userStub = new UserStub();
            userStub.setUserID(SecurityUtil.getUserID());
            dashboard.setAdministrators(Arrays.asList((FeedConsumer) userStub));

            Dashboard clonedDashboard = dashboard.cloneDashboard(new HashMap<Long, Scorecard>(), false, dataSource.getFields(), dataSource).getDashboard();
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
            SecurityUtil.authorizeDashboard(dashboard.getId(), Roles.EDITOR);
        } else {
            SecurityUtil.authorizeFeedAccess(dashboard.getDataSourceID());
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

            /*if (dashboardCache != null) {
                dashboardCache.remove(cacheKey);
            }*/
            MemCachedManager.delete("dashboard" + dashboard.getId());

            Set<Long> reportIDs = dashboard.containedReports();
            EIConnection conn = Database.instance().getConnection();
            try {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT PUBLICLY_VISIBLE, ACCOUNT_VISIBLE, PUBLIC_WITH_KEY FROM ANALYSIS WHERE ANALYSIS_ID = ?");
                boolean accessProblem = false;
                for (Long reportID : reportIDs) {
                    queryStmt.setLong(1, reportID);
                    ResultSet rs = queryStmt.executeQuery();
                    if (rs.next()) {
                        boolean publicVisible = rs.getBoolean(1);
                        boolean accountVisible = rs.getBoolean(2);
                        boolean publicWithKey = rs.getBoolean(3);
                        if (dashboard.isPublicVisible() && !publicVisible) {
                            accessProblem = true;
                        }
                        if (dashboard.isAccountVisible() && !accountVisible) {
                            accessProblem = true;
                        }
                        if (dashboard.isPublicWithKey() && !publicWithKey) {
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
        SecurityUtil.authorizeDashboard(dashboardID, Roles.EDITOR);
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
            if (dashboard.isPublicWithKey()) {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE ANALYSIS SET PUBLIC_WITH_KEY = ? WHERE ANALYSIS_ID = ?");
                for (long reportID : reportIDs) {
                    updateStmt.setBoolean(1, dashboard.isPublicWithKey());
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

    public Map<FilterPositionKey, FilterDefinition> getFiltersForDashboard(String urlKey, Map<String, InsightDescriptor> reportList) {
        Dashboard d = getDashboardView(canAccessDashboard(urlKey), null);
        ListFiltersVisitor listFiltersVisitor = new ListFiltersVisitor(reportList);
        d.visit(listFiltersVisitor);
        Map<FilterPositionKey, FilterDefinition> list = listFiltersVisitor.getFilters();
        for(FilterDefinition f : FilterUtils.flattenFilters(d.getFilters())) {
            FilterPositionKey fk = new FilterPositionKey(FilterPositionKey.DASHBOARD, f.getFilterID(), null);
            list.put(fk, f);
        }
        return list;
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

        Dashboard dashboard = null;
        byte[] bytes = (byte[]) MemCachedManager.get("dashboard" + dashboardID);
        if (bytes != null) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                dashboard = (Dashboard) ois.readObject();
            } catch (Exception e) {
                LogClass.error(e);
            }
        }

        if (dashboard == null) {
            dashboard = dashboardStorage.getDashboard(dashboardID, conn);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(dashboard);
        oos.flush();
        MemCachedManager.add("dashboard" + dashboardID, 10000, baos.toByteArray());

        Session session = Database.instance().createSession(conn);
        try {
            ApplicationSkin applicationSkin;
            if (SecurityUtil.getAccountID(false) == 0) {
                UserStub user = (UserStub) dashboard.getAdministrators().get(0);
                applicationSkin = ApplicationSkinSettings.retrieveSkin(user.getUserID(), session, user.getAccountID());
            } else {
                PreparedStatement ps = conn.prepareStatement("SELECT EXCHANGE_AUTHOR FROM ACCOUNT WHERE ACCOUNT_ID = ?");
                ps.setLong(1, SecurityUtil.getAccountID());
                ResultSet rs = ps.executeQuery();
                rs.next();
                boolean exchangeAuthor = rs.getBoolean(1);
                ps.close();

                if (exchangeAuthor) {
                    PreparedStatement typeStmt = conn.prepareStatement("SELECT FEED_TYPE FROM DATA_FEED WHERE DATA_FEED_ID = ?");
                    typeStmt.setLong(1, dashboard.getDataSourceID());
                    ResultSet typeRS = typeStmt.executeQuery();
                    typeRS.next();
                    int dataSourceType = typeRS.getInt(1);
                    typeStmt.close();
                    applicationSkin = new PreferencesService().getConnectionSkin(dataSourceType);
                } else {
                    applicationSkin = ApplicationSkinSettings.retrieveSkin(SecurityUtil.getUserID(), session, SecurityUtil.getAccountID());
                }
            }
            if (applicationSkin != null) {
                if ("Primary".equals(dashboard.getColorSet())) {
                    if (applicationSkin.isDashboardStack1ColorStartEnabled()) {
                        dashboard.setStackFill1Start(applicationSkin.getDashboardStack1ColorStart());
                    }
                    if (applicationSkin.isDashboardStack1ColorStartEnabled()) {
                        dashboard.setStackFill1SEnd(applicationSkin.getDashboardStack1ColorEnd());
                    }
                    if (applicationSkin.isDashboardStack1ColorStartEnabled()) {
                        dashboard.setStackFill2Start(applicationSkin.getDashboardStackColor2Start());
                    }
                    if (applicationSkin.isDashboardStack1ColorStartEnabled()) {
                        dashboard.setStackFill2End(applicationSkin.getDashboardStackColor2End());
                    }
                }
                if ("Primary".equals(dashboard.getColorSet())) {
                    if (applicationSkin.isDashboardReportHeaderBackgroundColorEnabled()) {
                        dashboard.setReportHeaderBackgroundColor(applicationSkin.getDashboardReportHeaderBackgroundColor());
                    }
                    if (applicationSkin.isDashboardReportHeaderTextColorEnabled()) {
                        dashboard.setReportHeaderTextColor(applicationSkin.getDashboardReportHeaderTextColor());
                    }
                }
            }
        } finally {
            session.close();
        }



        dashboard.setRole(role);
        dashboard.setConfigurations(getConfigurationsForDashboard(dashboardID));
        Feed feed = FeedRegistry.instance().getFeed(dashboard.getDataSourceID(), conn);
        List<FilterDefinition> dlsFilters = DataService.addDLSFilters(dashboard.getDataSourceID(), conn);
        KeyDisplayMapper mapper = KeyDisplayMapper.create(feed.getFields());
        Map<String, List<AnalysisItem>> keyMap = mapper.getKeyMap();
        Map<String, List<AnalysisItem>> displayMap = mapper.getDisplayMap();
        Map<String, List<AnalysisItem>> unqualifiedDisplayMap = mapper.getUnqualifiedDisplayMap();
        if (dashboard.getMarmotScript() != null && !"".equals(dashboard.getMarmotScript().trim())) {
            StringTokenizer toker = new StringTokenizer(dashboard.getMarmotScript(), "\r\n");
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();
                new ReportCalculation(line).apply(dashboard, feed.getFields(), keyMap, displayMap, unqualifiedDisplayMap, feed, conn, dlsFilters);
            }
        }
        dashboard.visit(new AnalysisItemFilterVisitor(feed, dlsFilters, conn));
        FilterVisitor filterVisitor = new FilterVisitor(dashboard.getDataSourceID(), dashboardID);
        dashboard.visit(filterVisitor);
        filterVisitor.done();
        DashboardTextVisitor textVisitor = new DashboardTextVisitor();
        dashboard.visit(textVisitor);
        if (dashboardStackPositions != null) {
            Map<String, FilterDefinition> overriddenFilters = new HashMap<>();
            for (FilterDefinition filter : dashboard.getFilters()) {
                FilterPositionKey filterPositionKey = new FilterPositionKey(FilterPositionKey.DASHBOARD, filter.getFilterID(), null);
                FilterDefinition overriddenFilter = dashboardStackPositions.getFilterMap().get(filterPositionKey.createURLKey());
                if (overriddenFilter != null) {
                    overriddenFilters.put(String.valueOf(filter.getFilterID()), overriddenFilter);
                }
            }
            dashboard.setOverridenFilters(overriddenFilters);
            dashboard.visit(new StateVisitor(dashboardStackPositions));
        }
        dashboard.setDataSourceInfo(feed.createSourceInfo(conn));
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

    public boolean isDashboardPublicWithKey(String urlKey) {
        boolean isPublic = false;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT embed_with_key FROM DASHBOARD WHERE URL_KEY = ?");
            stmt.setString(1, urlKey);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isPublic = rs.getBoolean(1);
            }
            stmt.close();
        } catch (SQLException se) {
            LogClass.error(se);
            throw new RuntimeException(se);
        } finally {
            Database.closeConnection(conn);
        }

        return isPublic;
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
                Map<String, FilterDefinition> filters = new HashMap<String, FilterDefinition>();
                for (FilterDefinition filter : dashboardStack.getFilters()) {
                    FilterPositionKey filterPositionKey = new FilterPositionKey(FilterPositionKey.DASHBOARD_STACK, filter.getFilterID(), dashboardStack.getUrlKey());
                    FilterDefinition overriddenFilter = dashboardStackPositions.getFilterMap().get(filterPositionKey.createURLKey());
                    if (overriddenFilter != null) {
                        filters.put(String.valueOf(filterPositionKey.getFilterID()), overriddenFilter);
                    }
                }
                dashboardStack.setOverridenFilters(filters);
            }
            if (dashboardElement instanceof DashboardReport) {
                DashboardReport dashboardReport = (DashboardReport) dashboardElement;

                InsightDescriptor report = dashboardStackPositions.getReports().get(dashboardReport.getUrlKey());
                if (report != null) {
                    dashboardReport.setReport(report);
                }

                // adjust filters, adjust reports...
                WSAnalysisDefinition reportDefinition = new AnalysisStorage().getAnalysisDefinition(dashboardReport.getReport().getId());
                Map<String, FilterDefinition> filters = new HashMap<String, FilterDefinition>();
                for (FilterDefinition filter : reportDefinition.getFilterDefinitions()) {
                    FilterPositionKey filterPositionKey = new FilterPositionKey(FilterPositionKey.DASHBOARD_REPORT, filter.getFilterID(), dashboardReport.getUrlKey());
                    FilterDefinition overriddenFilter = dashboardStackPositions.getFilterMap().get(filterPositionKey.createURLKey());
                    if (overriddenFilter != null) {
                        filters.put(String.valueOf(filterPositionKey.getFilterID()), overriddenFilter);
                    }
                }
                dashboardReport.setOverridenFilters(filters);

            }
        }
    }

    private static class ListFiltersVisitor implements IDashboardVisitor {
        private Map<String, InsightDescriptor> reportList;

        private Map<FilterPositionKey, FilterDefinition> filters = new HashMap<FilterPositionKey, FilterDefinition>();

        private Map<FilterPositionKey, FilterDefinition> getFilters() {
            return filters;
        }

        private ListFiltersVisitor(Map<String, InsightDescriptor> reportList) {
            this.reportList = reportList;
        }

        public void accept(DashboardElement dashboardElement) {
            if(dashboardElement instanceof DashboardReport) {
                InsightDescriptor id = ((DashboardReport) dashboardElement).getReport();

                if(reportList.containsKey(dashboardElement.getUrlKey()) && !reportList.get(dashboardElement.getUrlKey()).getUrlKey().equals(id.getUrlKey())) {
                    id = reportList.get(dashboardElement.getUrlKey());
                }
                WSAnalysisDefinition ad = new AnalysisService().openAnalysisDefinition(id.getId());
                for(FilterDefinition f : FilterUtils.flattenFilters(ad.getFilterDefinitions())) {
                    FilterPositionKey fk = new FilterPositionKey(FilterPositionKey.DASHBOARD_REPORT, f.getFilterID(), dashboardElement.getUrlKey());
                    filters.put(fk, f);
                }
            }
            int scope = 0;
            if(dashboardElement instanceof DashboardStack) {
                scope = FilterPositionKey.DASHBOARD_STACK;
            } else if(dashboardElement instanceof DashboardReport) {
                scope = FilterPositionKey.DASHBOARD_REPORT;
            }
            for(FilterDefinition f : FilterUtils.flattenFilters(dashboardElement.getFilters())) {
                FilterPositionKey fk = new FilterPositionKey(scope, f.getFilterID(), dashboardElement.getUrlKey());
                filters.put(fk, f);
            }
        }
    }


    private static class FilterVisitor implements IDashboardVisitor {

        private Map<AnalysisItem, List<FlatDateFilter>> flatDateFilters = new HashMap<>();
        private Map<AnalysisItem, List<MultiFlatDateFilter>> multiDateFilters = new HashMap<>();

        private long dataSourceID;
        private long dashboardID;

        private FilterVisitor(long dataSourceID, long dashboardID) {
            this.dataSourceID = dataSourceID;
            this.dashboardID = dashboardID;
        }

        public void done() {
            /*for (Map.Entry<AnalysisItem, List<FilterValueDefinition>> entry : valueFilters.entrySet()) {
                AnalysisItemResultMetadata metadata = new DataService().getAnalysisItemMetadata(dataSourceID, entry.getKey(), 0, 0, dashboardID);
                for (FilterValueDefinition filterDefinition : entry.getValue()) {
                    filterDefinition.setCachedValues(metadata);
                }
            }*/
            for (Map.Entry<AnalysisItem, List<MultiFlatDateFilter>> entry : multiDateFilters.entrySet()) {
                for (MultiFlatDateFilter filter : entry.getValue()) {
                    filter.setCachedValues(new DataService().getMultiDateOptions(filter));
                }
            }
            for (Map.Entry<AnalysisItem, List<FlatDateFilter>> entry : flatDateFilters.entrySet()) {
                int startYearFound = 0;
                for (FlatDateFilter filterDefinition : entry.getValue()) {
                    if (filterDefinition.getStartYear() > 0) {
                        startYearFound = filterDefinition.getStartYear();
                    }
                }
                if (startYearFound > 0) {
                    System.out.println("loading with cache");
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, startYearFound);

                    AnalysisDateDimensionResultMetadata metadata = new AnalysisDateDimensionResultMetadata();
                    metadata.setEarliestDate(cal.getTime());
                    metadata.setLatestDate(new Date());

                    for (FlatDateFilter filterDefinition : entry.getValue()) {
                        filterDefinition.setCachedValues(metadata);
                    }
                } else {
                    System.out.println("loading without cache");
                    AnalysisDateDimensionResultMetadata metadata = (AnalysisDateDimensionResultMetadata) new DataService().getAnalysisItemMetadata(dataSourceID, entry.getKey(), 0, 0, dashboardID);
                    metadata.setLatestDate(new Date());
                    for (FlatDateFilter filterDefinition : entry.getValue()) {
                        filterDefinition.setCachedValues(metadata);
                    }
                }
            }
        }

        public void accept(DashboardElement dashboardElement) {
            if (dashboardElement instanceof DashboardStack) {
                DashboardStack dashboardStack = (DashboardStack) dashboardElement;
                for (FilterDefinition filter : dashboardStack.getFilters()) {
                    /*if (filter instanceof FilterValueDefinition) {
                        List<FilterValueDefinition> filters = valueFilters.get(filter.getField());
                        if (filters == null) {
                            filters = new ArrayList<FilterValueDefinition>();
                            valueFilters.put(filter.getField(), filters);
                        }
                        filters.add((FilterValueDefinition) filter);
                    } else*/
                    if (filter instanceof FlatDateFilter) {
                        List<FlatDateFilter> filters = flatDateFilters.get(filter.getField());
                        if (filters == null) {
                            filters = new ArrayList<>();
                            flatDateFilters.put(filter.getField(), filters);
                        }
                        filters.add((FlatDateFilter) filter);
                    } else if (filter instanceof MultiFlatDateFilter) {
                        List<MultiFlatDateFilter> mfFilters = multiDateFilters.get(filter.getField());
                        if (mfFilters == null) {
                            mfFilters = new ArrayList<>();
                            multiDateFilters.put(filter.getField(), mfFilters);
                        }
                        mfFilters.add((MultiFlatDateFilter) filter);
                    }
                }
            }
        }
    }

    private static class DashboardTextVisitor implements IDashboardVisitor {

        public void accept(DashboardElement dashboardElement) {
            if (dashboardElement instanceof DashboardText) {
                DashboardText dashboardText = (DashboardText) dashboardElement;
                dashboardText.setHtml(dashboardText.createHTML());
            }
        }
    }

    private static class AnalysisItemFilterVisitor implements IDashboardVisitor {

        private Feed feed;
        private List<FilterDefinition> dlsFilters;
        private EIConnection conn;
        private Map<String, List<AnalysisItem>> keyMap;
        private Map<String, List<AnalysisItem>> displayMap;
        private Map<String, List<AnalysisItem>> unqualifiedDisplayMap;

        private AnalysisItemFilterVisitor(Feed feed, List<FilterDefinition> dlsFilters, EIConnection conn) throws SQLException {
            this.feed = feed;
            this.dlsFilters = dlsFilters;
            this.conn = conn;
            keyMap = new HashMap<>();
            displayMap = new HashMap<>();
            unqualifiedDisplayMap = new HashMap<>();
            for (AnalysisItem analysisItem : feed.getFields()) {
                List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
                if (items == null) {
                    items = new ArrayList<>(1);
                    keyMap.put(analysisItem.getKey().toKeyString(), items);
                }
                items.add(analysisItem);
            }


            for (AnalysisItem analysisItem : feed.getFields()) {
                List<AnalysisItem> items = displayMap.get(analysisItem.toDisplay());
                if (items == null) {
                    items = new ArrayList<>(1);
                    displayMap.put(analysisItem.toDisplay(), items);
                }
                items.add(analysisItem);
            }

            for (AnalysisItem analysisItem : feed.getFields()) {
                List<AnalysisItem> items = unqualifiedDisplayMap.get(analysisItem.toUnqualifiedDisplay());
                if (items == null) {
                    items = new ArrayList<>(1);
                    unqualifiedDisplayMap.put(analysisItem.toUnqualifiedDisplay(), items);
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
                                new ReportCalculation(line).apply(filterDefinition, feed.getFields(), keyMap, displayMap, unqualifiedDisplayMap, feed, conn, dlsFilters);
                            }
                        }
                        if (feed.getDataSource().getMarmotScript() != null) {
                            StringTokenizer toker = new StringTokenizer(feed.getDataSource().getMarmotScript(), "\r\n");
                            while (toker.hasMoreTokens()) {
                                String line = toker.nextToken();
                                new ReportCalculation(line).apply(filterDefinition, feed.getFields(), keyMap, displayMap, unqualifiedDisplayMap, feed, conn, dlsFilters);
                            }
                        }
                    }
                }
            }
        }
    }

    public void deleteDashboard(long dashboardID) {
        SecurityUtil.authorizeDashboard(dashboardID, Roles.OWNER);
        try {
            MemCachedManager.delete("dashboard" + dashboardID);
            dashboardStorage.deleteDashboard(dashboardID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
