package com.easyinsight.analysis;

import com.easyinsight.datafeeds.CredentialRequirement;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: jamesboe
 * Date: Nov 30, 2009
 * Time: 9:15:12 AM
 */
public abstract class DataResults {
    private Set<CredentialRequirement> credentialRequirements;
    private Set<Long> invalidAnalysisItemIDs;
    private FeedMetadata feedMetadata;
    private DataSourceInfo dataSourceInfo;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public Set<CredentialRequirement> getCredentialRequirements() {
        return credentialRequirements;
    }

    public void setCredentialRequirements(Set<CredentialRequirement> credentialRequirements) {
        this.credentialRequirements = credentialRequirements;
    }

    public DataSourceInfo getDataSourceInfo() {
        return dataSourceInfo;
    }

    public void setDataSourceInfo(DataSourceInfo dataSourceInfo) {
        this.dataSourceInfo = dataSourceInfo;
    }

    public FeedMetadata getFeedMetadata() {
        return feedMetadata;
    }

    public void setFeedMetadata(FeedMetadata feedMetadata) {
        this.feedMetadata = feedMetadata;
    }

    public Set<Long> getInvalidAnalysisItemIDs() {
        return invalidAnalysisItemIDs;
    }

    public void setInvalidAnalysisItemIDs(Set<Long> invalidAnalysisItemIDs) {
        this.invalidAnalysisItemIDs = invalidAnalysisItemIDs;
    }

    public abstract EmbeddedResults toEmbeddedResults();
}
