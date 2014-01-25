package com.easyinsight.analysis;

import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.tag.Tag;

import java.util.List;

/**
 * User: jamesboe
 * Date: 11/18/13
 * Time: 4:18 PM
 */
public class ReportResults {
    private List<InsightDescriptor> reports;
    private List<DashboardDescriptor> dashboards;
    private List<Tag> reportTags;
    private List<DataSourceDescriptor> dataSources;

    public ReportResults() {
    }

    public ReportResults(List<InsightDescriptor> reports, List<Tag> reportTags) {
        this.reports = reports;
        this.reportTags = reportTags;
    }

    public List<DashboardDescriptor> getDashboards() {
        return dashboards;
    }

    public void setDashboards(List<DashboardDescriptor> dashboards) {
        this.dashboards = dashboards;
    }

    public List<DataSourceDescriptor> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSourceDescriptor> dataSources) {
        this.dataSources = dataSources;
    }

    public List<InsightDescriptor> getReports() {
        return reports;
    }

    public void setReports(List<InsightDescriptor> reports) {
        this.reports = reports;
    }

    public List<Tag> getReportTags() {
        return reportTags;
    }

    public void setReportTags(List<Tag> reportTags) {
        this.reportTags = reportTags;
    }
}
