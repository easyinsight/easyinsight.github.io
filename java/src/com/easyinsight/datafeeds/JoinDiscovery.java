package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.DerivedKey;

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

    public List<CompositeFeedConnection> findPotentialJoins(long sourceFeedID, long targetFeedID) throws SQLException {
        FeedDefinition sourceFeed = feedStorage.getFeedDefinitionData(sourceFeedID);
        FeedDefinition targetFeed = feedStorage.getFeedDefinitionData(targetFeedID);

        List<FeedDefinition> sourceFeeds;
        List<FeedDefinition> targetFeeds;
        if (sourceFeed instanceof CompositeFeedDefinition) {
            CompositeFeedDefinition source = (CompositeFeedDefinition) sourceFeed;
            FeedVisitor feedVisitor = new FeedVisitor();
            feedVisitor.visit(source);
            sourceFeeds = feedVisitor.feeds;
        } else {
            sourceFeeds = Arrays.asList(sourceFeed);
        }

        if (targetFeed instanceof CompositeFeedDefinition) {
            CompositeFeedDefinition source = (CompositeFeedDefinition) targetFeed;
            FeedVisitor feedVisitor = new FeedVisitor();
            feedVisitor.visit(source);
            targetFeeds = feedVisitor.feeds;
        } else {
            targetFeeds = Arrays.asList(targetFeed);
        }
        List<CompositeFeedConnection> connections = new ArrayList<CompositeFeedConnection>();
        for (FeedDefinition sourceDefinition : sourceFeeds) {
            for (FeedDefinition targetDefinition : targetFeeds) {
                connections.addAll(explore(sourceDefinition, targetDefinition));
            }
        }
        for (CompositeFeedConnection connection : connections) {
            if (connection.getSourceFeedID() != sourceFeedID) {
                for (AnalysisItem field : sourceFeed.getFields()) {
                    Key key = field.getKey();
                    if (key instanceof DerivedKey) {
                        DerivedKey derivedKey = (DerivedKey) key;
                        if (derivedKey.toBaseKey().equals(connection.getSourceJoin())) {
                            connection.setSourceFeedID(sourceFeedID);
                            connection.setSourceJoin(derivedKey);
                        }
                    }
                }
            }
            if (connection.getTargetFeedID() != targetFeedID) {
                for (AnalysisItem field : targetFeed.getFields()) {
                    Key key = field.getKey();
                    if (key instanceof DerivedKey) {
                        DerivedKey derivedKey = (DerivedKey) key;
                        if (derivedKey.toBaseKey().equals(connection.getTargetJoin())) {
                            connection.setTargetFeedID(targetFeedID);
                            connection.setTargetJoin(derivedKey);
                        }
                    }
                }
            }
        }
        return connections;
    }

    private class FeedVisitor extends CompositeFeedNodeVisitor {
        private List<FeedDefinition> feeds = new ArrayList<FeedDefinition>();

        protected void accept(CompositeFeedNode compositeFeedNode) throws SQLException {
            FeedDefinition feed = feedStorage.getFeedDefinitionData(compositeFeedNode.getDataFeedID());
            if (!(feed instanceof CompositeFeedDefinition)) {
                feeds.add(feed);
            }
        }
    }

    private List<CompositeFeedConnection> explore(FeedDefinition sourceFeed, FeedDefinition targetFeed) throws SQLException {
        Connection conn = Database.instance().getConnection();
        List<CompositeFeedConnection> potentialJoins;
        try {
            potentialJoins = new ArrayList<CompositeFeedConnection>();
            Map<Key, List<Value>> valueMap = new HashMap<Key, List<Value>>();
            for (AnalysisItem analysisItem : sourceFeed.getFields()) {
                if (!analysisItem.isDerived()) {
                    if (analysisItem.getType() == AnalysisItemTypes.DIMENSION) {
                        AnalysisDimensionResultMetadata metadata = (AnalysisDimensionResultMetadata) new DataService().getAnalysisItemMetadata(sourceFeed.getDataFeedID(), analysisItem, 0);
                        valueMap.put(analysisItem.getKey(), metadata.getValues());
                    }
                }
            }
            for (AnalysisItem analysisItem : targetFeed.getFields()) {
                if (!analysisItem.isDerived()) {
                    if (analysisItem.getType() == AnalysisItemTypes.DIMENSION) {
                        AnalysisDimensionResultMetadata metadata = (AnalysisDimensionResultMetadata) new DataService().getAnalysisItemMetadata(targetFeed.getDataFeedID(), analysisItem, 0);
                        Set<Value> valueSet = new HashSet<Value>(metadata.getValues());
                        for (Map.Entry<Key, List<Value>> entry : valueMap.entrySet()) {
                            boolean matched = false;
                            int matches = 0;
                            int mismatches = 0;
                            for (Value sourceValue : entry.getValue()) {
                                if (!valueSet.contains(sourceValue)) {
                                    mismatches++;
                                } else {
                                    matches++;
                                }
                            }
                            if (matches > 0) {
                                matched = mismatches == 0 || matches > mismatches;
                            }
                            if (matched) {
                                potentialJoins.add(new CompositeFeedConnection(sourceFeed.getDataFeedID(), targetFeed.getDataFeedID(), entry.getKey(), analysisItem.getKey()));
                            }
                        }
                    }
                }
            }
        } finally {
            Database.closeConnection(conn);
        }
        return potentialJoins;
    }
}
