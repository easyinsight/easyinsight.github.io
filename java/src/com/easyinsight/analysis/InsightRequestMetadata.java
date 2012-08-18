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

    private transient List<IntentionSuggestion> suggestions = new ArrayList<IntentionSuggestion>();

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

    public void putPipeline(String name, Pipeline pipeline) {
        pipelineMap.put(name, pipeline);
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

    public void pipelineAssign(AnalysisItem analysisItem) {
        String name = getPipelineNameForField(analysisItem.toDisplay());
        if (name != null) {
            analysisItem.setPipelineName(name);
            List<AnalysisItem> fields = pipelineFieldMap.get(name);
            if (fields == null) {
                fields = new ArrayList<AnalysisItem>();
                pipelineFieldMap.put(name, fields);
            }
            analysisItem.getPipelineSections().add(name);
            fields.add(analysisItem);
        }
    }
}
