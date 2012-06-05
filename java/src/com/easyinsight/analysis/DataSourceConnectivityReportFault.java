package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.logging.LogClass;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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

    @Override
    public String toHTML() {
        String fullURL = "<div class=\"span12\" style=\"text-align:center\" id=\"reportErrorDiv\">\n" +
                "We ran into a configuration problem with the data source in trying to retrieve data for this report. Please log into the full interface and try the same operation in order to resolve the configuration problem."+
                "</div>";
        System.out.println(fullURL);
        return fullURL;
    }
}
