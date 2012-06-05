package com.easyinsight.datafeeds;

import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.analysis.AnalysisItem;
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
        Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID(), null);
        return feed.getFields();
    }

    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report) throws ReportException {
        WSAnalysisDefinition analysisDefinition = getAnalysisDefinition();
        Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID(), conn);
        return feed.getMetadata(analysisItem, null, conn, report);
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        WSAnalysisDefinition analysisDefinition = getAnalysisDefinition();

        Feed feed = FeedRegistry.instance().getFeed(analysisDefinition.getDataFeedID(), conn);

        if (analysisDefinition.retrieveFilterDefinitions() != null) {
            if (filters == null) {
                filters = new ArrayList<FilterDefinition>();
            }
            filters.addAll(analysisDefinition.retrieveFilterDefinitions());
        }
        DataSet dataSet = feed.getAggregateDataSet(analysisItems, filters, insightRequestMetadata, allAnalysisItems, adminMode, conn);

        return new DerivedDataSourcePipeline().setup(getAnalysisDefinition(), this, insightRequestMetadata).toDataSet(dataSet);
    }
}
