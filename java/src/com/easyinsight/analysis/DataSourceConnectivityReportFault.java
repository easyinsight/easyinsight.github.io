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
    private String debug;

    public DataSourceConnectivityReportFault(String message, FeedDefinition dataSource) {
        this.dataSource = dataSource;
        this.message = message;
    }

    public DataSourceConnectivityReportFault(String message, FeedDefinition dataSource, String debug) {
        this.dataSource = dataSource;
        this.message = message;
        this.debug = debug;
    }

    public DataSourceConnectivityReportFault() {
    }

    public String getDebug() {
        return debug;
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

    @Override
    public String toString() {
        if (debug != null) {
            return message + " - " + debug;
        } else {
            return message;
        }
    }
}
