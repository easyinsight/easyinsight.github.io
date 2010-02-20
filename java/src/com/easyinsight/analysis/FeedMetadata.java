package com.easyinsight.analysis;

import com.easyinsight.datafeeds.CredentialRequirement;
import com.easyinsight.datafeeds.FeedNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Jan 13, 2008
 * Time: 6:08:58 PM
 */
public class FeedMetadata implements Serializable {
    private AnalysisItem[] fields;
    private List<FeedNode> fieldHierarchy;
    private long dataFeedID;
    private String dataSourceName;
    private int version;
    private boolean dataSourceAdmin;
    private long originSolution;
    private String filterExampleMessage;
    private Set<CredentialRequirement> credentials = new HashSet<CredentialRequirement>();
    private List<FilterDefinition> intrinsicFilters = new ArrayList<FilterDefinition>();

    public String getFilterExampleMessage() {
        return filterExampleMessage;
    }

    public void setFilterExampleMessage(String filterExampleMessage) {
        this.filterExampleMessage = filterExampleMessage;
    }

    public long getOriginSolution() {
        return originSolution;
    }

    public void setOriginSolution(long originSolution) {
        this.originSolution = originSolution;
    }

    public List<FeedNode> getFieldHierarchy() {
        return fieldHierarchy;
    }

    public void setFieldHierarchy(List<FeedNode> fieldHierarchy) {
        this.fieldHierarchy = fieldHierarchy;
    }

    public List<FilterDefinition> getIntrinsicFilters() {
        return intrinsicFilters;
    }

    public void setIntrinsicFilters(List<FilterDefinition> intrinsicFilters) {
        this.intrinsicFilters = intrinsicFilters;
    }

    public Set<CredentialRequirement> getCredentials() {
        return credentials;
    }

    public void setCredentials(Set<CredentialRequirement> credentials) {
        this.credentials = credentials;
    }

    public boolean isDataSourceAdmin() {
        return dataSourceAdmin;
    }

    public void setDataSourceAdmin(boolean dataSourceAdmin) {
        this.dataSourceAdmin = dataSourceAdmin;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }

    public AnalysisItem[] getFields() {
        return fields;
    }

    public void setFields(AnalysisItem[] fields) {
        this.fields = fields;
    }
}
