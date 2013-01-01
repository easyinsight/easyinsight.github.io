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
    private List<AnalysisItem> fields;
    private List<FeedNode> fieldHierarchy;

    public RedirectFeed(long redirectID, List<AnalysisItem> fields, List<FeedNode> fieldHierarchy) {
        this.redirectID = redirectID;
        this.fields = fields;
        this.fieldHierarchy = fieldHierarchy;
    }

    @Override
    public List<AnalysisItem> getFields() {
        return fields;
    }

    @Override
    public List<FeedNode> getFieldHierarchy() {
        return fieldHierarchy;
    }

    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report, List<FilterDefinition> otherFilters, FilterDefinition requester) throws ReportException {
        return FeedRegistry.instance().getFeed(redirectID, conn).getMetadata(analysisItem, insightRequestMetadata, conn, report, otherFilters, requester);
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        return FeedRegistry.instance().getFeed(redirectID, conn).getAggregateDataSet(analysisItems, filters, insightRequestMetadata, allAnalysisItems, adminMode, conn);
    }
}
