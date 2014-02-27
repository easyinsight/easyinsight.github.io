package com.easyinsight.solutions;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.dashboard.DashboardStorage;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.datafeeds.composite.CustomFieldTag;
import com.easyinsight.goals.InstallationSystem;
import com.easyinsight.scorecard.Scorecard;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.tag.Tag;
import org.hibernate.Session;

import java.sql.*;
import java.util.*;

/**
* User: jamesboe
* Date: 2/3/14
* Time: 5:47 PM
*/
class InstallMetadata {

    private FeedDefinition originalSource;
    private FeedDefinition targetSource;
    private EIConnection conn;
    private Session session;

    private Map<Long, Tag> tagReplacementMap;
    private boolean dataSourceChanged = false;

    // which reports have been installed...

    private Map<Long, AnalysisDefinition> installedReportMap = new HashMap<Long, AnalysisDefinition>();

    // which data sources have been installed...

    private Map<Long, Dashboard> installedDashboardMap = new HashMap<Long, Dashboard>();

    private List<AnalysisDefinition> newOrUpdatedReports = new ArrayList<AnalysisDefinition>();
    private List<AnalysisDefinition.SaveMetadata> newOrUpdatedMetadatas = new ArrayList<AnalysisDefinition.SaveMetadata>();
    private List<AnalysisDefinition> originReportList = new ArrayList<AnalysisDefinition>();
    private List<Dashboard> newOrUpdatedDashboards = new ArrayList<Dashboard>();
    private List<Dashboard> originDashboardList = new ArrayList<Dashboard>();
    private Map<Integer, Integer> folderReplacementMap = new HashMap<Integer, Integer>();

    private AnalysisStorage analysisStorage = new AnalysisStorage();
    private DashboardStorage dashboardStorage = new DashboardStorage();

    public InstallMetadata(FeedDefinition originalSource, FeedDefinition targetSource, EIConnection conn, Session session) {
        this.originalSource = originalSource;
        this.targetSource = targetSource;
        this.conn = conn;
        this.session = session;
    }

    private void populateFolderReplacements() throws SQLException {
        PreparedStatement getFolderStmt = conn.prepareStatement("SELECT REPORT_FOLDER_ID, FOLDER_NAME FROM REPORT_FOLDER WHERE DATA_SOURCE_ID = ?");
        PreparedStatement saveFolderStmt = conn.prepareStatement("INSERT INTO REPORT_FOLDER (ACCOUNT_ID, FOLDER_NAME, FOLDER_SEQUENCE, DATA_SOURCE_ID) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        PreparedStatement findFolderStmt = conn.prepareStatement("SELECT REPORT_FOLDER_ID FROM REPORT_FOLDER WHERE ACCOUNT_ID = ? AND FOLDER_NAME = ? AND DATA_SOURCE_ID = ?");

        getFolderStmt.setLong(1, originalSource.getDataFeedID());
        ResultSet rs = getFolderStmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt(1);
            String folderName = rs.getString(2);
            findFolderStmt.setLong(1, SecurityUtil.getAccountID());
            findFolderStmt.setString(2, folderName);
            findFolderStmt.setLong(3, targetSource.getDataFeedID());
            ResultSet existing = findFolderStmt.executeQuery();
            if (existing.next()) {
                int existingID = existing.getInt(1);
                folderReplacementMap.put(id, existingID);
            } else {

                saveFolderStmt.setLong(1, SecurityUtil.getAccountID());
                saveFolderStmt.setString(2, folderName);
                saveFolderStmt.setInt(3, 1);
                saveFolderStmt.setLong(4, targetSource.getDataFeedID());
                saveFolderStmt.execute();
                int savedID = (int) Database.instance().getAutoGenKey(saveFolderStmt);
                folderReplacementMap.put(id, savedID);
            }
        }
        findFolderStmt.close();
        saveFolderStmt.close();
        getFolderStmt.close();
    }

    private List<ReportValidation> validateReport(long reportID) throws Exception {
        List<ReportValidation> validations = new ArrayList<ReportValidation>();
        PreparedStatement ps = conn.prepareStatement("SELECT result_report_id FROM report_install_info WHERE origin_report_id = ? AND data_source_id = ?");
        ps.setLong(1, reportID);
        ps.setLong(2, targetSource.getDataFeedID());
        boolean exists = false;
        int existingVersion = 0;
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            long resultID = rs.getLong(1);
            log("\tUsing report from a previous install");
            exists = true;
            AnalysisDefinition report = analysisStorage.getPersistableReport(resultID, session);
            existingVersion = 1;
            Set<EIDescriptor> ids = report.containedReportIDs();
            for (EIDescriptor descriptor : ids) {
                if (descriptor.getType() == EIDescriptor.REPORT) {
                    validations.addAll(validateReport(descriptor.getId()));
                } else if (descriptor.getType() == EIDescriptor.DASHBOARD) {
                    validations.addAll(validateDashboard(descriptor.getId()));
                }
            }
            // find version...
        }
        ps.close();
        ReportValidation reportValidation = new ReportValidation();
        reportValidation.setExists(exists);
        reportValidation.setExistingVersion(existingVersion);
        validations.add(reportValidation);
        return validations;
    }

    private List<ReportValidation> validateDashboard(long reportID) throws Exception {
        List<ReportValidation> validations = new ArrayList<ReportValidation>();
        PreparedStatement ps = conn.prepareStatement("SELECT result_dashboard_id FROM dashboard_install_info WHERE origin_dashboard_id = ? AND data_source_id = ?");
        ps.setLong(1, reportID);
        ps.setLong(2, targetSource.getDataFeedID());
        boolean exists = false;
        int existingVersion = 0;
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            long resultID = rs.getLong(1);
            log("\tUsing report from a previous install");
            exists = true;
            Dashboard dashboard = dashboardStorage.getDashboard(resultID, conn);
            existingVersion = 1;
            Set<Long> ids = dashboard.containedReports();
            for (Long id : ids) {
                validations.addAll(validateReport(id));
            }
            // find version...
        }
        ps.close();
        ReportValidation reportValidation = new ReportValidation();
        reportValidation.setExists(exists);
        reportValidation.setExistingVersion(existingVersion);
        validations.add(reportValidation);
        return validations;
    }

    public static List<ReportValidation> determine(FeedDefinition originalSource, FeedDefinition targetDataSource, EIConnection conn, Session session, List<EIDescriptor> installing)
        throws Exception {
        InstallMetadata installMetadata = new InstallMetadata(originalSource, targetDataSource, conn, session);

        // any tags we'll have to copy?

        List<ReportValidation> validations = new ArrayList<ReportValidation>();

        List<InsightDescriptor> distinctAddons =  installMetadata.findDistinctCachedAddons();
        for (InsightDescriptor reportID : distinctAddons) {
            // does report exist, and if so, which version
            // is report part of data source
            validations.addAll(installMetadata.validateReport(reportID.getId()));
        }

        List<InsightDescriptor> dataSourceFieldReports = installMetadata.findDataSourceFieldReports();
        for (InsightDescriptor reportID : dataSourceFieldReports) {
            // does report exist, and if so, which version
            // is report a data source field report
            validations.addAll(installMetadata.validateReport(reportID.getId()));
        }

        for (EIDescriptor desc : installing) {
            if (desc.getType() == EIDescriptor.REPORT) {
                validations.addAll(installMetadata.validateReport(desc.getId()));
                // does report exist, and if so, is it the right version

                //installReport((InsightDescriptor) desc);
            } else if (desc.getType() == EIDescriptor.DASHBOARD) {

                // does dashboard exist, and if so, is it the right version
                validations.addAll(installMetadata.validateDashboard(desc.getId()));
                //installDashboard((DashboardDescriptor) desc);
            }
        }
        return validations;
    }

    public static EIDescriptor install(FeedDefinition originalSource, FeedDefinition targetDataSource, EIConnection conn, Session session, List<EIDescriptor> installing) throws Exception {

        InstallMetadata installMetadata = new InstallMetadata(originalSource, targetDataSource, conn, session);


        installMetadata.populateFolderReplacements();
        installMetadata.copyTags();
        installMetadata.copyCustomFieldTags();

        // change to graph structure...

        // copy any distinct sources, add them to the data source

        List<InsightDescriptor> distinctAddons = installMetadata.findDistinctCachedAddons();
        for (InsightDescriptor reportID : distinctAddons) {
            AnalysisDefinition report = installMetadata.installReport(reportID);
            installMetadata.addReportToDataSource(report, reportID.getName());
        }

        // copy any field data source reports

        List<InsightDescriptor> dataSourceFieldReports = installMetadata.findDataSourceFieldReports();
        for (InsightDescriptor reportID : dataSourceFieldReports) {
            installMetadata.installReport(reportID);
            // do we need to do anything here?
        }

        // rebuild the fields on the data source so that we have the proper full list
        installMetadata.dataSourceChange();
        installMetadata.copyAdditionalConnections();
        installMetadata.dataSourceChange();
        installMetadata.copyFieldTags();
        installMetadata.copyFieldRules();


        // copy all reports and dashboards

        installMetadata.installAll(installing);

        installMetadata.copyReportTags();
        installMetadata.copyDashboardTags();

        // update all reports and dashboards to point to appropriate copied tags

        installMetadata.updateAllMetadata();



        if (installing.size() == 1) {
            for (EIDescriptor install : installing) {
                if (install.getType() == EIDescriptor.REPORT) {
                    AnalysisDefinition report = installMetadata.installedReportMap.get(install.getId());
                    return new InsightDescriptor(report.getAnalysisID(), report.getTitle(), report.getDataFeedID(), report.getReportType(), report.getUrlKey(), Roles.OWNER, true);
                } else if (install.getType() == EIDescriptor.DASHBOARD) {
                    Dashboard dashboard = installMetadata.installedDashboardMap.get(install.getId());
                    return new DashboardDescriptor(dashboard.getName(), dashboard.getId(), dashboard.getUrlKey(), dashboard.getDataSourceID(), Roles.OWNER, null, true);
                }
                install.getId();
            }
        }
        return null;
    }

    private void log(String message) {
        System.out.println(message);
    }

    public AnalysisDefinition installReport(InsightDescriptor insightDescriptor) throws Exception {
        log("Installing " + insightDescriptor.getName());
        AnalysisDefinition report = installedReportMap.get(insightDescriptor.getId());
        if (report != null) {
            log("\tFound the report in the list already installed");
        }
        /*if (report == null && installMode != SolutionService.UPGRADE_CHANGES) {
            PreparedStatement ps = conn.prepareStatement("SELECT result_report_id FROM report_install_info WHERE origin_report_id = ? AND data_source_id = ?");
            ps.setLong(1, insightDescriptor.getId());
            ps.setLong(2, targetSource.getDataFeedID());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long resultID = rs.getLong(1);
                log("\tUsing report from a previous install");
                report = analysisStorage.getPersistableReport(resultID, session);
                installedReportMap.put(insightDescriptor.getId(), report);
            }
            ps.close();
        }*/
        if (report == null) {
            AnalysisDefinition fromReport = analysisStorage.getPersistableReport(insightDescriptor.getId(), session);
            log("\tInstalling a fresh version");
            AnalysisDefinition.SaveMetadata metadata = copyReportToDataSource(targetSource, fromReport);
            report = metadata.analysisDefinition;
            int folder = fromReport.getFolder();
            Integer folderID = folderReplacementMap.get(folder);
            if (folderID != null) {
                report.setFolder(folderID);
            }
            analysisStorage.saveAnalysis(report, session);
            installedReportMap.put(insightDescriptor.getId(), report);
            newOrUpdatedReports.add(report);
            newOrUpdatedMetadatas.add(metadata);
            originReportList.add(fromReport);
            Set<EIDescriptor> ids = fromReport.containedReportIDs();
            for (EIDescriptor descriptor : ids) {
                if (descriptor.getType() == EIDescriptor.REPORT) {
                    installReport((InsightDescriptor) descriptor);
                }
            }
        }

        return report;
    }

    public Dashboard installDashboard(DashboardDescriptor dashboardDescriptor) throws Exception {
        log("Installing " + dashboardDescriptor.getName());
        Dashboard dashboard = installedDashboardMap.get(dashboardDescriptor.getId());
        if (dashboard != null) {
            log("\tFound the dashboard in the list already installed");
        }
        /*if (dashboard == null) {
            PreparedStatement ps = conn.prepareStatement("SELECT result_dashboard_id FROM dashboard_install_info WHERE origin_dashboard_id = ? AND data_source_id = ?");
            ps.setLong(1, dashboardDescriptor.getId());
            ps.setLong(2, targetSource.getDataFeedID());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long resultID = rs.getLong(1);
                log("\tUsing dashboard from a previous install");
                dashboard = dashboardStorage.getDashboard(resultID, conn);
                installedDashboardMap.put(dashboardDescriptor.getId(), dashboard);
            }
            ps.close();
        }*/
        if (dashboard == null) {
            Dashboard fromDashboard = dashboardStorage.getDashboard(dashboardDescriptor.getId(), conn);
            log("\tInstalling a fresh version");
            dashboard = copyDashboardToDataSource(targetSource, fromDashboard);
            dashboardStorage.saveDashboard(dashboard, conn);
            installedDashboardMap.put(dashboardDescriptor.getId(), dashboard);
            newOrUpdatedDashboards.add(dashboard);
            originDashboardList.add(fromDashboard);
            Set<Long> ids = fromDashboard.containedReports();
            for (long id : ids) {
                InsightDescriptor report = new InsightDescriptor();
                report.setId(id);
                installReport(report);
            }
        }

        return dashboard;
    }

    private AnalysisDefinition.SaveMetadata copyReportToDataSource(FeedDefinition localDefinition, AnalysisDefinition report) throws CloneNotSupportedException {
        AnalysisDefinition.SaveMetadata metadata = report.clone(localDefinition.allFields(conn), true, tagReplacementMap);
        AnalysisDefinition clonedReport = metadata.analysisDefinition;
        clonedReport.setSolutionVisible(false);
        clonedReport.setRecommendedExchange(false);
        clonedReport.setAutoSetupDelivery(false);
        clonedReport.setDataFeedID(localDefinition.getDataFeedID());
        clonedReport.setUserBindings(Arrays.asList(new UserToAnalysisBinding(SecurityUtil.getUserID(), UserPermission.OWNER)));
        return metadata;
    }

    private Dashboard copyDashboardToDataSource(FeedDefinition localDefinition, Dashboard dashboard) throws CloneNotSupportedException {
        Dashboard clonedDashboard = dashboard.cloneDashboard(new HashMap<Long, Scorecard>(), true, localDefinition.allFields(conn), localDefinition);
        clonedDashboard.setExchangeVisible(false);
        clonedDashboard.setRecommendedExchange(false);
        clonedDashboard.setTemporary(false);
        clonedDashboard.setDataSourceID(localDefinition.getDataFeedID());
        return clonedDashboard;
    }



    public void addReportToDataSource(AnalysisDefinition report, String name) throws Exception {

        log("Adding " + report.getTitle() + " as a distinct source");

        if (!(targetSource instanceof CompositeFeedDefinition)) {
            return;
        }

        CompositeFeedDefinition compositeFeedDefinition = (CompositeFeedDefinition) targetSource;

        // is this data source already added to the data source as a distinct cached node?

        boolean alreadyInSource = false;

        PreparedStatement repStmt = conn.prepareStatement("SELECT REPORT_ID FROM distinct_cached_addon_report_source WHERE data_source_id = ?");
        for (CompositeFeedNode node : compositeFeedDefinition.getCompositeFeedNodes()) {
            if (node.getDataSourceType() == FeedType.DISTINCT_CACHED_ADDON.getType()) {
                repStmt.setLong(1, node.getDataFeedID());
                ResultSet rs = repStmt.executeQuery();
                rs.next();
                long reportID = rs.getLong(1);
                if (reportID == report.getAnalysisID()) {
                    // already exists
                    alreadyInSource = true;

                    break;
                }
            }
        }
        repStmt.close();

        if (!alreadyInSource) {
            log("\tNot yet in the data source, creating and adding");
            DistinctCachedSource source = (DistinctCachedSource) new InstallationSystem(conn).installConnection(FeedType.DISTINCT_CACHED_ADDON.getType());
            source.setVisible(false);
            source.setReportID(report.getAnalysisID());
            source.setFeedName(name);
            source.create(conn, null, null);
            CompositeFeedNode node = new CompositeFeedNode();
            node.setDataFeedID(source.getDataFeedID());
            node.setDataSourceType(FeedType.DISTINCT_CACHED_ADDON.getType());

            compositeFeedDefinition.getCompositeFeedNodes().add(node);
            dataSourceChanged = true;
        } else {
            log("\tFound in the data source, not adding");
        }
    }

    public List<InsightDescriptor> findDataSourceFieldReports() throws SQLException {
        log("Finding data source field reports...");
        List<InsightDescriptor> ids = new ArrayList<InsightDescriptor>();
        PreparedStatement dsFieldStmt = conn.prepareStatement("SELECT ANALYSIS_ID, TITLE FROM ANALYSIS WHERE DATA_FEED_ID = ? AND data_source_field_report = ?");
        dsFieldStmt.setLong(1, originalSource.getDataFeedID());
        dsFieldStmt.setBoolean(2, true);
        ResultSet rs = dsFieldStmt.executeQuery();
        while (rs.next()) {
            long reportID = rs.getLong(1);
            InsightDescriptor report = new InsightDescriptor();
            report.setId(reportID);
            report.setName(rs.getString(2));
            ids.add(report);
            // install report specified in report ID to the target data source
        }
        dsFieldStmt.close();
        return ids;
    }

    public void copyAdditionalConnections() throws SQLException {
        if (!(targetSource instanceof CompositeFeedDefinition)) {
            return;
        }
        CompositeServerDataSource source = (CompositeServerDataSource) originalSource;
        CompositeServerDataSource target = (CompositeServerDataSource) targetSource;

        Map<Long, AnalysisItem> sourceMap = new HashMap<Long, AnalysisItem>();
        for (AnalysisItem field : source.getFields()) {
            if (field.isConcrete()) {
                sourceMap.put(field.getKey().toBaseKey().getKeyID(), field);
            }
        }

        List<CompositeFeedConnection> addonConnections = source.getAddonConnections();
        List<CompositeFeedConnection> newConnections = new ArrayList<CompositeFeedConnection>();
        for (CompositeFeedConnection connection : addonConnections) {
            AnalysisItem sourceItem = connection.getSourceItem();
            AnalysisItem targetItem = connection.getTargetItem();

            AnalysisItem sourceResult = sourceMap.get(sourceItem.getKey().toBaseKey().getKeyID());
            AnalysisItem matchedSourceItem = target.findAnalysisItemByDisplayName(sourceResult.toDisplay());
            AnalysisItem targetResult = sourceMap.get(targetItem.getKey().toBaseKey().getKeyID());
            AnalysisItem matchedTargetItem = target.findAnalysisItemByDisplayName(targetResult.toDisplay());


            if (matchedSourceItem != null && matchedTargetItem != null) {
                CompositeFeedConnection newConnection = new CompositeFeedConnection();
                newConnection.setSourceFeedID(((DerivedKey) matchedSourceItem.getKey()).getFeedID());
                newConnection.setTargetFeedID(((DerivedKey) matchedTargetItem.getKey()).getFeedID());
                AnalysisItem s = new FeedStorage().getFeedDefinitionData(newConnection.getSourceFeedID(), conn).findAnalysisItemByKey(matchedSourceItem.getKey().toBaseKey().toKeyString());
                System.out.println("\tEnd field = " + s.toDisplay());
                newConnection.setSourceItem(s);
                AnalysisItem t = new FeedStorage().getFeedDefinitionData(newConnection.getTargetFeedID(), conn).findAnalysisItemByKey(matchedTargetItem.getKey().toBaseKey().toKeyString());
                System.out.println("\tEnd field = " + t.toDisplay());
                newConnection.setTargetItem(t);
                newConnections.add(newConnection);
            }
        }
        if (target.getAddonConnections() == null) {
            target.setAddonConnections(new ArrayList<CompositeFeedConnection>());
        }
        target.getAddonConnections().addAll(newConnections);
        dataSourceChanged = true;
    }

    public List<InsightDescriptor> findDistinctCachedAddons() throws SQLException {
        if (!(targetSource instanceof CompositeFeedDefinition)) {
            return new ArrayList<InsightDescriptor>();
        }
        List<InsightDescriptor> ids = new ArrayList<InsightDescriptor>();
        PreparedStatement dsFieldStmt = conn.prepareStatement("SELECT COMPOSITE_NODE.DATA_FEED_ID, distinct_cached_addon_report_source.report_id, data_feed.feed_name " +
                "FROM COMPOSITE_NODE, DATA_FEED, distinct_cached_addon_report_source, analysis, composite_feed WHERE " +
                "COMPOSITE_NODE.COMPOSITE_FEED_ID = COMPOSITE_FEED.COMPOSITE_FEED_ID AND COMPOSITE_FEED.DATA_FEED_ID = ? AND " +
                "COMPOSITE_NODE.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND DATA_FEED.FEED_TYPE = ? AND " +
                "DATA_FEED.DATA_FEED_ID = distinct_cached_addon_report_source.data_source_id AND distinct_cached_addon_report_source.report_id = analysis.analysis_id");
        dsFieldStmt.setLong(1, originalSource.getDataFeedID());
        dsFieldStmt.setInt(2, FeedType.DISTINCT_CACHED_ADDON.getType());
        ResultSet rs = dsFieldStmt.executeQuery();
        while (rs.next()) {
            //long dataSourceID = rs.getLong(1);
            long reportID = rs.getLong(2);
            String reportName = rs.getString(3);
            InsightDescriptor report = new InsightDescriptor();
            report.setName(reportName);
            report.setId(reportID);
            ids.add(report);
            // install report specified in report ID to the target data source
        }
        dsFieldStmt.close();
        return ids;
    }

    public void copyReportTags() throws SQLException {
        PreparedStatement reportTagStmt = conn.prepareStatement("SELECT TAG_NAME FROM " +
                "ACCOUNT_TAG, REPORT_TO_TAG WHERE ACCOUNT_TAG.ACCOUNT_TAG_ID = REPORT_TO_TAG.TAG_ID AND REPORT_TO_TAG.REPORT_ID = ?");
        for (Long reportID : installedReportMap.keySet()) {
            reportTagStmt.setLong(1, reportID);
            ResultSet targetRS = reportTagStmt.executeQuery();
            PreparedStatement findInAccountStmt = conn.prepareStatement("SELECT ACCOUNT_TAG_ID, REPORT_TAG FROM ACCOUNT_TAG WHERE TAG_NAME = ? AND ACCOUNT_ID = ?");
            while (targetRS.next()) {
                String tagName = targetRS.getString(1);

                findInAccountStmt.setString(1, tagName);
                findInAccountStmt.setLong(2, SecurityUtil.getAccountID());
                ResultSet rs = findInAccountStmt.executeQuery();
                long inOurAccountTagID;
                if (rs.next()) {
                    inOurAccountTagID = rs.getLong(1);
                    boolean reportTag = rs.getBoolean(2);
                    if (!reportTag) {
                        PreparedStatement updateStmt = conn.prepareStatement("UPDATE ACCOUNT_TAG SET REPORT_TAG = ? WHERE ACCOUNT_TAG_ID = ?");
                        updateStmt.setBoolean(1, true);
                        updateStmt.setLong(2, inOurAccountTagID);
                        updateStmt.executeUpdate();
                        updateStmt.close();
                    }
                } else {
                    PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO ACCOUNT_TAG (TAG_NAME, ACCOUNT_ID, DATA_SOURCE_TAG, REPORT_TAG, FIELD_TAG) VALUES (?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    saveStmt.setString(1, tagName);
                    saveStmt.setLong(2, SecurityUtil.getAccountID());
                    saveStmt.setBoolean(3, false);
                    saveStmt.setBoolean(4, true);
                    saveStmt.setBoolean(5, false);
                    saveStmt.execute();
                    inOurAccountTagID = Database.instance().getAutoGenKey(saveStmt);
                }

                PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO REPORT_TO_TAG (REPORT_ID, TAG_ID) VALUES (?, ?)");
                saveStmt.setLong(1, installedReportMap.get(reportID).getAnalysisID());
                saveStmt.setLong(2, inOurAccountTagID);
                saveStmt.execute();
                saveStmt.close();
            }
            findInAccountStmt.close();
        }
        reportTagStmt.close();
    }

    public void copyDashboardTags() throws SQLException {
        PreparedStatement reportTagStmt = conn.prepareStatement("SELECT TAG_NAME FROM " +
                "ACCOUNT_TAG, DASHBOARD_TO_TAG WHERE ACCOUNT_TAG.ACCOUNT_TAG_ID = DASHBOARD_TO_TAG.TAG_ID AND DASHBOARD_TO_TAG.DASHBOARD_ID = ?");
        for (Long reportID : installedDashboardMap.keySet()) {
            reportTagStmt.setLong(1, reportID);
            ResultSet targetRS = reportTagStmt.executeQuery();
            while (targetRS.next()) {
                String tagName = targetRS.getString(1);
                PreparedStatement findInAccountStmt = conn.prepareStatement("SELECT ACCOUNT_TAG_ID, REPORT_TAG FROM ACCOUNT_TAG WHERE TAG_NAME = ? AND ACCOUNT_ID = ?");
                findInAccountStmt.setString(1, tagName);
                findInAccountStmt.setLong(2, SecurityUtil.getAccountID());
                ResultSet rs = findInAccountStmt.executeQuery();
                long inOurAccountTagID;
                if (rs.next()) {
                    inOurAccountTagID = rs.getLong(1);
                    boolean reportTag = rs.getBoolean(2);
                    if (!reportTag) {
                        PreparedStatement updateStmt = conn.prepareStatement("UPDATE ACCOUNT_TAG SET REPORT_TAG = ? WHERE ACCOUNT_TAG_ID = ?");
                        updateStmt.setBoolean(1, true);
                        updateStmt.setLong(2, inOurAccountTagID);
                        updateStmt.executeUpdate();
                        updateStmt.close();
                    }
                } else {
                    PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO ACCOUNT_TAG (TAG_NAME, ACCOUNT_ID, DATA_SOURCE_TAG, REPORT_TAG, FIELD_TAG) VALUES (?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
                    saveStmt.setString(1, tagName);
                    saveStmt.setLong(2, SecurityUtil.getAccountID());
                    saveStmt.setBoolean(3, false);
                    saveStmt.setBoolean(4, true);
                    saveStmt.setBoolean(5, false);
                    saveStmt.execute();
                    inOurAccountTagID = Database.instance().getAutoGenKey(saveStmt);
                }
                PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO DASHBOARD_TO_TAG (DASHBOARD_ID, TAG_ID) VALUES (?, ?)");
                saveStmt.setLong(1, installedDashboardMap.get(reportID).getId());
                saveStmt.setLong(2, inOurAccountTagID);
                saveStmt.execute();
                saveStmt.close();
            }
        }

    }

    public void copyCustomFieldTags() throws SQLException {
        log("Copying custom field tags...");
        List<CustomFieldTag> customFieldTags = new FeedService().getCustomFieldTags(originalSource, conn);
        if (customFieldTags != null) {
            List<CustomFieldTag> copy = new ArrayList<CustomFieldTag>();
            for (CustomFieldTag customFieldTag : customFieldTags) {
                if (customFieldTag.getTagID() > 0) {
                    CustomFieldTag copyTag = new CustomFieldTag(customFieldTag.getType(), customFieldTag.getName());
                    Tag targetTag = tagReplacementMap.get(customFieldTag.getTagID());
                    copyTag.setTagID(targetTag.getId());
                    copy.add(copyTag);
                }
            }
            new FeedService().saveCustomFieldTags(copy, targetSource.getDataFeedID(), conn);
        }
    }

    public void copyTags() throws SQLException {
        log("Copying tags...");
        PreparedStatement findNeeded = conn.prepareStatement("SELECT ACCOUNT_TAG.ACCOUNT_TAG_ID, TAG_NAME, DATA_SOURCE_TAG, REPORT_TAG, FIELD_TAG " +
                "FROM ACCOUNT_TAG, FIELD_TO_TAG WHERE DATA_SOURCE_ID = ? AND FIELD_TO_TAG.ACCOUNT_TAG_ID = ACCOUNT_TAG.ACCOUNT_TAG_ID");
        findNeeded.setLong(1, originalSource.getDataFeedID());
        ResultSet targetRS = findNeeded.executeQuery();
        Set<Tag> tags = new HashSet<Tag>();
        while (targetRS.next()) {
            tags.add(new Tag(targetRS.getLong(1), targetRS.getString(2), targetRS.getBoolean(3), targetRS.getBoolean(4), targetRS.getBoolean(5)));
        }
        Map<Long, Tag> tagReplacementMap = new HashMap<Long, Tag>();
        for (Tag tag : tags) {
            log("\tLooking for " + tag.getName());
            PreparedStatement findInAccountStmt = conn.prepareStatement("SELECT ACCOUNT_TAG_ID FROM ACCOUNT_TAG WHERE TAG_NAME = ? AND ACCOUNT_ID = ?");
            findInAccountStmt.setString(1, tag.getName());
            findInAccountStmt.setLong(2, SecurityUtil.getAccountID());
            ResultSet inAccountRS = findInAccountStmt.executeQuery();
            long replaceID;
            if (inAccountRS.next()) {
                replaceID = inAccountRS.getLong(1);
                log("\t\tFound existing tag as " + replaceID);
            } else {
                PreparedStatement saveTagStmt = conn.prepareStatement("INSERT INTO ACCOUNT_TAG (TAG_NAME, ACCOUNT_ID, DATA_SOURCE_TAG, REPORT_TAG, FIELD_TAG) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                saveTagStmt.setString(1, tag.getName());
                saveTagStmt.setLong(2, SecurityUtil.getAccountID());
                saveTagStmt.setBoolean(3, tag.isDataSource());
                saveTagStmt.setBoolean(4, tag.isReport());
                saveTagStmt.setBoolean(5, tag.isField());
                saveTagStmt.execute();
                replaceID = Database.instance().getAutoGenKey(saveTagStmt);
                log("\t\tDidn't find, creating new");
                // also need to assign the appropriate tags here...
            }
            Tag replaceTag = new Tag();
            replaceTag.setName(tag.getName());
            replaceTag.setId(replaceID);
            tagReplacementMap.put(tag.getId(), replaceTag);
        }
        this.tagReplacementMap = tagReplacementMap;
    }

    public void copyFieldRules() throws Exception {
        log("Copying field rules...");
        ReplacementMap replacementMap = new ReplacementMap();
        for (AnalysisItem item : targetSource.getFields()) {
            replacementMap.addField(item, true);
        }
        List<FieldRule> rules = FieldRule.load(conn, originalSource.getDataFeedID());
        List<FieldRule> copiedRules = new ArrayList<FieldRule>();
        for (FieldRule fieldRule : rules) {
            FieldRule copiedRule = new FieldRule();
            copiedRule.setType(fieldRule.getType());
            copiedRule.setAll(fieldRule.isAll());
            copiedRules.add(copiedRule);
            if (fieldRule.getTag() != null) {
                Tag tag = fieldRule.getTag();
                Tag replaceTag = tagReplacementMap.get(tag.getId());
                copiedRule.setTag(replaceTag);
            }
            if (fieldRule.getExplicitField() != null) {
                AnalysisItemHandle handle = fieldRule.getExplicitField();
                AnalysisItemHandle copy = new AnalysisItemHandle();
                copy.setName(handle.getName());
                copiedRule.setExplicitField(copy);
            }
            if (fieldRule.getExtension() != null) {
                ReportFieldExtension ext = fieldRule.getExtension();
                ReportFieldExtension clone = ext.clone();
                clone.updateIDs(replacementMap);
                copiedRule.setExtension(clone);
            }
            if (fieldRule.getLink() != null) {
                Link link = fieldRule.getLink();
                Link clone = link.clone();
                if (link instanceof DrillThrough) {
                    DrillThrough drillThrough = (DrillThrough) link;
                    if (drillThrough.getReportID() != null && drillThrough.getReportID() > 0) {
                        InsightDescriptor report = new InsightDescriptor();
                        report.setId(drillThrough.getReportID());
                        installReport(report);
                    } else if (drillThrough.getDashboardID() != null && drillThrough.getDashboardID() > 0) {
                        DashboardDescriptor report = new DashboardDescriptor();
                        report.setId(drillThrough.getDashboardID());
                        installDashboard(report);
                    }
                }
                clone.updateReportIDs(installedReportMap, installedDashboardMap);
                copiedRule.setLink(clone);
            }
        }
        new FeedService().saveFieldRules(targetSource.getDataFeedID(), copiedRules, conn);
    }

    public void dataSourceChange() throws Exception {
        if (dataSourceChanged) {
            new DataSourceInternalService().updateFeedDefinition(targetSource, conn);
        }
    }

    public void done() throws SQLException {
        conn.commit();
    }

    private Set<Long> validChildSources() throws SQLException {
        Set<Long> valids = new HashSet<Long>();
        if (targetSource instanceof CompositeFeedDefinition) {
            CompositeFeedDefinition compositeOriginalSource = (CompositeFeedDefinition) targetSource;
            for (CompositeFeedNode node : compositeOriginalSource.getCompositeFeedNodes()) {
                valids.add(node.getDataFeedID());
            }
        }
        return valids;
    }

    private List<AnalysisItem> createTargetFields() throws SQLException {
        List<AnalysisItem> targetFields = targetSource.allFields(conn);
        if (targetSource instanceof CompositeFeedDefinition) {
            CompositeFeedDefinition compositeOriginalSource = (CompositeFeedDefinition) targetSource;
            PreparedStatement stmt = conn.prepareStatement("SELECT REPORT_ID FROM distinct_cached_addon_report_source WHERE DATA_SOURCE_ID = ?");
            Map<Long, CompositeFeedNode> nodeMap = new HashMap<Long, CompositeFeedNode>();
            Map<Long, Long> rMap = new HashMap<Long, Long>();
            for (CompositeFeedNode node : compositeOriginalSource.getCompositeFeedNodes()) {
                nodeMap.put(node.getDataFeedID(), node);
                if (node.getDataSourceType() == FeedType.DISTINCT_CACHED_ADDON.getType()) {
                    stmt.setLong(1, node.getDataFeedID());
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        long reportID = rs.getLong(1);
                        rMap.put(node.getDataFeedID(), reportID);
                    }
                }
            }

            stmt.close();
            for (AnalysisItem field : targetFields) {
                Key key = field.getKey();
                if (key instanceof DerivedKey) {
                    DerivedKey derivedKey = (DerivedKey) key;
                    CompositeFeedNode node = nodeMap.get(derivedKey.getFeedID());
                    if (node != null) {
                        if (node.getDataSourceType() == FeedType.DISTINCT_CACHED_ADDON.getType()) {
                            Long reportID = rMap.get(node.getDataFeedID());
                            if (reportID != null) {
                                FieldDataSourceOrigin origin = new FieldDataSourceOrigin();
                                origin.setReport(reportID);
                                field.setOrigin(origin);
                            }
                        }
                    }
                }
            }
        }
        return targetFields;
    }

    public void updateAllMetadata() throws SQLException, CloneNotSupportedException {
        // update tags, etc
        session.flush();

        //

        for (int i = 0; i < newOrUpdatedMetadatas.size(); i++) {
            List<AnalysisItem> targetFields = createTargetFields();
            AnalysisDefinition.SaveMetadata metadata = newOrUpdatedMetadatas.get(i);
            AnalysisDefinition original = originReportList.get(i);
            System.out.println("Updating metadata on " + original.getTitle());
            AnalysisDefinition.updateFromMetadata(targetSource, metadata.replacementMap, metadata.analysisDefinition, targetFields, metadata.added);
            metadata.analysisDefinition.updateReportIDs(installedReportMap, installedDashboardMap, session);
            analysisStorage.saveAnalysis(metadata.analysisDefinition, session);
        }

        Set<Long> valids = validChildSources();
        for (AnalysisDefinition report : newOrUpdatedReports) {
            report.populateValidationIDs(valids);
        }

        session.flush();


        for (Dashboard dashboard : newOrUpdatedDashboards) {
            List<AnalysisItem> targetFields = createTargetFields();
            dashboard.updateIDs(installedReportMap, targetFields, true, targetSource);
        }

        PreparedStatement originStmt = conn.prepareStatement("INSERT INTO report_install_info (origin_report_id, result_report_id, install_date, data_source_id) VALUES (?, ?, ?, ?)");
        for (int i = 0; i < newOrUpdatedReports.size(); i++) {
            AnalysisDefinition copiedReport = newOrUpdatedReports.get(i);
            AnalysisDefinition originReport = originReportList.get(i);
            new AnalysisStorage().saveAnalysis(copiedReport, session);
            originStmt.setLong(1, originReport.getAnalysisID());
            originStmt.setLong(2, copiedReport.getAnalysisID());
            originStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            originStmt.setLong(4, targetSource.getDataFeedID());
            originStmt.execute();
        }
        originStmt.close();

        PreparedStatement originDashboardStmt = conn.prepareStatement("INSERT INTO dashboard_install_info (origin_dashboard_id, result_dashboard_id, " +
                "install_date, data_source_id) VALUES (?, ?, ?, ?)");
        for (int i = 0; i < newOrUpdatedDashboards.size(); i++) {
            Dashboard copiedDashboard = newOrUpdatedDashboards.get(i);
            Dashboard originDashboard = originDashboardList.get(i);
            new DashboardStorage().saveDashboard(copiedDashboard, conn);
            originDashboardStmt.setLong(1, originDashboard.getId());
            originDashboardStmt.setLong(2, copiedDashboard.getId());
            originDashboardStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            originDashboardStmt.setLong(4, targetSource.getDataFeedID());
            originDashboardStmt.execute();
        }
        originDashboardStmt.close();
    }



    public List<EIDescriptor> installAll(List<EIDescriptor> installing) throws Exception {

        for (EIDescriptor desc : installing) {
            if (desc.getType() == EIDescriptor.REPORT) {
                installReport((InsightDescriptor) desc);
            } else if (desc.getType() == EIDescriptor.DASHBOARD) {
                installDashboard((DashboardDescriptor) desc);
            }
        }


        return new ArrayList<EIDescriptor>();
    }

    public void copyFieldTags() throws SQLException {
        log("Copying field tags...");
        List<AnalysisItemConfiguration> configs = new FeedService().getAnalysisItemConfigurations(originalSource.getDataFeedID(), conn, originalSource);
        List<AnalysisItemConfiguration> newConfigs = new ArrayList<AnalysisItemConfiguration>();

        List<AnalysisItem> targetFields = targetSource.allFields(conn);
        Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
        for (AnalysisItem item : targetFields) {
            map.put(item.toDisplay(), item);
        }
        for (AnalysisItemConfiguration config : configs) {
            List<Tag> configTags = config.getTags();
            AnalysisItem item = config.getAnalysisItem();
            AnalysisItem targetItem = map.get(item.toDisplay());
            if (targetItem != null) {
                List<Tag> newTags = new ArrayList<Tag>();
                for (Tag tag : configTags) {
                    Tag replaceTag = tagReplacementMap.get(tag.getId());
                    newTags.add(replaceTag);
                }
                AnalysisItemConfiguration newConfiguration = new AnalysisItemConfiguration();
                newConfiguration.setTags(newTags);
                newConfiguration.setAnalysisItem(targetItem);
                newConfigs.add(newConfiguration);
            }
        }
        new FeedService().updateAnalysisItemConfigurations(newConfigs, targetSource.getDataFeedID(), conn);
    }
}
