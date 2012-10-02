package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: Alan
 * Date: 9/7/12
 * Time: 9:23 AM
 */
public class DatabaseConnection extends ServerDataSourceDefinition {

    public String getRefreshKey() {
        return refreshKey;
    }

    public void setRefreshKey(String refreshKey) {
        this.refreshKey = refreshKey;
    }

    public String getRefreshUrl() {
        return refreshUrl;
    }

    public void setRefreshUrl(String url) {
        this.refreshUrl = url;
    }

    private String refreshKey;
    private String refreshUrl;

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }

    public FeedType getFeedType() {
        return FeedType.DATABASE_CONNECTION;
    }

    @Override
    public int getDataSourceType() {
        if (getRefreshUrl() != null && !"".equals(getRefreshUrl())) {
            return DataSourceInfo.STORED_PULL;
        } else {
            return DataSourceInfo.STORED_PUSH;
        }
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        return getFields();
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {

        if (getRefreshUrl() != null) {
            // call refresh.jsp on client
            HttpClient client = new HttpClient();

            String refreshEndpoint;
            if (callDataID == null) {
                refreshEndpoint = getRefreshUrl() + "/refresh/refresh.jsp?refreshKey=" + getRefreshKey();
            } else {
                refreshEndpoint = getRefreshUrl() + "/refresh/refresh.jsp?refreshKey=" + getRefreshKey() + "&callDataID=" + callDataID;
            }

            HttpMethod requestMethod = new GetMethod(refreshEndpoint);
            try {
                client.executeMethod(requestMethod);
            } catch (Exception e) {
                throw new ReportException(new DataSourceConnectivityReportFault(e.getMessage(), this));
            }


        }
        return new DataSet();
    }

    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM DATABASE_DATA_SOURCE WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        deleteStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DATABASE_DATA_SOURCE (REFRESH_KEY, REFRESH_URL, DATA_SOURCE_ID) VALUES (?, ?, ?)");
        insertStmt.setString(1, getRefreshKey());
        insertStmt.setString(2, getRefreshUrl());
        insertStmt.setLong(3, getDataFeedID());
        insertStmt.execute();
        insertStmt.close();
    }

    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement stmt = conn.prepareStatement("SELECT REFRESH_KEY, REFRESH_URL FROM DATABASE_DATA_SOURCE WHERE " +
                "DATA_SOURCE_ID = ?");
        stmt.setLong(1, getDataFeedID());
        ResultSet dsRS = stmt.executeQuery();
        if (dsRS.next()) {
            refreshKey = dsRS.getString(1);
            refreshUrl = dsRS.getString(2);
        }
        stmt.close();
    }

    public boolean waitsOnServiceUtil() {
        return true;
    }
}
