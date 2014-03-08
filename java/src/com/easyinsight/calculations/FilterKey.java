package com.easyinsight.calculations;

/**
 * User: jamesboe
 * Date: 3/7/14
 * Time: 9:00 AM
 */
public class FilterKey {
    private String filterLabel;
    private String source;

    public FilterKey(String filterLabel, String source) {
        this.filterLabel = filterLabel;
        this.source = source;
    }

    public String getFilterLabel() {
        return filterLabel;
    }

    public String getSource() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterKey filterKey = (FilterKey) o;

        if (!filterLabel.equals(filterKey.filterLabel)) return false;
        if (!source.equals(filterKey.source)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = filterLabel.hashCode();
        result = 31 * result + source.hashCode();
        return result;
    }
}

