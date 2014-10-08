package com.easyinsight.solutions;

import com.easyinsight.core.EIDescriptor;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.dashboard.DashboardStorage;
import com.easyinsight.datafeeds.*;
import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.exchange.ExchangeItem;
import com.easyinsight.export.*;
import com.easyinsight.logging.LogClass;
import com.easyinsight.preferences.ApplicationSkin;
import com.easyinsight.preferences.ApplicationSkinSettings;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.AuthorizationManager;
import com.easyinsight.security.AuthorizationRequirement;
import com.easyinsight.goals.*;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.users.Account;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.sql.*;
import java.io.ByteArrayInputStream;

import com.easyinsight.users.AccountStats;
import com.easyinsight.users.UserAccountAdminService;
import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 12:32:59 AM
 */
public class SolutionService {

    public List<FieldAssignment> determineFields(DashboardDescriptor dashboardDescriptor, long targetID) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dashboardDescriptor.getDataSourceID(), conn);
            List<AnalysisItem> items = new FeedService().allFields(targetID);
            Map<String, AnalysisItem> lookup = new HashMap<>();
            for (AnalysisItem field : items) {
                lookup.put(field.toDisplay(), field);
            }
            List<AnalysisItem> fields = InstallMetadata.findFieldsForMapping(dataSource, dashboardDescriptor, conn, session);
            Map<String, AnalysisItem> distincts = new HashMap<>();
            for (AnalysisItem field : fields) {
                distincts.put(field.toDisplay(), field);
            }
            fields = new ArrayList<>(distincts.values());
            Collections.sort(fields, (o1, o2) -> o1.toDisplay().compareTo(o2.toDisplay()));
            PreparedStatement stmt = conn.prepareStatement("SELECT source_field, target_field from copy_template_to_field_assignment, copy_template where " +
                    "copy_template_to_field_assignment.copy_template_id = copy_template.copy_template_id and " +
                    "copy_template.dashboard_id = ? AND copy_template.target_source_id = ?");
            stmt.setLong(1, dashboardDescriptor.getId());
            stmt.setLong(2, targetID);
            ResultSet rs = stmt.executeQuery();
            Map<String, String> endMap = new HashMap<>();
            while (rs.next()) {
                String sourceField = rs.getString(1);
                String targetField = rs.getString(2);
                AnalysisItem source = distincts.get(sourceField);
                if (source != null) {
                    endMap.put(sourceField, targetField);
                }
            }
            List<FieldAssignment> assignments = new ArrayList<>();
            for (AnalysisItem field : distincts.values()) {
                FieldAssignment fieldAssignment = new FieldAssignment();
                fieldAssignment.setSourceField(field);
                fieldAssignment.setTargetField(lookup.get(endMap.get(field.toDisplay())));
                assignments.add(fieldAssignment);
            }
            System.out.println(fields);
            return assignments;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            session.close();
            Database.closeConnection(conn);
        }
    }

    public void saveTemplate(DashboardDescriptor dashboardDescriptor, long targetSource, List<FieldAssignment> fieldAssignments) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM copy_template WHERE dashboard_id = ? AND target_source_id = ?");
            deleteStmt.setLong(1, dashboardDescriptor.getId());
            deleteStmt.setLong(2, targetSource);
            deleteStmt.executeUpdate();
            deleteStmt.close();
            PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO copy_template (dashboard_id, target_source_id) values (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            saveStmt.setLong(1, dashboardDescriptor.getId());
            saveStmt.setLong(2, targetSource);
            saveStmt.execute();
            long id = Database.instance().getAutoGenKey(saveStmt);
            saveStmt.close();
            PreparedStatement saveAssignmentStmt = conn.prepareStatement("INSERT INTO copy_template_to_field_assignment (copy_template_id, source_field, target_field) values (?, ?, ?)");
            for (FieldAssignment fieldAssignment : fieldAssignments) {
                if (fieldAssignment.getSourceField() != null && fieldAssignment.getTargetField() != null) {
                    saveAssignmentStmt.setLong(1, id);
                    saveAssignmentStmt.setString(2, fieldAssignment.getSourceField().toDisplay());
                    saveAssignmentStmt.setString(3, fieldAssignment.getTargetField().toDisplay());
                    saveAssignmentStmt.execute();
                }
            }
            saveAssignmentStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void installTemplate(DashboardDescriptor dashboardDescriptor, long targetSource, List<FieldAssignment> fieldAssignments, String targetName) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            FeedDefinition originalSource = new FeedStorage().getFeedDefinitionData(dashboardDescriptor.getDataSourceID(), conn);
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(targetSource, conn);
            Map<String, AnalysisItem> fieldAssignmentMap = new HashMap<>();

            for (FieldAssignment fieldAssignment : fieldAssignments) {
                fieldAssignmentMap.put(fieldAssignment.getSourceField().toDisplay(), fieldAssignment.getTargetField());
            }
            DashboardDescriptor copied = (DashboardDescriptor) InstallMetadata.installAsTemplate(originalSource,
                    dataSource, conn, session, Arrays.asList(dashboardDescriptor), fieldAssignmentMap);
            PreparedStatement nameStmt = conn.prepareStatement("UPDATE DASHBOARD SET DASHBOARD_NAME = ? WHERE DASHBOARD_ID = ?");
            nameStmt.setString(1, targetName);
            nameStmt.setLong(2, copied.getId());
            nameStmt.executeUpdate();
            nameStmt.close();
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

    public PostInstallSteps addKPIData(SolutionKPIData solutionKPIData) {
        PostInstallSteps postInstallSteps = new PostInstallSteps();
        SecurityUtil.authorizeFeedAccess(solutionKPIData.getDataSourceID());
        FeedDefinition dataSource;
        EIConnection conn = Database.instance().getConnection();
        try {
            System.out.println("Invoking ADD KPI data on " + solutionKPIData.getDataSourceID());
            conn.setAutoCommit(false);
            dataSource = new FeedStorage().getFeedDefinitionData(solutionKPIData.getDataSourceID(), conn);
            dataSource.setAccountVisible(true);
            new FeedStorage().updateDataFeedConfiguration(dataSource, conn);
            if (solutionKPIData.getActivity() != null) {
                new ExportService().addOrUpdateSchedule(solutionKPIData.getActivity(), solutionKPIData.getUtcOffset(), conn);
            }

            Set<Long> idSet = new HashSet<Long>();

            PreparedStatement analysisQueryStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, ANALYSIS.DATA_FEED_ID, ANALYSIS.TITLE FROM ANALYSIS, DATA_FEED " +
                    " WHERE ANALYSIS.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                    "DATA_FEED.feed_type = ? AND analysis.recommended_exchange = ?");
            List<EIDescriptor> toInstall = new ArrayList<EIDescriptor>();
            analysisQueryStmt.setInt(1, dataSource.getFeedType().getType());
            analysisQueryStmt.setBoolean(2, true);
            ResultSet rs = analysisQueryStmt.executeQuery();

            long masterSourceID = 0;
            while (rs.next()) {
                long reportID = rs.getLong(1);
                long dataSourceID = rs.getLong(2);
                idSet.add(dataSourceID);
                String name = rs.getString(3);
                masterSourceID = dataSourceID;
                InsightDescriptor report = new InsightDescriptor();
                report.setId(reportID);
                report.setName(name);
                toInstall.add(report);
            }

            PreparedStatement dashboardQueryStmt = conn.prepareStatement("SELECT DASHBOARD.DASHBOARD_ID, DASHBOARD.DASHBOARD_NAME, DASHBOARD.DATA_SOURCE_ID FROM DASHBOARD, DATA_FEED " +
                    " WHERE DASHBOARD.DATA_SOURCE_ID = DATA_FEED.DATA_FEED_ID AND " +
                    "DATA_FEED.feed_type = ? AND " +
                    "DASHBOARD.recommended_exchange = ?");
            dashboardQueryStmt.setInt(1, dataSource.getFeedType().getType());
            dashboardQueryStmt.setBoolean(2, true);
            ResultSet dashboardRS = dashboardQueryStmt.executeQuery();

            while (dashboardRS.next()) {
                long dashboardID = dashboardRS.getLong(1);
                String dashboardName = dashboardRS.getString(2);
                long dataSourceID = dashboardRS.getLong(3);
                idSet.add(dataSourceID);
                masterSourceID = dataSourceID;
                DashboardDescriptor dashboardDescriptor = new DashboardDescriptor();
                dashboardDescriptor.setName(dashboardName);
                dashboardDescriptor.setId(dashboardID);
                toInstall.add(dashboardDescriptor);
            }

            EIDescriptor result = null;
            if (masterSourceID > 0) {
                Session session = Database.instance().createSession(conn);
                try {
                    FeedDefinition source = new FeedStorage().getFeedDefinitionData(masterSourceID);
                    long targetID = solutionKPIData.getDataSourceID();
                    FeedDefinition target = new FeedStorage().getFeedDefinitionData(targetID);
                    result = InstallMetadata.install(source, target, conn, session, toInstall);
                } finally {
                    session.close();
                }
            }

            if (result != null && result.getType() == EIDescriptor.DASHBOARD) {
                PreparedStatement saveDashboardStmt = conn.prepareStatement("INSERT INTO ACCOUNT_TO_DASHBOARD (ACCOUNT_ID, DASHBOARD_ID) VALUES (?, ?)");
                saveDashboardStmt.setLong(1, SecurityUtil.getAccountID());
                saveDashboardStmt.setLong(2, result.getId());
                saveDashboardStmt.execute();
                saveDashboardStmt.close();
            }


            if (result != null && result.getType() == EIDescriptor.DASHBOARD) {
                postInstallSteps.setResult(result);
            }
            copyLookAndFeel(solutionKPIData, conn, postInstallSteps);



            conn.commit();

        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        /*if (postInstallSteps.getResult() != null && postInstallSteps.getResult().getType() == EIDescriptor.DASHBOARD) {
            new DashboardService().getDashboard(postInstallSteps.getResult().getId());
        }*/
        conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            new QuickReportDeliveryServlet.QuickReportResult(conn, solutionKPIData.getUtcOffset(), solutionKPIData.getDataSourceID(), dataSource.getFeedName()).invoke();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return postInstallSteps;
    }

    private void copyLookAndFeel(SolutionKPIData solutionKPIData, EIConnection conn, PostInstallSteps postInstallSteps) throws SQLException {
        Session session;PreparedStatement lfStmt = conn.prepareStatement("SELECT look_and_feel_customized FROM account WHERE account_id = ?");
        lfStmt.setLong(1, SecurityUtil.getAccountID());
        ResultSet lfRS = lfStmt.executeQuery();
        lfRS.next();

        boolean customized = lfRS.getBoolean(1);
        if (!customized) {
            PreparedStatement typeStmt = conn.prepareStatement("SELECT feed_type FROM data_feed WHERE data_feed_id = ?");
            typeStmt.setLong(1, solutionKPIData.getDataSourceID());
            ResultSet typeRS = typeStmt.executeQuery();
            typeRS.next();
            int connectionType = typeRS.getInt(1);
            ApplicationSkinSettings connectionSkin;
            session = Database.instance().createSession(conn);
            List results = session.createQuery("from ApplicationSkinSettings where connectionType = ?").setInteger(0, connectionType).list();
            if (results.size() > 0) {
                connectionSkin = (ApplicationSkinSettings) results.get(0);
                ApplicationSkinSettings globalSkin;
                results = session.createQuery("from ApplicationSkinSettings where globalSkin = ?").setBoolean(0, true).list();
                if (results.size() > 0) {
                    globalSkin = (ApplicationSkinSettings) results.get(0);
                } else {
                    globalSkin = new ApplicationSkinSettings();
                }
                ApplicationSkinSettings accountSkin;
                results = session.createQuery("from ApplicationSkinSettings where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list();
                if (results.size() > 0) {
                    accountSkin = (ApplicationSkinSettings) results.get(0);
                } else {
                    accountSkin = new ApplicationSkinSettings();
                }
                ApplicationSkinSettings settings = globalSkin.toSkin().toSettings(ApplicationSkin.APPLICATION).override(connectionSkin.toSkin().toSettings(ApplicationSkin.ACCOUNT));
                settings.setAccountID(SecurityUtil.getAccountID());
                session.save(settings);
                session.flush();
                ApplicationSkin applicationSkin = settings.override(accountSkin.toSkin().toSettings(ApplicationSkin.ACCOUNT)).toSkin();
                postInstallSteps.setApplicationSkin(applicationSkin);
            }
            session.close();
        }
    }

    public List<Long> getInstalledConnections() {
        EIConnection conn = Database.instance().getConnection();
        try {
            Set<Long> connectionIDs = new HashSet<Long>();
            boolean testAccountVisible = FeedService.testAccountVisible(conn);
            List<DataSourceDescriptor> dataSources = new FeedStorage().getDataSources(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn, testAccountVisible);
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
            PreparedStatement insertSolutionStmt = conn.prepareStatement("INSERT INTO SOLUTION (NAME, INDUSTRY, COPY_DATA, " +
                    "SOLUTION_TIER, CATEGORY, logo_link, data_source_type) VALUES (?, ?, ?, ?, ?, ?, ?)",
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

    public InstallationValidation connectionInstalled(long solutionID) {
        AccountStats stats = new UserAccountAdminService().getAccountStats();

        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT PRICING_MODEL FROM ACCOUNT WHERE ACCOUNT_ID = ?");
            stmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet pricingRS = stmt.executeQuery();
            pricingRS.next();
            int pricingModel = pricingRS.getInt(1);
            InstallationValidation installationValidation = new InstallationValidation();
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_SOURCE_TYPE FROM SOLUTION WHERE SOLUTION_ID = ?");
            queryStmt.setLong(1, solutionID);
            ResultSet solRS = queryStmt.executeQuery();
            solRS.next();
            int dataSourceType = solRS.getInt(1);
            if (pricingModel == Account.NEW) {
                // is this a connection requiring small biz?
                int connectionBillingType = new DataSourceTypeRegistry().billingInfoForType(new FeedType(dataSourceType));
                if (connectionBillingType == ConnectionBillingType.SMALL_BIZ) {
                    if (stats.getCoreSmallBizConnections() + stats.getAddonSmallBizConnections() <= stats.getCurrentSmallBizConnections()) {
                        installationValidation.setAtSizeLimit(true);
                    }
                } else if (connectionBillingType == ConnectionBillingType.QUICKBASE) {
                    // do we have quickbase connections available?
                    if (stats.isUnlimitedQuickbaseConnections()) {
                        // we're licensed for unlimited quickbase connections (should restrict by domain)
                    } else if (stats.getUsedQuickbaseConnections() >= stats.getAddonQuickbaseConnections()) {
                        installationValidation.setEnterpriseLimit(true);
                    }
                }
            }
            long existingID = 0;
            boolean testAccountVisible = FeedService.testAccountVisible(conn);
            List<DataSourceDescriptor> dataSources = new FeedStorage().getDataSources(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn, testAccountVisible);
            for (DataSourceDescriptor dataSource : dataSources) {
                if (dataSource.getDataSourceType() == dataSourceType) {
                    existingID = dataSource.getId();
                    break;
                }
            }
            installationValidation.setExistingConnectionID(existingID);
            return installationValidation;
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

            // look for existing installs, find any...

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

    private ConnectionInstallOptions determineDataSourceForDashboard(long dashboardID, EIConnection conn) throws SQLException {

        //

        PreparedStatement alreadyInstalledStmt = conn.prepareStatement("SELECT result_dashboard_id, data_feed.data_feed_id FROM dashboard, dashboard_install_info, data_feed, upload_policy_users, user " +
                "WHERE origin_dashboard_id = ? AND dashboard_install_info.data_source_id = data_feed.data_feed_id and data_feed.data_feed_id = upload_policy_users.feed_id AND " +
                "upload_policy_users.user_id = user.user_id and user.account_id = ? AND result_dashboard_id = dashboard.dashboard_id");
        alreadyInstalledStmt.setLong(1, dashboardID);
        alreadyInstalledStmt.setLong(2, SecurityUtil.getAccountID());

        ResultSet installRS = alreadyInstalledStmt.executeQuery();
        Map<Long, Set<EIDescriptor>> map = new HashMap<Long, Set<EIDescriptor>>();
        while (installRS.next()) {
            long existingID = installRS.getLong(1);
            long sourceID = installRS.getLong(2);
            Set<EIDescriptor> existingSet = map.get(sourceID);
            if (existingSet == null) {
                existingSet = new HashSet<EIDescriptor>();
                map.put(sourceID, existingSet);
            }
            existingSet.add(new DashboardDescriptor(null, existingID, null, 0, 0, null, false));
        }

        PreparedStatement dashboardQuery = conn.prepareStatement("SELECT DASHBOARD.data_source_id FROM DASHBOARD WHERE DASHBOARD_ID = ?");
        dashboardQuery.setLong(1, dashboardID);
        ResultSet dashboardRS = dashboardQuery.executeQuery();
        if (dashboardRS.next()) {
            long dataSourceID = dashboardRS.getLong(1);
            ConnectionInstallOptions connectionInstallOptions = determineDataSources(conn, dataSourceID);
            for (DataSourceDescriptor d : connectionInstallOptions.descriptors) {

                d.setPrebuilts(map.get(d.getId()));
            }
            return connectionInstallOptions;
        }
        return null;
    }

    private static class ConnectionInstallOptions {
        private List<DataSourceDescriptor> descriptors;
        private long connectionID;
        private String connectionName;

        private ConnectionInstallOptions(List<DataSourceDescriptor> descriptors, long connectionID, String connectionName) {
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
            PreparedStatement reportQueryStmt = conn.prepareStatement("SELECT ANALYSIS_ID, DATA_FEED_ID, TITLE, CREATE_DATE, AUTHOR_NAME, DESCRIPTION, RECOMMENDED_EXCHANGE " +
                    "FROM ANALYSIS WHERE ANALYSIS.solution_visible = ? AND ANALYSIS.url_key = ?");
            reportQueryStmt.setBoolean(1, true);
            reportQueryStmt.setString(2, urlKey);
            ConnectionInstallOptions connectionInstallOptions;
            ResultSet rs = reportQueryStmt.executeQuery();
            if (rs.next()) {
                long reportID = rs.getLong(1);
                long dataSourceID = rs.getLong(2);
                String reportName = rs.getString(3);
                Date dateCreated = new Date(rs.getTimestamp(4).getTime());
                String authorName = rs.getString(5);
                String description = rs.getString(6);
                boolean recommended = rs.getBoolean(7);
                EIDescriptor descriptor = new InsightDescriptor(reportID, null, dataSourceID, 0, urlKey, Roles.NONE, false);
                PreparedStatement ratingStmt = conn.prepareStatement("SELECT count(exchange_report_install_id) from " +
                    "exchange_report_install where report_id = ?");
                ratingStmt.setLong(1, reportID);
                ResultSet ratingRS = ratingStmt.executeQuery();
                ratingRS.next();
                int installs = ratingRS.getInt(1);
                connectionInstallOptions = determineDataSourceForReport(reportID, conn);
                srei = new ExchangeItem(reportName, reportID, installs, dateCreated, description,
                        authorName, descriptor, connectionInstallOptions.connectionID, connectionInstallOptions.connectionName, recommended, 0);
            } else {
                PreparedStatement dashboardQueryStmt = conn.prepareStatement("SELECT DASHBOARD_ID, URL_KEY, DASHBOARD_NAME, CREATION_DATE," +
                        "AUTHOR_NAME, DESCRIPTION, recommended_exchange FROM DASHBOARD WHERE DASHBOARD.exchange_visible = ? and " +
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
                    boolean recommended = dashboardRS.getBoolean(7);
                    EIDescriptor descriptor = new DashboardDescriptor(null, dashboardID, dashboardURLKey, 0, Roles.NONE, null, false);
                    connectionInstallOptions = determineDataSourceForDashboard(dashboardID, conn);
                    PreparedStatement ratingStmt = conn.prepareStatement("SELECT count(exchange_dashboard_install_id) FROM EXCHANGE_DASHBOARD_INSTALL WHERE DASHBOARD_ID = ?");
                    ratingStmt.setLong(1, dashboardID);
                    ResultSet ratingRS = ratingStmt.executeQuery();
                    ratingRS.next();
                    int installs = ratingRS.getInt(1);
                    srei = new ExchangeItem(dashboardName, dashboardID, installs, dateCreated, description,
                            authorName, descriptor, connectionInstallOptions.connectionID, connectionInstallOptions.connectionName, recommended, 0);
                } else {
                    return null;
                }
            }
            reportTemplateInfo.setExchangeData(srei);
            reportTemplateInfo.setDataSources(connectionInstallOptions.descriptors);
            return reportTemplateInfo;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private ConnectionInstallOptions determineDataSourceForReport(long reportID, EIConnection conn) throws SQLException {
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

    private ConnectionInstallOptions determineDataSources(EIConnection conn, long originalDataSourceID) throws SQLException {
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
        return new ConnectionInstallOptions(descriptors, connectionID, connectionName);
    }

    private void describe(List<DataSourceDescriptor> descriptors, EIConnection conn) throws SQLException {
        PreparedStatement queryReportCountStmt = conn.prepareStatement("SELECT COUNT(ANALYSIS_ID) FROM ANALYSIS WHERE " +
                "ANALYSIS.data_feed_id = ? AND ANALYSIS.temporary_report = ?");
        for (DataSourceDescriptor descriptor : descriptors) {
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

    public static final int FRESH_INSTALL = 1;
    public static final int UPGRADE_CHANGES = 2;

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
        SecurityUtil.authorizeFeedAccess(dataSourceID);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            Session session = Database.instance().createSession(conn);

            PreparedStatement getStmt = conn.prepareStatement("SELECT DASHBOARD_NAME, DATA_SOURCE_ID FROM DASHBOARD WHERE DASHBOARD_ID = ?");
            getStmt.setLong(1, dashboardID);
            ResultSet rs = getStmt.executeQuery();
            rs.next();
            String dashboardName = rs.getString(1);
            long sourceID = rs.getLong(2);
            getStmt.close();

            FeedDefinition sourceDataSource = new FeedStorage().getFeedDefinitionData(sourceID, conn);
            FeedDefinition targetDataSource = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);

            DashboardDescriptor dashboardDescriptor = new DashboardDescriptor();
            dashboardDescriptor.setId(dashboardID);
            dashboardDescriptor.setName(dashboardName);
            DashboardDescriptor result = (DashboardDescriptor) InstallMetadata.install(sourceDataSource, targetDataSource, conn, session, Arrays.asList((EIDescriptor) dashboardDescriptor));

            session.flush();

            conn.commit();
            session.close();
            return result;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }
    
    public static void recurseDashboard(Map<Long, AnalysisDefinition> reports, Map<Long, Dashboard> dashboards, Dashboard dashboard, Session session, EIConnection conn) throws Exception {
        if (!dashboards.containsKey(dashboard.getId())) {
            dashboards.put(dashboard.getId(), dashboard);
            Set<Long> reportIDs = dashboard.containedReports();
            for (Long reportID : reportIDs) {
                AnalysisDefinition report = new AnalysisStorage().getPersistableReport(reportID, session);
                recurseReport(reports, dashboards, report, session, conn);
            }
        }
    }

    public static void recurseReport(Map<Long, AnalysisDefinition> reports, Map<Long, Dashboard> dashboards, AnalysisDefinition report, Session session, EIConnection conn) throws Exception {
        if (!reports.containsKey(report.getAnalysisID())) {
            reports.put(report.getAnalysisID(), report);
            Set<EIDescriptor> containedReportIDs = report.containedReportIDs(session);
            for (EIDescriptor descriptor : containedReportIDs) {
                if (descriptor.getType() == EIDescriptor.REPORT) {
                    AnalysisDefinition child = new AnalysisStorage().getPersistableReport(descriptor.getId(), session);
                    recurseReport(reports, dashboards, child, session, conn);
                } else if (descriptor.getType() == EIDescriptor.DASHBOARD) {
                    Dashboard dashboard = new DashboardStorage().getDashboard(descriptor.getId(), conn);
                    recurseDashboard(reports, dashboards, dashboard, session, conn);
                }
            }
        }
    }

    public InsightDescriptor installReport(long reportID, long dataSourceID) {
        SecurityUtil.authorizeFeedAccess(dataSourceID);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);



            Session session = Database.instance().createSession(conn);
            PreparedStatement ps = conn.prepareStatement("SELECT TITLE, DATA_FEED_ID FROM ANALYSIS WHERE ANALYSIS_ID = ?");
            ps.setLong(1, reportID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String reportName = rs.getString(1);
            long sourceID = rs.getLong(2);
            FeedDefinition sourceDataSource = new FeedStorage().getFeedDefinitionData(sourceID, conn);
            FeedDefinition targetDataSource = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);

            InsightDescriptor insightDescriptor = new InsightDescriptor();
            insightDescriptor.setId(reportID);
            insightDescriptor.setName(reportName);
            InsightDescriptor result = (InsightDescriptor) InstallMetadata.install(sourceDataSource, targetDataSource, conn, session, Arrays.asList((EIDescriptor) insightDescriptor));
            conn.commit();
            session.close();
            return result;
        } catch (Exception e) {
            LogClass.error("Installing report " + reportID + " from exchange", e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public List<ExchangeItem> getSolutionReports() {
        List<ExchangeItem> reports = new ArrayList<ExchangeItem>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement analysisQueryStmt = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE, ANALYSIS.DATA_FEED_ID, ANALYSIS.REPORT_TYPE, " +
                    "analysis.create_date, ANALYSIS.DESCRIPTION, DATA_FEED.FEED_NAME, ANALYSIS.AUTHOR_NAME," +
                    "DATA_FEED.PUBLICLY_VISIBLE, SOLUTION.NAME, SOLUTION.SOLUTION_ID, ANALYSIS.url_key, analysis.recommended_exchange FROM DATA_FEED, SOLUTION, ANALYSIS " +
                    " WHERE ANALYSIS.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                    "DATA_FEED.feed_type = SOLUTION.data_source_type AND ANALYSIS.RECOMMENDED_EXCHANGE = ?");

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
                boolean recommended = analysisRS.getBoolean(13);
                getReportRatingStmt.setLong(1, analysisID);
                ResultSet ratingRS = getReportRatingStmt.executeQuery();
                ratingRS.next();
                int installs = ratingRS.getInt(1);
                InsightDescriptor insightDescriptor = new InsightDescriptor(analysisID, title, dataSourceID, reportType, urlKey, Roles.NONE, false);
                ExchangeItem item = new ExchangeItem(title, analysisID,
                        installs, created, description, authorName, insightDescriptor, connectionID, connectionName, recommended, 0);
                reports.add(item);
            }

            PreparedStatement alreadyInstalledStmt = conn.prepareStatement("SELECT result_dashboard_id FROM dashboard, dashboard_install_info, data_feed, upload_policy_users, user " +
                    "WHERE origin_dashboard_id = ? AND dashboard_install_info.data_source_id = data_feed.data_feed_id and data_feed.data_feed_id = upload_policy_users.feed_id AND " +
                    "upload_policy_users.user_id = user.user_id and user.account_id = ? AND result_dashboard_id = dashboard.dashboard_id");
            PreparedStatement dashboardQueryStmt = conn.prepareStatement("SELECT DASHBOARD.DASHBOARD_ID, DASHBOARD.DASHBOARD_NAME, " +
                    "SOLUTION.NAME, SOLUTION.SOLUTION_ID, dashboard.creation_date, dashboard.description," +
                    "dashboard.author_name, dashboard.url_key, dashboard.recommended_exchange FROM DATA_FEED, SOLUTION, dashboard " +
                    " WHERE dashboard.data_source_id = DATA_FEED.DATA_FEED_ID AND " +
                    "data_feed.feed_type = solution.data_source_type AND dashboard.recommended_exchange = ? " +
                    "AND dashboard.temporary_dashboard = ?");
            PreparedStatement getRatingDashboardStmt = conn.prepareStatement("SELECT count(exchange_dashboard_install_id) from " +
                    "exchange_dashboard_install where dashboard_id = ?");
            dashboardQueryStmt.setBoolean(1, true);
            dashboardQueryStmt.setBoolean(2, false);

            ResultSet dashboardRS = dashboardQueryStmt.executeQuery();
            while (dashboardRS.next()) {
                long dashboardID = dashboardRS.getLong(1);
                alreadyInstalledStmt.setLong(1, dashboardID);
                alreadyInstalledStmt.setLong(2, SecurityUtil.getAccountID());
                ResultSet alreadyRS = alreadyInstalledStmt.executeQuery();
                long existingID = 0;
                if (alreadyRS.next()) {

                    existingID = alreadyRS.getLong(1);
                }
                String dashboardName = dashboardRS.getString(2);
                String connectionName = dashboardRS.getString(3);
                long connectionID = dashboardRS.getLong(4);
                Date createdDate = new Date(dashboardRS.getTimestamp(5).getTime());
                String description = dashboardRS.getString(6);
                String authorName = dashboardRS.getString(7);
                String urlKey = dashboardRS.getString(8);
                boolean recommended = dashboardRS.getBoolean(9);



                getRatingDashboardStmt.setLong(1, dashboardID);
                ResultSet ratingRS = getRatingDashboardStmt.executeQuery();
                ratingRS.next();

                int installs = ratingRS.getInt(1);
                DashboardDescriptor dashboardDescriptor = new DashboardDescriptor(dashboardName, dashboardID, urlKey, 0, Roles.NONE, null, false);
                ExchangeItem item = new ExchangeItem(dashboardName, dashboardID, installs,
                        createdDate, description, authorName, dashboardDescriptor, connectionID, connectionName, recommended, existingID);
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

    public List<Solution> getExchangeConnections() {
        List<Solution> solutions = getSolutions();
        Set<Integer> exchangeTypes = new DataSourceTypeRegistry().getExchangeTypes();
        List<Solution> availableSolutions = new ArrayList<Solution>();
        for (Solution solution : solutions) {
            if (exchangeTypes.contains(solution.getDataSourceType())) {
                availableSolutions.add(solution);
            }
        }
        return availableSolutions;
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
