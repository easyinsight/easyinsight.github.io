package com.easyinsight.datafeeds;

import com.easyinsight.core.InsightDescriptor;

import java.util.List;

/**
 * User: jamesboe
 * Date: Apr 2, 2010
 * Time: 12:01:21 PM
 */
public class MultiReportInfo {
    private String dataSourceKey;
    private List<InsightDescriptor> reports;

    public MultiReportInfo(String dataSourceKey, List<InsightDescriptor> reports) {
        this.dataSourceKey = dataSourceKey;
        this.reports = reports;
    }

    public MultiReportInfo() {

    }

    public String getDataSourceKey() {
        return dataSourceKey;
    }

    public void setDataSourceKey(String dataSourceKey) {
        this.dataSourceKey = dataSourceKey;
    }

    public List<InsightDescriptor> getReports() {
        return reports;
    }

    public void setReports(List<InsightDescriptor> reports) {
        this.reports = reports;
    }
}
