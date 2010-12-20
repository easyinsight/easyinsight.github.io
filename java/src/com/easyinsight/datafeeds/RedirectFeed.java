package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.database.EIConnection;
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
        return FeedRegistry.instance().getFeed(redirectID, null).getFields();
    }

    @Override
    public List<FeedNode> getFieldHierarchy() {
        return FeedRegistry.instance().getFeed(redirectID, null).getFieldHierarchy();
    }

    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws ReportException {
        return FeedRegistry.instance().getFeed(redirectID, conn).getMetadata(analysisItem, insightRequestMetadata, conn);
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        return FeedRegistry.instance().getFeed(redirectID, conn).getAggregateDataSet(analysisItems, filters, insightRequestMetadata, allAnalysisItems, adminMode, conn);
    }
}
