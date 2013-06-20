package com.easyinsight.dashboard;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.core.InsightDescriptor;

import java.util.List;

/**
 * User: jamesboe
 * Date: 5/14/13
 * Time: 7:50 AM
 */
public class NewDashboardMetadata {
    private DataSourceInfo dataSourceInfo;
    private List<InsightDescriptor> availableReports;

    public DataSourceInfo getDataSourceInfo() {
        return dataSourceInfo;
    }

    public void setDataSourceInfo(DataSourceInfo dataSourceInfo) {
        this.dataSourceInfo = dataSourceInfo;
    }

    public List<InsightDescriptor> getAvailableReports() {
        return availableReports;
    }

    public void setAvailableReports(List<InsightDescriptor> availableReports) {
        this.availableReports = availableReports;
    }
}
