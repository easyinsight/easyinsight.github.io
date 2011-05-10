package com.easyinsight.analysis;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
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
}
