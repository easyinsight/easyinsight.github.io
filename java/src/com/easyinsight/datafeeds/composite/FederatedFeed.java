package com.easyinsight.datafeeds.composite;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
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

                Map<AnalysisItem, AnalysisItem> map = new HashMap<AnalysisItem, AnalysisItem>();
                Set<AnalysisItem> childAnalysisItems = new HashSet<AnalysisItem>();
                for (AnalysisItem analysisItem : analysisItems) {
                    for (AnalysisItem field : feed.getDataSource().getFields()) {
                        if (field.toDisplay().equals(analysisItem.toDisplay())) {
                            childAnalysisItems.add(field);
                            map.put(field, analysisItem);
                        }
                    }
                }
                DataSet childSet = feed.getAggregateDataSet(childAnalysisItems, filters, insightRequestMetadata, allAnalysisItems, adminMode, conn);

                for (IRow row : childSet.getRows()) {
                    IRow newRow = dataSet.createRow();
                    for (Map.Entry<AnalysisItem, AnalysisItem> entry : map.entrySet()) {
                        Value value = row.getValue(entry.getKey());
                        newRow.addValue(entry.getValue().createAggregateKey(), value);
                    }
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
