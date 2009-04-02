package com.easyinsight.datafeeds;


import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemResultMetadata;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.core.Key;

import java.util.List;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 11:46:38 AM
 */
public abstract class Feed {
    private long feedID;
    private List<AnalysisItem> fields;
    private WSAnalysisDefinition analysisDefinition;
    private int version;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<AnalysisItem> getFields() {
        return fields;
    }

    public WSAnalysisDefinition getAnalysisDefinition() {
        return analysisDefinition;
    }

    public void setAnalysisDefinition(WSAnalysisDefinition analysisDefinition) {
        this.analysisDefinition = analysisDefinition;
    }

    public void setFields(List<AnalysisItem> fields) {
        this.fields = fields;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public abstract FeedType getDataFeedType();

    public abstract AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem);

    public DataSet getDataSet(List<Key> columns, Integer maxRows, boolean admin, InsightRequestMetadata insightRequestMetadata) {
        // okay, credentials may be necessary here...
        return getUncachedDataSet(columns, maxRows, admin, insightRequestMetadata);
    }

    protected abstract DataSet getUncachedDataSet(List<Key> columns, Integer maxRows, boolean admin, InsightRequestMetadata insightRequestMetadata);
}
