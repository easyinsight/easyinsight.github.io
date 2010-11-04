package com.easyinsight.datafeeds;

import com.easyinsight.storage.DataStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.logging.LogClass;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.userupload.UserUploadInternalService;
import com.easyinsight.database.Database;
import com.easyinsight.api.APIService;
import com.easyinsight.api.dynamic.DynamicServiceDefinition;
import com.easyinsight.api.dynamic.ConfiguredMethod;
import com.easyinsight.solutions.SolutionInstallInfo;
import com.easyinsight.security.Roles;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.core.DataSourceDescriptor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: May 27, 2009
 * Time: 4:25:09 PM
 */
public class DataSourceCopyUtils {

    public static List<SolutionInstallInfo> installFeed(long userID, Connection conn, boolean copyData, long feedID,
                                                        FeedDefinition feedDefinition, boolean includeChildren, String newDataSourceName,
                                                        long solutionID, long accountID, String userName) throws Exception {
        FeedStorage feedStorage = new FeedStorage();
        AnalysisStorage analysisStorage = new AnalysisStorage();
        List<SolutionInstallInfo> infos = new ArrayList<SolutionInstallInfo>();

        // result here needs to have the core keys
        // 

        DataSourceCloneResult result = cloneFeed(userID, conn, feedDefinition, solutionID > 0, accountID, userName);
        FeedDefinition clonedFeedDefinition = result.getFeedDefinition();
        if (solutionID > 0) {
            clonedFeedDefinition.setVisible(false);
        }
        clonedFeedDefinition.setDateCreated(new Date());
        clonedFeedDefinition.setDateUpdated(new Date());
        if (newDataSourceName != null) {
            clonedFeedDefinition.setFeedName(newDataSourceName);
        }
        feedStorage.updateDataFeedConfiguration(clonedFeedDefinition, conn);
        new UserUploadInternalService().createUserFeedLink(userID, clonedFeedDefinition.getDataFeedID(), Roles.OWNER, conn);
        if ((feedDefinition.getDataSourceType() == DataSourceInfo.STORED_PUSH || feedDefinition.getDataSourceType() == DataSourceInfo.STORED_PULL)) {
            buildClonedDataStores(copyData, feedDefinition, clonedFeedDefinition, conn);
        } else {
            DataStorage.liveDataSource(result.getFeedDefinition().getDataFeedID(), conn);
        }
        try {
            clonedFeedDefinition.postClone(conn);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (includeChildren) {
            List<AnalysisDefinition> insights = getInsightsFromFeed(feedID, conn);
            for (AnalysisDefinition insight : insights) {
                if (insight.isRootDefinition()) {
                    continue;
                }
                AnalysisDefinition clonedInsight = insight.clone(result.getKeyReplacementMap(), clonedFeedDefinition.getFields());
                // need to update keys on the analysis items at this point
                clonedInsight.setAnalysisPolicy(AnalysisPolicy.PRIVATE);
                clonedInsight.setDataFeedID(clonedFeedDefinition.getDataFeedID());
                clonedInsight.setUserBindings(Arrays.asList(new UserToAnalysisBinding(userID, UserPermission.OWNER)));
                analysisStorage.saveAnalysis(clonedInsight, conn);
                InsightDescriptor insightDescriptor = new InsightDescriptor(clonedInsight.getAnalysisID(), clonedInsight.getTitle(),
                        clonedInsight.getDataFeedID(), clonedInsight.getReportType(), clonedInsight.getUrlKey());
                infos.add(new SolutionInstallInfo(insight.getAnalysisID(), insightDescriptor, null, false));
                List<FeedDefinition> insightFeeds = getFeedsFromInsight(clonedInsight.getAnalysisID(), conn);
                for (FeedDefinition insightFeed : insightFeeds) {
                    infos.addAll(installFeed(userID, conn, copyData, insightFeed.getDataFeedID(), insightFeed, true, null, solutionID, accountID, userName));
                }
            }
        }

        boolean requiresConfig = solutionID > 0;

        DataSourceDescriptor dataSourceDescriptor = new DataSourceDescriptor(clonedFeedDefinition.getFeedName(), clonedFeedDefinition.getDataFeedID(),
                clonedFeedDefinition.getFeedType().getType());
        infos.add(new SolutionInstallInfo(feedDefinition.getDataFeedID(), dataSourceDescriptor, clonedFeedDefinition.getFeedName(), requiresConfig));

        if (solutionID > 0) {
            PreparedStatement installStmt = conn.prepareStatement("INSERT INTO SOLUTION_INSTALL (SOLUTION_ID, installed_data_source_id, original_data_source_id) VALUES (?, ?, ?)");
            installStmt.setLong(1, solutionID);
            installStmt.setLong(2, clonedFeedDefinition.getDataFeedID());
            installStmt.setLong(3, feedDefinition.getDataFeedID());
            installStmt.execute();
        }
        return infos;
    }

    public static void buildClonedDataStores(boolean copyData, FeedDefinition feedDefinition, FeedDefinition clonedFeedDefinition, Connection conn) throws SQLException {
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
            } catch (SQLException e) {
                LogClass.error(e);
                clonedTable.rollback();
                throw new RuntimeException(e);
            } finally {
                clonedTable.closeConnection();
            }
        } else {
            DataStorage clonedTable = DataStorage.writeConnection(clonedFeedDefinition, conn);
            try {
                clonedTable.createTable();
                clonedTable.commit();
            } catch (SQLException e) {
                LogClass.error(e);
                clonedTable.rollback();
                throw new RuntimeException(e);
            } finally {
                clonedTable.closeConnection();
            }
        }
    }

    public static DataSourceCloneResult cloneFeed(long userID, Connection conn, FeedDefinition feedDefinition,
                                                  boolean installingFromConnection, long accountID, String userName) throws Exception {
        FeedStorage feedStorage = new FeedStorage();
        DataSourceCloneResult result = feedDefinition.cloneDataSource(conn);
        FeedDefinition clonedFeedDefinition = result.getFeedDefinition();
        clonedFeedDefinition.setUploadPolicy(new UploadPolicy(userID, accountID));
        clonedFeedDefinition.setOwnerName(userName);
        feedStorage.addFeedDefinitionData(clonedFeedDefinition, conn);
        if (feedDefinition.getFeedType().equals(FeedType.ANALYSIS_BASED)) {
            // TODO: repair, we don't use atm
            /*if (feedDefinition.getAnalysisDefinitionID() > 0) {
                AnalysisDefinition clonedRootInsight = analysisStorage.cloneReport(feedDefinition.getAnalysisDefinitionID(), conn, result.getKeyReplacementMap(), clonedFeedDefinition.getFields());
                if (clonedRootInsight != null) {
                    clonedRootInsight.setUserBindings(Arrays.asList(new UserToAnalysisBinding(userID, UserPermission.OWNER)));
                    analysisStorage.saveAnalysis(clonedRootInsight, conn);
                    clonedFeedDefinition.setAnalysisDefinitionID(clonedRootInsight.getAnalysisID());
                }
            }*/
        }
        if (clonedFeedDefinition.getDynamicServiceDefinitionID() > 0) {
            cloneAPIs(conn, feedDefinition, clonedFeedDefinition);
        }
        return result;
    }

    private static void cloneAPIs(Connection conn, FeedDefinition feedDefinition, FeedDefinition clonedFeedDefinition) {
        Session session = Database.instance().createSession(conn);
        try {
            APIService apiService = new APIService();
            DynamicServiceDefinition dynamicServiceDefinition = apiService.getDynamicServiceDefinition(feedDefinition.getDataFeedID(), conn, session);
            List<ConfiguredMethod> clonedConfiguredMethods = new ArrayList<ConfiguredMethod>();
            for (ConfiguredMethod configuredMethod : dynamicServiceDefinition.getConfiguredMethods()) {
                List<AnalysisItem> clonedMethodItems = new ArrayList<AnalysisItem>();
                for (AnalysisItem keyItem : configuredMethod.getKeys()) {
                    // find that item in the cloned definition...
                    AnalysisItem matchedItem = null;
                    for (AnalysisItem clonedItem : clonedFeedDefinition.getFields()) {
                        if (clonedItem.equals(keyItem)) {
                            matchedItem = clonedItem;
                        }
                    }
                    clonedMethodItems.add(matchedItem);
                }
                ConfiguredMethod clonedMethod = configuredMethod.clone();
                clonedMethod.setKeys(clonedMethodItems);
                clonedConfiguredMethods.add(clonedMethod);
            }
            DynamicServiceDefinition clonedDefinition = dynamicServiceDefinition.clone();
            clonedDefinition.setConfiguredMethods(clonedConfiguredMethods);
            apiService.addDynamicServiceDefinition(clonedDefinition, conn);
            clonedFeedDefinition.setDynamicServiceDefinitionID(clonedDefinition.getServiceID());
            session.flush();
        } finally {
            session.close();
        }
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
                analyses.add(AnalysisDefinitionFactory.fromWSDefinition(analysisStorage.getAnalysisDefinition(rs.getLong(1), conn), session));
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
