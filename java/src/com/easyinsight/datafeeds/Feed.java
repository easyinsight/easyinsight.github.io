package com.easyinsight.datafeeds;


import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;

import java.util.*;
import java.io.Serializable;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 11:46:38 AM
 */
public abstract class Feed implements Serializable {
    private long feedID;
    private List<AnalysisItem> fields;
    private List<FeedNode> fieldHierarchy;
    private int version;
    private String name;
    private String attribution;
    private boolean visible;
    private int type;
    private long originSolution;

    public long getOriginSolution() {
        return originSolution;
    }

    public void setOriginSolution(long originSolution) {
        this.originSolution = originSolution;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DataSourceInfo getDataSourceInfo() {
        DataSourceInfo dataSourceInfo = new DataSourceInfo();
        dataSourceInfo.setType(type);
        dataSourceInfo.setDataSourceID(feedID);
        dataSourceInfo.setDataSourceName(name);
        dataSourceInfo.setLiveDataSource(!getCredentialRequirement(false).isEmpty());
        dataSourceInfo.setOriginSolution(originSolution);
        return dataSourceInfo;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public List<FeedNode> getFieldHierarchy() {
        return fieldHierarchy;
    }

    public void setFieldHierarchy(List<FeedNode> fieldHierarchy) {
        this.fieldHierarchy = fieldHierarchy;
    }

    public List<FilterDefinition> getIntrinsicFilters() {
        return new ArrayList<FilterDefinition>();
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

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

    public void setFields(List<AnalysisItem> fields) {
        this.fields = fields;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public Set<CredentialRequirement> getCredentialRequirement(boolean allSources) {
        return new HashSet<CredentialRequirement>();
    }

    public List<Long> getDataSourceIDs() {
        List<Long> ids = new ArrayList<Long>();
        ids.add(feedID);
        return ids;
    }

    public abstract AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata);

    public abstract DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, Collection<Key> additionalNeededKeys) throws TokenMissingException;

    /*public DataSet getDataSet(List<Key> columns, Integer maxRows, boolean admin, InsightRequestMetadata insightRequestMetadata) {
        // okay, credentials may be necessary here...
        return getUncachedDataSet(columns, maxRows, admin, insightRequestMetadata);
    }*/

    public abstract DataSet getDetails(Collection<FilterDefinition> filters);
}
