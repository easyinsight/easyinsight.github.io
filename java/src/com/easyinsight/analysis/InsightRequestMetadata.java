package com.easyinsight.analysis;

import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.pipeline.Pipeline;

import java.util.*;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Oct 28, 2008
 * Time: 11:22:35 AM
 */
public class InsightRequestMetadata implements Serializable {
    private Date now = new Date();
    private AnalysisDateDimension baseDate;
    private boolean dateJoin;
    private int utcOffset;
    private int version;
    private boolean refreshAllSources;
    private boolean noCache;
    private List<AnalysisItemOverride> hierarchyOverrides = new ArrayList<AnalysisItemOverride>();
    private boolean aggregateQuery = true;
    private boolean gmtData;
    private List<JoinOverride> joinOverrides = new ArrayList<JoinOverride>();
    private boolean optimized;
    private boolean traverseAllJoins;
    private boolean logReport;
    private Collection<AnalysisItem> reportItems;
    private List<AnalysisItem> additionalAnalysisItems = new ArrayList<AnalysisItem>();
    private transient Map<UniqueKey, AnalysisItem> uniqueIteMap = new HashMap<UniqueKey, AnalysisItem>();
    private transient Map<String, UniqueKey> fieldToUniqueMap = new HashMap<String, UniqueKey>();
    private transient Map<AnalysisItem, Set<String>> pipelineAssignmentMap = new HashMap<AnalysisItem, Set<String>>();
    private transient Map<AnalysisItem, String> derivedFieldAssignmentMap = new HashMap<AnalysisItem, String>();

    private transient List<ReportAuditEvent> auditEvents = new ArrayList<ReportAuditEvent>();
    private transient List<String> warnings = new ArrayList<String>();

    private transient Collection<FilterDefinition> reportFilters;

    private transient Set<FilterDefinition> suppressedFilters = new HashSet<FilterDefinition>();

    private transient List<IntentionSuggestion> suggestions = new ArrayList<IntentionSuggestion>();

    private transient String targetCurrency;
    private transient Map<AnalysisItem, AnalysisItem> currencyMap = new HashMap<AnalysisItem, AnalysisItem>();
    private transient Set<AnalysisItem> postProcessJoins = new HashSet<AnalysisItem>();
    private List<AddonReport> addonReports;
    private boolean noDataOnNoJoin;
    private String ip;
    private transient boolean noLogging;
    private transient Map<String, List<String>> fieldAudits = new HashMap<String, List<String>>();
    private transient Map<String, List<String>> filterAudits = new HashMap<String, List<String>>();

    private transient boolean aggregationRowChanged;

    private transient List<AnalysisItem> allItems;
    private transient AnalysisItemRetrievalStructure structure;

    public boolean isAggregationRowChanged() {
        return aggregationRowChanged;
    }

    public void setAggregationRowChanged(boolean aggregationRowChanged) {
        this.aggregationRowChanged = aggregationRowChanged;
    }

    public List<AnalysisItem> getAllItems() {
        return allItems;
    }

    public void setAllItems(List<AnalysisItem> allItems) {
        this.allItems = allItems;
    }

    public AnalysisItemRetrievalStructure getStructure() {
        return structure;
    }

    public void setStructure(AnalysisItemRetrievalStructure structure) {
        this.structure = structure;
    }

    public Map<String, List<String>> getFieldAudits() {
        return fieldAudits;
    }

    public boolean isDateJoin() {
        return dateJoin;
    }

    public void setDateJoin(boolean dateJoin) {
        this.dateJoin = dateJoin;
    }

    public AnalysisDateDimension getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(AnalysisDateDimension baseDate) {
        this.baseDate = baseDate;
    }

    public Map<String, List<String>> getFilterAudits() {
        return filterAudits;
    }

    public void setFilterAudits(Map<String, List<String>> filterAudits) {
        this.filterAudits = filterAudits;
    }

    public void addAudit(AnalysisItem field, String audit) {
        List<String> audits = fieldAudits.get(field.toDisplay());
        if (audits == null) {
            audits = new ArrayList<String>();
            fieldAudits.put(field.toDisplay(), audits);
        }
        audits.add(audit);
    }

    public void addAudit(FilterDefinition filter, String audit) {
        List<String> audits = filterAudits.get(filter.label(false));
        if (audits == null) {
            audits = new ArrayList<String>();
            filterAudits.put(filter.label(false), audits);
        }
        audits.add(audit);
    }

    public List<ReportAuditEvent> getAuditEvents() {
        return auditEvents;
    }

    public void setAuditEvents(List<ReportAuditEvent> auditEvents) {
        this.auditEvents = auditEvents;
    }

    public Collection<FilterDefinition> getReportFilters() {
        return reportFilters;
    }

    public void setReportFilters(Collection<FilterDefinition> reportFilters) {
        this.reportFilters = reportFilters;
    }

    private transient Map<String, Boolean> filterOverrideMap = new HashMap<String, Boolean>();

    private transient int fetchSize;

    private transient Map<AnalysisItem, Boolean> distinctFieldMap = new HashMap<AnalysisItem, Boolean>();

    private transient Map<FilterDefinition, AdvancedFilterProperties> filterPropertiesMap = new HashMap<FilterDefinition, AdvancedFilterProperties>();

    private transient Collection<FilterDefinition> filters;

    public Collection<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(Collection<FilterDefinition> filters) {
        this.filters = filters;
    }

    public boolean isLogReport() {
        return logReport;
    }

    public void setLogReport(boolean logReport) {
        this.logReport = logReport;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public Map<String, Boolean> getFilterOverrideMap() {
        return filterOverrideMap;
    }

    public void setFilterOverrideMap(Map<String, Boolean> filterOverrideMap) {
        this.filterOverrideMap = filterOverrideMap;
    }

    public Set<FilterDefinition> getSuppressedFilters() {
        return suppressedFilters;
    }

    public void setSuppressedFilters(Set<FilterDefinition> suppressedFilters) {
        this.suppressedFilters = suppressedFilters;
    }

    private long databaseTime = 0;

    public Map<AnalysisItem, Boolean> getDistinctFieldMap() {
        return distinctFieldMap;
    }

    public void setDistinctFieldMap(Map<AnalysisItem, Boolean> distinctFieldMap) {
        this.distinctFieldMap = distinctFieldMap;
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public Map<FilterDefinition, AdvancedFilterProperties> getFilterPropertiesMap() {
        return filterPropertiesMap;
    }

    public void setFilterPropertiesMap(Map<FilterDefinition, AdvancedFilterProperties> filterPropertiesMap) {
        this.filterPropertiesMap = filterPropertiesMap;
    }

    public boolean isNoDataOnNoJoin() {
        return noDataOnNoJoin;
    }

    public void setNoDataOnNoJoin(boolean noDataOnNoJoin) {
        this.noDataOnNoJoin = noDataOnNoJoin;
    }

    public boolean isNoLogging() {
        return noLogging;
    }

    public void setNoLogging(boolean noLogging) {
        this.noLogging = noLogging;
    }

    public void addDatabaseTime(long time) {
        databaseTime += time;
    }

    public long getDatabaseTime() {
        return databaseTime;
    }

    public List<AnalysisItem> getAdditionalAnalysisItems() {
        return additionalAnalysisItems;
    }

    public void setAdditionalAnalysisItems(List<AnalysisItem> additionalAnalysisItems) {
        this.additionalAnalysisItems = additionalAnalysisItems;
    }

    public List<AddonReport> getAddonReports() {
        return addonReports;
    }

    public void setAddonReports(List<AddonReport> addonReports) {
        this.addonReports = addonReports;
    }

    public void addPostProcessJoin(AnalysisItem analysisItem) {
        postProcessJoins.add(analysisItem);
    }

    public Set<AnalysisItem> getPostProcessJoins() {
        return postProcessJoins;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Map<AnalysisItem, AnalysisItem> getCurrencyMap() {
        return currencyMap;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public void assignDerived(AnalysisItem analysisItem, String pipeline) {
        derivedFieldAssignmentMap.put(analysisItem, pipeline);
    }

    public String getDerived(AnalysisItem analysisItem) {
        String pipeline = derivedFieldAssignmentMap.get(analysisItem);
        if (pipeline == null) {
            if (analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
                pipeline = ((AnalysisCalculation) analysisItem).getPipelineName();
            } else if (analysisItem.hasType(AnalysisItemTypes.DERIVED_DIMENSION)) {
                pipeline = ((DerivedAnalysisDimension) analysisItem).getPipelineName();
            } else if (analysisItem.hasType(AnalysisItemTypes.DERIVED_DATE)) {
                pipeline = ((DerivedAnalysisDateDimension) analysisItem).getPipelineName();
            }
            derivedFieldAssignmentMap.put(analysisItem, pipeline);
        }
        return pipeline;
    }

    public void assign(AnalysisItem analysisItem, String pipeline) {
        Set<String> pipelines = pipelineAssignmentMap.get(analysisItem);
        if (pipelines == null) {
            pipelines = new HashSet<String>();
            pipelineAssignmentMap.put(analysisItem, pipelines);
        }
        pipelines.add(pipeline);
    }

    public Set<String> getPipelines(AnalysisItem analysisItem) {
        Set<String> pipelines = pipelineAssignmentMap.get(analysisItem);
        if (pipelines == null) {
            pipelines = new HashSet<String>();
            pipelineAssignmentMap.put(analysisItem, pipelines);
        }
        return pipelines;
    }

    public List<IntentionSuggestion> getSuggestions() {
        return suggestions;
    }

    public Map<UniqueKey, AnalysisItem> getUniqueIteMap() {
        return uniqueIteMap;
    }

    public void setUniqueIteMap(Map<UniqueKey, AnalysisItem> uniqueIteMap) {
        this.uniqueIteMap = uniqueIteMap;
    }

    public Map<String, UniqueKey> getFieldToUniqueMap() {
        return fieldToUniqueMap;
    }

    public void setFieldToUniqueMap(Map<String, UniqueKey> fieldToUniqueMap) {
        this.fieldToUniqueMap = fieldToUniqueMap;
    }

    public Collection<AnalysisItem> getReportItems() {
        return reportItems;
    }

    public void setReportItems(Collection<AnalysisItem> reportItems) {
        this.reportItems = reportItems;
    }

    public boolean isTraverseAllJoins() {
        return traverseAllJoins;
    }

    public void setTraverseAllJoins(boolean traverseAllJoins) {
        this.traverseAllJoins = traverseAllJoins;
    }

    public boolean isOptimized() {
        return optimized;
    }

    public void setOptimized(boolean optimized) {
        this.optimized = optimized;
    }

    public List<JoinOverride> getJoinOverrides() {
        return joinOverrides;
    }

    public void setJoinOverrides(List<JoinOverride> joinOverrides) {
        this.joinOverrides = joinOverrides;
    }

    public boolean isGmtData() {
        return gmtData;
    }

    public void setGmtData(boolean gmtData) {
        this.gmtData = gmtData;
    }

    public boolean isAggregateQuery() {
        return aggregateQuery;
    }

    public void setAggregateQuery(boolean aggregateQuery) {
        this.aggregateQuery = aggregateQuery;
    }

    public List<AnalysisItemOverride> getHierarchyOverrides() {
        return hierarchyOverrides;
    }

    public void setHierarchyOverrides(List<AnalysisItemOverride> hierarchyOverrides) {
        this.hierarchyOverrides = hierarchyOverrides;
    }

    public boolean isNoCache() {
        return noCache;
    }

    public void setNoCache(boolean noCache) {
        this.noCache = noCache;
    }

    public boolean isRefreshAllSources() {
        return refreshAllSources;
    }

    public void setRefreshAllSources(boolean refreshAllSources) {
        this.refreshAllSources = refreshAllSources;
    }

    public int getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(int utcOffset) {
        this.utcOffset = utcOffset;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    private Map<String, Pipeline> pipelineMap = new HashMap<String, Pipeline>();

    private Map<String, List<AnalysisItem>> pipelineFieldMap = new HashMap<String, List<AnalysisItem>>();

    public Pipeline findPipeline(String name) {
        return pipelineMap.get(name);
    }

    public Map<String, Pipeline> getPipelineMap() {
        return pipelineMap;
    }

    public void putPipeline(String name, Pipeline pipeline) {
        pipelineMap.put(name, pipeline);
    }

    private transient List<String> intermediatePipelines = new ArrayList<String>();

    public List<String> getIntermediatePipelines() {
        return intermediatePipelines;
    }

    public void setIntermediatePipelines(List<String> intermediatePipelines) {
        this.intermediatePipelines = intermediatePipelines;
    }

    private Map<String, String> pipelineAssignments = new HashMap<String, String>();

    public void assignFieldToPipeline(String field, String pipelineName) {
        pipelineAssignments.put(field, pipelineName);
    }

    public String getPipelineNameForField(String field) {
        return pipelineAssignments.get(field);
    }

    public List<AnalysisItem> getFieldsForPipeline(String name) {
        return pipelineFieldMap.get(name);
    }

    private Map<String, String> filterAssignments = new HashMap<String, String>();

    public String getPipelineNameForFilter(String filter) {
        return filterAssignments.get(filter);
    }

    public void assignFilterToPipeline(String field, String pipelineName) {
        filterAssignments.put(field, pipelineName);
    }

    public void pipelineAssign(AnalysisItem analysisItem) {
        String name = getPipelineNameForField(analysisItem.toDisplay());
        if (name != null) {
            derivedFieldAssignmentMap.put(analysisItem, name);
            List<AnalysisItem> fields = pipelineFieldMap.get(name);
            if (fields == null) {
                fields = new ArrayList<AnalysisItem>();
                pipelineFieldMap.put(name, fields);
            }
            assign(analysisItem, name);
            fields.add(analysisItem);
        }
        if (analysisItem.getFilters() != null) {
            for (FilterDefinition filter : analysisItem.getFilters()) {
                if (filter.getFilterName() != null && !"".equals(filter.getFilterName())) {
                    name = getPipelineNameForField(filter.getFilterName());
                    filter.setPipelineName(name);
                }
            }
        }
    }

    public void pipelineAssign(FilterDefinition filter) {
        if (filter.getFilterName() != null && !"".equals(filter.getFilterName())) {
            String name = getPipelineNameForFilter(filter.getFilterName());
            if (name != null) {
                filter.setPipelineName(name);
            }
        }
    }

    public List<IntentionSuggestion> generateSuggestions() {
        List<IntentionSuggestion> suggestionList = new ArrayList<IntentionSuggestion>();
        if (!isAggregationRowChanged()) {
            /*suggestionList.add(new IntentionSuggestion("turn off aggregate query if possible", "We recommend turning off aggregate query if possible to speed up the report load time.",
                    IntentionSuggestion.SCOPE_REPORT, IntentionSuggestion.TURN_OFF_AGGREGATE_QUERY, IntentionSuggestion.YOU_SHOULD_DO_THIS));*/
        }
        if (suggestions != null) {
            suggestionList.addAll(suggestions);
        }
        return suggestionList;
    }
}
