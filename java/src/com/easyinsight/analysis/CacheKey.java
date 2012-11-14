package com.easyinsight.analysis;

import java.io.Serializable;
import java.util.List;

/**
* User: jamesboe
* Date: 11/13/12
* Time: 3:57 PM
*/
class CacheKey implements Serializable {
    private long reportID;
    private List<String> customFilters;

    CacheKey(long reportID, List<String> customFilters) {
        this.reportID = reportID;
        this.customFilters = customFilters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CacheKey cacheKey = (CacheKey) o;

        if (reportID != cacheKey.reportID) return false;
        if (!customFilters.equals(cacheKey.customFilters)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (reportID ^ (reportID >>> 32));
        result = 31 * result + customFilters.hashCode();
        return result;
    }
}
