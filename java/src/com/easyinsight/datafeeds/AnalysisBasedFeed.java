package com.easyinsight.datafeeds;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.AnalysisItem;
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

    protected DataSet getUncachedDataSet(List<Key> columns, Integer maxRows, boolean admin, InsightRequestMetadata insightRequestMetadata) {

        WSAnalysisDefinition analysisDefinition = getAnalysisDefinition();

        Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID());

        Set<Key> columnSet = new HashSet<Key>(columns);
        // TODO: is this right?
        if (analysisDefinition.getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : analysisDefinition.getFilterDefinitions()) {
                List<AnalysisItem> items = filterDefinition.getField().getAnalysisItems(feed.getFields(), feed.getFields());
                for (AnalysisItem item : items) {
                    if (item.getAnalysisItemID() != 0) {
                        columnSet.add(item.getKey());
                    }
                }
            }
        }
        if (analysisDefinition.getDataScrubs() != null) {
            for (DataScrub dataScrub : analysisDefinition.getDataScrubs()) {
                columnSet.addAll(dataScrub.createNeededKeys(feed.getFields()));
            }
        }

        DataSet dataSet = feed.getDataSet(new ArrayList<Key>(columnSet), maxRows, false, insightRequestMetadata);

        return dataSet.nextStep(analysisDefinition, getFields(), insightRequestMetadata);
    }    
}
