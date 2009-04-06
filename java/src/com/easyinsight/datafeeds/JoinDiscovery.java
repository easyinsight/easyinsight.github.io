package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.IRow;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;

import java.util.*;
import java.sql.SQLException;
import java.sql.Connection;

/**
 * User: James Boe
 * Date: Sep 19, 2008
 * Time: 9:29:22 AM
 */
public class JoinDiscovery {

    private FeedStorage feedStorage = new FeedStorage();

    public List<CompositeFeedConnection> findPotentialJoins(long sourceFeedID, long targetFeedID) {
        FeedDefinition sourceFeed = feedStorage.getFeedDefinitionData(sourceFeedID);
        FeedDefinition targetFeed = feedStorage.getFeedDefinitionData(targetFeedID);
        return explore(sourceFeed, targetFeed);
    }

    private List<CompositeFeedConnection> explore(FeedDefinition sourceFeed, FeedDefinition targetFeed) {
        Connection conn = Database.instance().getConnection();
        List<CompositeFeedConnection> potentialJoins;
        try {
            potentialJoins = new ArrayList<CompositeFeedConnection>();
            Map<Key, Value[]> valueMap = new HashMap<Key, Value[]>();
            for (AnalysisItem analysisItem : sourceFeed.getFields()) {
                if (!analysisItem.isDerived()) {
                    if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
                        List<AnalysisItem> keys = Arrays.asList(analysisItem);
                        DataStorage metadata = DataStorage.writeConnection(sourceFeed, conn);
                        DataSet dataSet;
                        try {
                            dataSet = metadata.retrieveData(keys, null, null, 3);
                        } finally {
                            metadata.closeConnection();
                        }
                        Value[] subset = new Value[Math.min(dataSet.getRows().size(), 3)];
                        for (int i = 0; i < Math.min(dataSet.getRows().size(), 3); i++) {
                            IRow row = dataSet.getRow(i);
                            subset[i] = row.getValue(analysisItem.getKey());
                        }
                        valueMap.put(analysisItem.getKey(), subset);
                    }
                }
            }
            for (AnalysisItem analysisItem : targetFeed.getFields()) {
                if (!analysisItem.isDerived()) {
                    if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
                        List<AnalysisItem> keys = Arrays.asList(analysisItem);
                        DataStorage metadata = DataStorage.writeConnection(targetFeed, conn);
                        DataSet dataSet;
                        try {
                            dataSet = metadata.retrieveData(keys, null, null, 3);
                        } finally {
                            metadata.closeConnection();
                        }
                        Set<Value> valueSet = new HashSet<Value>();
                        for (IRow row : dataSet.getRows()) {
                            valueSet.add(row.getValue(analysisItem.getKey()));
                        }
                        for (Map.Entry<Key, Value[]> entry : valueMap.entrySet()) {
                            boolean matched = true;
                            for (Value sourceValue : entry.getValue()) {
                                if (!valueSet.contains(sourceValue)) {
                                    matched = false;
                                }
                            }
                            if (matched) {
                                potentialJoins.add(new CompositeFeedConnection(sourceFeed.getDataFeedID(), targetFeed.getDataFeedID(), entry.getKey(), analysisItem.getKey()));
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return potentialJoins;
    }
}
