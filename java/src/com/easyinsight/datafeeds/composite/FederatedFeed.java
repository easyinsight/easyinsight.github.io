package com.easyinsight.datafeeds.composite;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 3/5/11
 * Time: 6:31 PM
 */
public class FederatedFeed extends Feed {

    private List<FederationSource> sources;

    public FederatedFeed(List<FederationSource> sources) {
        this.sources = sources;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            DataSet dataSet = new DataSet();
            for (FederationSource source : sources) {
                Feed feed = FeedRegistry.instance().getFeed(source.getDataSourceID(), conn);

                System.out.println("Feed " + feed.getName());

                Map<Key, Key> map = new HashMap<Key, Key>();
                for (AnalysisItem analysisItem : analysisItems) {
                    Key key = feed.getDataSource().getField(analysisItem.getKey().toKeyString());
                    map.put(key, analysisItem.getKey());
                    System.out.println("Temporarily replacing " + analysisItem.getKey() + " with " + key);
                    analysisItem.setKey(key);
                }
                DataSet childSet = feed.getAggregateDataSet(analysisItems, filters, insightRequestMetadata, allAnalysisItems, adminMode, conn);
                System.out.println(childSet);
                /*DataSetKeys keys = childSet.getDataSetKeys();
                for (Key key : keys.getKeys()) {
                    AggregateKey aggregateKey = (AggregateKey) key;
                    Key existingKey = aggregateKey.getKey();
                    Key swapKey = map.get(existingKey);
                    System.out.println("swapping " + swapKey + " in for " + existingKey);
                    aggregateKey.setKey(swapKey);
                }*/
                for (AnalysisItem analysisItem : analysisItems) {
                    analysisItem.setKey(map.get(analysisItem.getKey()));
                }
                for (IRow row : childSet.getRows()) {
                    IRow newRow = dataSet.createRow();
                    newRow.addValues(row);
                }
            }
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
