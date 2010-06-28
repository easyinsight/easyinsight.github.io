package com.easyinsight.datafeeds;

import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.InsightDescriptor;

import java.util.List;

/**
 * User: jamesboe
 * Date: Jun 26, 2010
 * Time: 7:45:13 PM
 */
public class HomeState {
    private List<DataSourceDescriptor> dataSources;
    private List<InsightDescriptor> reports;

    public HomeState() {
    }

    public HomeState(List<DataSourceDescriptor> dataSources, List<InsightDescriptor> reports) {
        this.dataSources = dataSources;
        this.reports = reports;
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
}
