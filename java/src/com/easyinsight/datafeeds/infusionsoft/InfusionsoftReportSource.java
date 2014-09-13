package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 9/12/14
 * Time: 1:32 PM
 */
public class InfusionsoftReportSource extends ServerDataSourceDefinition {
    private String reportID;
    private String userID;

    public InfusionsoftReportSource() {

    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        try {
            InfusionsoftCompositeSource infusionsoftCompositeSource = (InfusionsoftCompositeSource) new FeedStorage().getFeedDefinitionData(getParentSourceID(), conn);
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            String url = infusionsoftCompositeSource.getUrl() + ":443/api/xmlrpc";
            config.setServerURL(new URL(url));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            List parameters = new ArrayList();
            parameters.add(infusionsoftCompositeSource.getInfusionApiKey());
            parameters.add(reportID);
            parameters.add(1);
            Map<String, String> result = (Map<String, String>) client.execute("SearchService.getAllReportColumns", parameters);
            for (Map.Entry<String, String> entry : result.entrySet()) {
                fieldBuilder.addField(entry.getKey(), new AnalysisDimension(getFeedName() + " - " + entry.getKey()));
            }
            //System.out.println(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_REPORT;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            InfusionsoftCompositeSource infusionsoftCompositeSource = (InfusionsoftCompositeSource) new FeedStorage().getFeedDefinitionData(getParentSourceID(), conn);
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            String url = infusionsoftCompositeSource.getUrl() + ":443/api/xmlrpc";
            config.setServerURL(new URL(url));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            DataSet dataSet = new DataSet();

            //The secure encryption key
            boolean hasMoreResults;
            int page = 0;
            do {
                int count = 0;
                List parameters = new ArrayList();
                parameters.add(infusionsoftCompositeSource.getInfusionApiKey());
                parameters.add(reportID);
                parameters.add(1);
                parameters.add(page);
                Object[] results = (Object[]) client.execute("SearchService.getSavedSearchResultsAllFields", parameters);
                for (Object result : results) {
                    IRow row = dataSet.createRow();
                    Map resultMap = (Map) result;
                    for (Map.Entry<String, Key> entry : keys.entrySet()) {
                        Object value = resultMap.get(entry.getKey());
                        if (value != null) {
                            String string = value.toString();
                            row.addValue(entry.getValue(), string);
                        }
                    }
                    count++;
                }

                // todo: figure out what page size it returns

                hasMoreResults = count == 1000;
                page++;
            } while (hasMoreResults);
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM INFUSIONSOFT_REPORT_SOURCE WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO INFUSIONSOFT_REPORT_SOURCE (DATA_SOURCE_ID, REPORT_ID, USER_ID) VALUES (?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, reportID);
        insertStmt.setString(3, userID);
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT REPORT_ID, USER_ID FROM INFUSIONSOFT_REPORT_SOURCE WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            setReportID(rs.getString(1));
            setUserID(rs.getString(2));
        }
        queryStmt.close();
    }
}
