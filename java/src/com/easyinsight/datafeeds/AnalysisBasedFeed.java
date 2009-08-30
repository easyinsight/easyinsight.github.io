package com.easyinsight.datafeeds;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.scrubbing.DataScrub;
import com.easyinsight.core.Key;
import com.easyinsight.pipeline.DerivedDataSourcePipeline;

import java.util.*;

/**
 * User: James Boe
 * Date: May 5, 2008
 * Time: 2:45:56 PM
 */
public class AnalysisBasedFeed extends Feed {

    private WSAnalysisDefinition analysisDefinition;    

    public FeedType getDataFeedType() {
        return FeedType.ANALYSIS_BASED;
    }

    public WSAnalysisDefinition getAnalysisDefinition() {
        return analysisDefinition;
    }

    public void setAnalysisDefinition(WSAnalysisDefinition analysisDefinition) {
        this.analysisDefinition = analysisDefinition;
    }

    public List<AnalysisItem> getFields() {
        WSAnalysisDefinition analysisDefinition = getAnalysisDefinition();
        Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID());
        return feed.getFields();
    }

    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) {
        WSAnalysisDefinition analysisDefinition = getAnalysisDefinition();
        Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID());
        return feed.getMetadata(analysisItem, null);
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, Collection<Key> additionalNeededKeys) throws TokenMissingException {
        WSAnalysisDefinition analysisDefinition = getAnalysisDefinition();

        Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID());

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

        return new DerivedDataSourcePipeline().setup(getAnalysisDefinition(), this, insightRequestMetadata).toDataSet(dataSet);
    }

    public DataSet getDetails(Collection<FilterDefinition> filters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
