package com.easyinsight.datafeeds;

import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.solutions.SolutionService;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.userupload.UserUploadInternalService;
import com.easyinsight.database.Database;
import com.easyinsight.solutions.SolutionInstallInfo;
import com.easyinsight.security.Roles;
import com.easyinsight.core.DataSourceDescriptor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import org.hibernate.Session;
import org.jetbrains.annotations.Nullable;

/**
 * User: James Boe
 * Date: May 27, 2009
 * Time: 4:25:09 PM
 */
public class DataSourceCopyUtils {

    @Nullable
    public static Map<Long, SolutionInstallInfo> installFeed(long userID, Connection conn, boolean copyData,
                                                        FeedDefinition feedDefinition, String newDataSourceName,
                                                        long solutionID, long accountID, String userName, Map<Long, SolutionInstallInfo> existingInfos) throws Exception {
        if (existingInfos.containsKey(feedDefinition.getDataFeedID())) {
            return null;
        }
        FeedStorage feedStorage = new FeedStorage();
        Map<Long, SolutionInstallInfo> infos;

        // result here needs to have the core keys
        // 

        infos = cloneFeed(userID, conn, feedDefinition, solutionID > 0, accountID, userName);
        FeedDefinition clonedFeedDefinition = infos.get(feedDefinition.getDataFeedID()).getNewDataSource();
        if (solutionID > 0 && feedDefinition.requiresConfiguration()) {
            clonedFeedDefinition.setVisible(false);
        }
        clonedFeedDefinition.setDateCreated(new Date());
        clonedFeedDefinition.setDateUpdated(new Date());
        clonedFeedDefinition.setLastRefreshStart(new Date(1));
        if (newDataSourceName != null) {
            clonedFeedDefinition.setFeedName(newDataSourceName);
        }
        feedStorage.updateDataFeedConfiguration(clonedFeedDefinition, conn);
        new UserUploadInternalService().createUserFeedLink(userID, clonedFeedDefinition.getDataFeedID(), Roles.OWNER, conn);
        if ((feedDefinition.getDataSourceType() == DataSourceInfo.STORED_PUSH || feedDefinition.getDataSourceType() == DataSourceInfo.STORED_PULL)) {
            buildClonedDataStores(copyData, feedDefinition, clonedFeedDefinition, conn);
        } else {
            DataStorage.liveDataSource(clonedFeedDefinition.getDataFeedID(), conn, clonedFeedDefinition.getFeedType().getType());
        }
        clonedFeedDefinition.postClone(conn);
        PreparedStatement getReports = conn.prepareStatement("SELECT ANALYSIS_ID FROM ANALYSIS WHERE DATA_FEED_ID = ? AND TEMPORARY_REPORT = ?");
        getReports.setLong(1, feedDefinition.getDataFeedID());
        getReports.setBoolean(2, false);
        ResultSet reportRS = getReports.executeQuery();
        Map<Long, AnalysisDefinition> alreadyInstalledMap = new HashMap<Long, AnalysisDefinition>();
        Session session = Database.instance().createSession(conn);
        while (reportRS.next()) {
            long reportID = reportRS.getLong(1);
            try {
                new SolutionService().installReport(reportID, clonedFeedDefinition.getDataFeedID(), (EIConnection) conn, session, false, true, alreadyInstalledMap);
            } catch (Exception e) {
                LogClass.error("Could not install report " + reportID, e);
            }
        }
        PreparedStatement getDashboards = conn.prepareStatement("SELECT DASHBOARD_ID, FOLDER FROM DASHBOARD WHERE DATA_SOURCE_ID = ? AND TEMPORARY_DASHBOARD = ?");
        getDashboards.setLong(1, feedDefinition.getDataFeedID());
        getDashboards.setBoolean(2, false);
        ResultSet dashboardRS = getDashboards.executeQuery();
        while (dashboardRS.next()) {
            long reportID = dashboardRS.getLong(1);
            int folder = dashboardRS.getInt(2);
            try {
                new SolutionService().installDashboard(reportID, clonedFeedDefinition.getDataFeedID(), (EIConnection) conn, session, false, true, alreadyInstalledMap, folder);
            } catch (Exception e) {
                LogClass.error("Could not install dashboard " + reportID, e);
            }
        }
        session.close();
        return infos;
    }

    public static void buildClonedDataStores(boolean copyData, FeedDefinition feedDefinition, FeedDefinition clonedFeedDefinition, Connection conn) throws Exception {
        if (copyData) {
            DataStorage sourceTable = DataStorage.writeConnection(feedDefinition, conn);
            DataSet dataSet;
            try {
                Set<AnalysisItem> validQueryItems = new HashSet<AnalysisItem>();
                for (AnalysisItem analysisItem : feedDefinition.getFields()) {
                    if (!analysisItem.isDerived()) {
                        validQueryItems.add(analysisItem);
                    }
                }
                dataSet = sourceTable.retrieveData(validQueryItems, null, null, null);
            } finally {
                sourceTable.closeConnection();
            }
            DataStorage clonedTable = DataStorage.writeConnection(clonedFeedDefinition, conn);
            try {
                clonedTable.createTable();
                clonedTable.insertData(dataSet);
                clonedTable.commit();
            } catch (Exception e) {
                clonedTable.rollback();
                throw e;
            } finally {
                clonedTable.closeConnection();
            }
        } else {
            DataStorage clonedTable = DataStorage.writeConnection(clonedFeedDefinition, conn);
            try {
                clonedTable.createTable();
                clonedTable.commit();
            } catch (SQLException e) {
                clonedTable.rollback();
                throw e;
            } finally {
                clonedTable.closeConnection();
            }
        }
    }

    public static Map<Long, SolutionInstallInfo> cloneFeed(long userID, Connection conn, FeedDefinition feedDefinition,
                                                  boolean installingFromConnection, long accountID, String userName) throws Exception {
        FeedStorage feedStorage = new FeedStorage();
        Map<Long, SolutionInstallInfo> result = feedDefinition.cloneDataSource(conn);
        FeedDefinition clonedFeedDefinition = result.get(feedDefinition.getDataFeedID()).getNewDataSource();
        clonedFeedDefinition.setUploadPolicy(new UploadPolicy(userID, accountID));
        clonedFeedDefinition.setLastRefreshStart(new Date(1));
        clonedFeedDefinition.setOwnerName(userName);
        feedStorage.addFeedDefinitionData(clonedFeedDefinition, conn);
        return result;
    }


    private static List<AnalysisDefinition> getInsightsFromFeed(long feedID, Connection conn) throws SQLException {
        AnalysisStorage analysisStorage = new AnalysisStorage();
        List<AnalysisDefinition> analyses = new ArrayList<AnalysisDefinition>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS_ID FROM ANALYSIS WHERE DATA_FEED_ID = ?");
        queryStmt.setLong(1, feedID);
        ResultSet rs = queryStmt.executeQuery();
        Session session = Database.instance().createSession(conn);
        try {
            while (rs.next()) {
                analyses.add(AnalysisDefinitionFactory.fromWSDefinition(analysisStorage.getAnalysisDefinition(rs.getLong(1), conn)));
            }
        } finally {
            session.close();
        }
        return analyses;
    }

    private static List<FeedDefinition> getFeedsFromInsight(long insightID, Connection conn) throws SQLException {
        FeedStorage feedStorage = new FeedStorage();
        List<FeedDefinition> feeds = new ArrayList<FeedDefinition>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED_ID FROM ANALYSIS_BASED_FEED WHERE analysis_id = ?");
        queryStmt.setLong(1, insightID);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long feedID = rs.getLong(1);
            feeds.add(feedStorage.getFeedDefinitionData(feedID, conn));
        }
        return feeds;
    }
}
