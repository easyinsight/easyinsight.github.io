package com.easyinsight.datafeeds;


import com.easyinsight.storage.DataStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.userupload.UploadPolicy;

import com.easyinsight.database.Database;
import com.easyinsight.solutions.SolutionInstallInfo;

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

    /*public static void buildClonedDataStores(boolean copyData, FeedDefinition feedDefinition, FeedDefinition clonedFeedDefinition, Connection conn) throws Exception {
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
    }*/
}
