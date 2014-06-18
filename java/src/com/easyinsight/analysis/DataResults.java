package com.easyinsight.analysis;

import com.easyinsight.intention.IntentionSuggestion;

import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 30, 2009
 * Time: 9:15:12 AM
 */
public abstract class DataResults implements Cloneable {
    
    private Set<Long> invalidAnalysisItemIDs;
    private WSAnalysisDefinition report;
    private FeedMetadata feedMetadata;
    private DataSourceInfo dataSourceInfo;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private List<ReportAuditEvent> auditMessages;
    private ReportFault reportFault;
    private List<IntentionSuggestion> suggestions;
    private String reportLog;
    private String uid;
    private long databaseTime;
    private long processingTime;
    private Map<String, List<String>> fieldEvents = new HashMap<String, List<String>>();
    private Map<String, List<String>> filterEvents = new HashMap<String, List<String>>();
    private String cacheForHTMLKey;

    public String getCacheForHTMLKey() {
        return cacheForHTMLKey;
    }

    public void setCacheForHTMLKey(String cacheForHTMLKey) {
        this.cacheForHTMLKey = cacheForHTMLKey;
    }

    public Map<String, List<String>> getFilterEvents() {
        return filterEvents;
    }

    public void setFilterEvents(Map<String, List<String>> filterEvents) {
        this.filterEvents = filterEvents;
    }

    public Map<String, List<String>> getFieldEvents() {
        return fieldEvents;
    }

    public void setFieldEvents(Map<String, List<String>> fieldEvents) {
        this.fieldEvents = fieldEvents;
    }

    public WSAnalysisDefinition getReport() {
        return report;
    }

    public void setReport(WSAnalysisDefinition report) {
        this.report = report;
    }

    public long getDatabaseTime() {
        return databaseTime;
    }

    public void setDatabaseTime(long databaseTime) {
        this.databaseTime = databaseTime;
    }

    public long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(long processingTime) {
        this.processingTime = processingTime;
    }

    public DataResults clone() throws CloneNotSupportedException {
        return (DataResults) super.clone();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getReportLog() {
        return reportLog;
    }

    public void setReportLog(String reportLog) {
        this.reportLog = reportLog;
    }

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

    public List<ReportAuditEvent> getAuditMessages() {
        return auditMessages;
    }

    public void setAuditMessages(List<ReportAuditEvent> auditMessages) {
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
