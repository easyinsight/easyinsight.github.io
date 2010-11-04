package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * User: jamesboe
 * Date: Aug 17, 2010
 * Time: 10:14:59 AM
 */
public class RedirectFeed extends Feed implements Serializable {

    private long redirectID;

    public RedirectFeed(long redirectID) {
        this.redirectID = redirectID;
    }

    @Override
    public List<AnalysisItem> getFields() {
        return FeedRegistry.instance().getFeed(redirectID).getFields();
    }

    @Override
    public List<FeedNode> getFieldHierarchy() {
        return FeedRegistry.instance().getFeed(redirectID).getFieldHierarchy();
    }

    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) throws ReportException {
        return FeedRegistry.instance().getFeed(redirectID).getMetadata(analysisItem, insightRequestMetadata);
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode) throws ReportException {
        return FeedRegistry.instance().getFeed(redirectID).getAggregateDataSet(analysisItems, filters, insightRequestMetadata, allAnalysisItems, adminMode);
    }
}
