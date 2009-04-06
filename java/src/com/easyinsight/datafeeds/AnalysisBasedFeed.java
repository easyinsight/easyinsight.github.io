package com.easyinsight.datafeeds;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.scrubbing.DataScrub;
import com.easyinsight.core.Key;

import java.util.*;

/**
 * User: James Boe
 * Date: May 5, 2008
 * Time: 2:45:56 PM
 */
public class AnalysisBasedFeed extends Feed {

    public FeedType getDataFeedType() {
        return FeedType.ANALYSIS_BASED;
    }

    public List<AnalysisItem> getFields() {
        WSAnalysisDefinition analysisDefinition = getAnalysisDefinition();
        Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID());
        return feed.getFields();
    }

    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem) {
        WSAnalysisDefinition analysisDefinition = getAnalysisDefinition();
        Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID());
        return feed.getMetadata(analysisItem);
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, Collection<Key> additionalNeededKeys) {
        WSAnalysisDefinition analysisDefinition = getAnalysisDefinition();

        Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID());

        /*Set<Key> columnSet = new HashSet<Key>(columns);
        Set<AnalysisItem> analysisItemSet = new HashSet<AnalysisItem>();
        // TODO: is this right?
        if (analysisDefinition.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : analysisDefinition.getFilterDefinitions()) {
                List<AnalysisItem> items = filterDefinition.getField().getAnalysisItems(feed.getFields(), feed.getFields());
                for (AnalysisItem item : items) {
                    if (item.getAnalysisItemID() != 0) {
                        columnSet.add(item.getKey());
                        analysisItemSet.add(item);
                    }
                }
            }
        }
        for (Key column : columns) {
            for (AnalysisItem item : getFields()) {
                if (item.getKey().equals(column)) {
                    analysisItemSet.add(item);
                }
            }
        }*/

        /*Collection<AnalysisItem> analysisItems = report.getColumnItems(allAnalysisItems);
        Collection<FilterDefinition> filters = report.getFilterDefinitions();*/
        if (analysisDefinition.getFilterDefinitions() != null) {
            if (filters == null) {
                filters = new ArrayList<FilterDefinition>();
            }
            filters.addAll(analysisDefinition.getFilterDefinitions());
        }
        Set<Key> additionalKeys = new HashSet<Key>();
        if (analysisDefinition.getDataScrubs() != null) {
            for (DataScrub dataScrub : analysisDefinition.getDataScrubs()) {
                additionalKeys.addAll(dataScrub.createNeededKeys(feed.getFields()));
            }
        }
        DataSet dataSet = feed.getAggregateDataSet(analysisItems, filters, insightRequestMetadata, allAnalysisItems, adminMode, additionalKeys);

        return dataSet.nextStep(analysisDefinition, analysisItems, insightRequestMetadata);
    }

    protected DataSet getUncachedDataSet(List<Key> columns, Integer maxRows, boolean admin, InsightRequestMetadata insightRequestMetadata) {

       /* WSAnalysisDefinition analysisDefinition = getAnalysisDefinition();

        Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID());

        Set<Key> columnSet = new HashSet<Key>(columns);
        Set<AnalysisItem> analysisItemSet = new HashSet<AnalysisItem>();
        // TODO: is this right?
        if (analysisDefinition.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : analysisDefinition.getFilterDefinitions()) {
                List<AnalysisItem> items = filterDefinition.getField().getAnalysisItems(feed.getFields(), feed.getFields());
                for (AnalysisItem item : items) {
                    if (item.getAnalysisItemID() != 0) {
                        columnSet.add(item.getKey());
                        analysisItemSet.add(item);
                    }
                }
            }
        }
        for (Key column : columns) {
            for (AnalysisItem item : getFields()) {
                if (item.getKey().equals(column)) {
                    analysisItemSet.add(item);
                }
            }
        }
        if (analysisDefinition.getDataScrubs() != null) {
            for (DataScrub dataScrub : analysisDefinition.getDataScrubs()) {
                columnSet.addAll(dataScrub.createNeededKeys(feed.getFields()));
            }
        }

        DataSet dataSet = feed.getDataSet(new ArrayList<Key>(columnSet), maxRows, false, insightRequestMetadata);

        return dataSet.nextStep(analysisDefinition, analysisItemSet, insightRequestMetadata);*/
        throw new UnsupportedOperationException();
    }    
}
