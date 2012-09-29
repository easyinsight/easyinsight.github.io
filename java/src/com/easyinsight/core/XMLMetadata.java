package com.easyinsight.core;

import com.easyinsight.database.EIConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: 5/16/12
 * Time: 9:10 AM
 */
public class XMLMetadata {
    private EIConnection conn;

    public EIConnection getConn() {
        return conn;
    }

    public void setConn(EIConnection conn) {
        this.conn = conn;
    }

    public String value(String string) {
        if (string == null) {
            return "";
        } else {
            return string;
        }
    }

    public String urlKeyForDashboardID(long dashboardID) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT URL_KEY FROM DASHBOARD WHERE DASHBOARD_ID = ?");
            stmt.setLong(1, dashboardID);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            String urlKey = rs.getString(1);
            stmt.close();
            return urlKey;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String urlKeyForReportID(long reportID) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT URL_KEY FROM ANALYSIS WHERE ANALYSIS_ID = ?");
            stmt.setLong(1, reportID);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            String urlKey = rs.getString(1);
            stmt.close();
            return urlKey;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String urlKeyForDataSourceID(long dataSourceID) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT API_KEY FROM DATA_FEED WHERE DATA_FEED_ID = ?");
            stmt.setLong(1, dataSourceID);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            String urlKey = rs.getString(1);
            stmt.close();
            return urlKey;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
