package com.easyinsight.datafeeds;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.AnalysisItem;
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

        DataSet dataSet = feed.getDataSet(columns, maxRows, false, insightRequestMetadata);

        return dataSet.nextStep(analysisDefinition, getFields(), insightRequestMetadata);
    }    
}
