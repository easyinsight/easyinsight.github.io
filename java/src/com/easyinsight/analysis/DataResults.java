package com.easyinsight.analysis;

import com.easyinsight.intention.IntentionSuggestion;

import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 30, 2009
 * Time: 9:15:12 AM
 */
public abstract class DataResults {
    
    private Set<Long> invalidAnalysisItemIDs;
    private FeedMetadata feedMetadata;
    private DataSourceInfo dataSourceInfo;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private List<String> auditMessages;
    private ReportFault reportFault;
    private List<IntentionSuggestion> suggestions;

    public List<IntentionSuggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<IntentionSuggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public ReportFault getReportFault() {
        return reportFault;
    }

    public void setReportFault(ReportFault reportFault) {
        this.reportFault = reportFault;
    }

    public List<String> getAuditMessages() {
        return auditMessages;
    }

    public void setAuditMessages(List<String> auditMessages) {
        this.auditMessages = auditMessages;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
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
