package com.easyinsight.solutions;

import com.easyinsight.core.DataSourceDescriptor;

import java.util.List;

/**
 * User: jamesboe
 * Date: May 20, 2010
 * Time: 1:55:46 PM
 */
public class ReportTemplateInfo {
    private List<DataSourceDescriptor> dataSources;
    private long reportID;
    private long solutionID;

    public ReportTemplateInfo() {
    }

    public ReportTemplateInfo(List<DataSourceDescriptor> dataSources, long reportID, long solutionID) {
        this.dataSources = dataSources;
        this.reportID = reportID;
        this.solutionID = solutionID;
    }

    public long getSolutionID() {
        return solutionID;
    }

    public void setSolutionID(long solutionID) {
        this.solutionID = solutionID;
    }

    public List<DataSourceDescriptor> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSourceDescriptor> dataSources) {
        this.dataSources = dataSources;
    }

    public long getReportID() {
        return reportID;
    }

    public void setReportID(long reportID) {
        this.reportID = reportID;
    }
}
