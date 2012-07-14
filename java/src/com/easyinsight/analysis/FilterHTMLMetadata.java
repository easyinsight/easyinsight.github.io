package com.easyinsight.analysis;

import com.easyinsight.dashboard.Dashboard;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * User: jamesboe
 * Date: 6/12/12
 * Time: 12:42 PM
 */
public class FilterHTMLMetadata {

    private Dashboard dashboard;
    private HttpServletRequest request;
    private WSAnalysisDefinition report;
    private String onChange;
    private String filterKey = "reportbase";
    private List<FilterDefinition> filters;

    public String createOnChange() {
        if (dashboard != null) {
            if (onChange != null) {
                return getOnChange();
            } else {
                return "renderReport" + getReport().getAnalysisID();
            }
        }
        return "refreshReport";
    }

    public FilterHTMLMetadata(Dashboard dashboard, WSAnalysisDefinition report) {
        this.dashboard = dashboard;
        this.report = report;
    }

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }

    public FilterHTMLMetadata(WSAnalysisDefinition report) {
        this.report = report;
    }

    public FilterHTMLMetadata(Dashboard dashboard, HttpServletRequest request) {
        this.dashboard = dashboard;
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public String getOnChange() {
        return onChange;
    }

    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    public String getFilterKey() {
        return filterKey;
    }

    public void setFilterKey(String filterKey) {
        this.filterKey = filterKey;
    }

    public long getDataSourceID() {
        if (dashboard != null) {
            return dashboard.getDataSourceID();
        }
        return report.getDataFeedID();
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public WSAnalysisDefinition getReport() {
        return report;
    }
}
