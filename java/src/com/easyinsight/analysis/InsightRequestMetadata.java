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
    private List<HierarchyOverride> hierarchyOverrides = new ArrayList<HierarchyOverride>();
    private boolean aggregateQuery = true;

    public boolean isAggregateQuery() {
        return aggregateQuery;
    }

    public void setAggregateQuery(boolean aggregateQuery) {
        this.aggregateQuery = aggregateQuery;
    }

    public List<HierarchyOverride> getHierarchyOverrides() {
        return hierarchyOverrides;
    }

    public void setHierarchyOverrides(List<HierarchyOverride> hierarchyOverrides) {
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
