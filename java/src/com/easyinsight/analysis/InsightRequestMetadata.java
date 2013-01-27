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
    private Collection<AnalysisItem> reportItems;
    private boolean lookupTableAggregate;
    private transient Map<Long, AnalysisItem> uniqueIteMap = new HashMap<Long, AnalysisItem>();
    private transient Map<String, Long> fieldToUniqueMap = new HashMap<String, Long>();
    private transient Map<AnalysisItem, Set<String>> pipelineAssignmentMap = new HashMap<AnalysisItem, Set<String>>();
    private transient Map<AnalysisItem, String> derivedFieldAssignmentMap = new HashMap<AnalysisItem, String>();

    private transient List<IntentionSuggestion> suggestions = new ArrayList<IntentionSuggestion>();

    private transient String targetCurrency;
    private transient Map<AnalysisItem, AnalysisItem> currencyMap = new HashMap<AnalysisItem, AnalysisItem>();
    private transient Set<AnalysisItem> postProcessJoins = new HashSet<AnalysisItem>();
    private String ip;

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

    public Map<Long, AnalysisItem> getUniqueIteMap() {
        return uniqueIteMap;
    }

    public void setUniqueIteMap(Map<Long, AnalysisItem> uniqueIteMap) {
        this.uniqueIteMap = uniqueIteMap;
    }

    public Map<String, Long> getFieldToUniqueMap() {
        return fieldToUniqueMap;
    }

    public void setFieldToUniqueMap(Map<String, Long> fieldToUniqueMap) {
        this.fieldToUniqueMap = fieldToUniqueMap;
    }

    public boolean isLookupTableAggregate() {
        return lookupTableAggregate;
    }

    public void setLookupTableAggregate(boolean lookupTableAggregate) {
        this.lookupTableAggregate = lookupTableAggregate;
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
}
