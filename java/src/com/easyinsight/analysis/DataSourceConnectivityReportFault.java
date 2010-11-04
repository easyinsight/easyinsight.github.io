package com.easyinsight.analysis;

import com.easyinsight.datafeeds.FeedDefinition;

/**
 * User: jamesboe
 * Date: Nov 1, 2010
 * Time: 12:59:51 PM
 */
public class DataSourceConnectivityReportFault extends ReportFault {
    private FeedDefinition dataSource;
    private String message;

    public DataSourceConnectivityReportFault(String message, FeedDefinition dataSource) {
        this.dataSource = dataSource;
        this.message = message;
    }

    public DataSourceConnectivityReportFault() {
    }

    public FeedDefinition getDataSource() {
        return dataSource;
    }

    public void setDataSource(FeedDefinition dataSource) {
        this.dataSource = dataSource;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
