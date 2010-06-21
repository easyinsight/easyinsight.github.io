package com.easyinsight.analysis;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
* User: jamesboe
* Date: Jun 21, 2010
* Time: 4:13:05 PM
*/
class EmbeddedCacheKey implements Serializable {

    private Collection<FilterDefinition> filters;
    private List<FilterDefinition> drillThroughFilters;
    private long reportID;

    EmbeddedCacheKey(Collection<FilterDefinition> filters, long reportID, List<FilterDefinition> drillThroughFilters) {
        this.filters = filters;
        this.reportID = reportID;
        this.drillThroughFilters = drillThroughFilters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmbeddedCacheKey that = (EmbeddedCacheKey) o;

        if (reportID != that.reportID) return false;
        if (drillThroughFilters != null ? !drillThroughFilters.equals(that.drillThroughFilters) : that.drillThroughFilters != null)
            return false;
        if (filters != null ? !filters.equals(that.filters) : that.filters != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = filters != null ? filters.hashCode() : 0;
        result = 31 * result + (drillThroughFilters != null ? drillThroughFilters.hashCode() : 0);
        result = 31 * result + (int) (reportID ^ (reportID >>> 32));
        return result;
    }
}
