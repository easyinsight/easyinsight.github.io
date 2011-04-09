package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 4/8/11
 * Time: 5:26 PM
 */
public class SalesforceSObjectSource extends ServerDataSourceDefinition {

    private String sobjectName;

    public String getSobjectName() {
        return sobjectName;
    }

    public void setSobjectName(String sobjectName) {
        this.sobjectName = sobjectName;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        try {
            List<String> keys = new ArrayList<String>();
            SalesforceBaseDataSource salesforceBaseDataSource = (SalesforceBaseDataSource) parentDefinition;
            HttpGet httpRequest = new HttpGet(salesforceBaseDataSource.getInstanceName() + "/services/data/v20.0/sobjects/" + sobjectName + "/describe/");
            httpRequest.setHeader("Accept", "application/xml");
            httpRequest.setHeader("Content-Type", "application/xml");
            httpRequest.setHeader("Authorization", "OAuth " + salesforceBaseDataSource.getAccessToken());


            org.apache.http.client.HttpClient cc = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String string = cc.execute(httpRequest, responseHandler);
            System.out.println(string);
            Builder builder = new Builder();
            Document doc = builder.build(new ByteArrayInputStream(string.getBytes()));
            Nodes fieldsNodes = doc.query("/" + sobjectName + "/fields");
            for (int i = 0; i < fieldsNodes.size(); i++) {
                Node fieldNode = fieldsNodes.get(i);
                String name = fieldNode.query("name/text()").get(0).getValue();
                keys.add(name);
            }
            return keys;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        return new SalesforceFeed(sobjectName);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        try {
            List<AnalysisItem> items = new ArrayList<AnalysisItem>();
            SalesforceBaseDataSource salesforceBaseDataSource = (SalesforceBaseDataSource) parentDefinition;
            HttpGet httpRequest = new HttpGet(salesforceBaseDataSource.getInstanceName() + "/services/data/v20.0/sobjects/" + sobjectName + "/describe/");
            httpRequest.setHeader("Accept", "application/xml");
            httpRequest.setHeader("Content-Type", "application/xml");
            httpRequest.setHeader("Authorization", "OAuth " + salesforceBaseDataSource.getAccessToken());


            org.apache.http.client.HttpClient cc = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String string = cc.execute(httpRequest, responseHandler);
            //System.out.println(string);
            Builder builder = new Builder();
            Document doc = builder.build(new ByteArrayInputStream(string.getBytes()));
            Nodes fieldsNodes = doc.query("/" + sobjectName + "/fields");
            System.out.println(sobjectName);
            for (int i = 0; i < fieldsNodes.size(); i++) {
                Node fieldNode = fieldsNodes.get(i);
                String fieldName = fieldNode.query("name/text()").get(0).getValue();
                System.out.println("\t" + fieldName);
                String friendlyName = fieldNode.query("label/text()").get(0).getValue();
                String type = fieldNode.query("type/text()").get(0).getValue().toUpperCase();
                if("BOOLEAN".equals(type) ||
                        "STRING".equals(type) ||
                        "TEXTAREA".equals(type) ||
                        "PHONE".equals(type) ||
                        "URL".equals(type) ||
                        "PICKLIST".equals(type) ||
                        "ID".equals(type) ||
                        "REFERENCE".equals(type)) {
                    items.add(new AnalysisDimension(keys.get(fieldName), friendlyName));
                }
                else if("DOUBLE".equals(type) || "INT".equals(type)) {
                    items.add(new AnalysisMeasure(keys.get(fieldName), friendlyName, AggregationTypes.SUM));
                } else if ("CURRENCY".equals(type)) {
                    AnalysisMeasure analysisMeasure = new AnalysisMeasure(keys.get(fieldName), friendlyName, AggregationTypes.SUM);
                    FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
                    formattingConfiguration.setFormattingType(FormattingConfiguration.CURRENCY);
                    analysisMeasure.setFormattingConfiguration(formattingConfiguration);
                    items.add(analysisMeasure);
                } else if ("PERCENT".equals(type)) {
                    AnalysisMeasure analysisMeasure = new AnalysisMeasure(keys.get(fieldName), friendlyName, AggregationTypes.AVERAGE);
                    FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
                    formattingConfiguration.setFormattingType(FormattingConfiguration.PERCENTAGE);
                    analysisMeasure.setFormattingConfiguration(formattingConfiguration);
                    items.add(analysisMeasure);
                } else if ("DATE".equals(type)) {
                    items.add(new AnalysisDateDimension(keys.get(fieldName), friendlyName, AnalysisDateDimension.DAY_LEVEL));
                } else if ("DATETIME".equals(type)) {
                    AnalysisDateDimension dateDimension = new AnalysisDateDimension(keys.get(fieldName), friendlyName, AnalysisDateDimension.DAY_LEVEL);
                    dateDimension.setCustomDateFormat("yyyy-MM-dd'T'HH:mm:SS.sss'Z'");
                    items.add(dateDimension);
                } else {
                    System.out.println("** NO CLUE HOW TO HANDLE " + type);
                }
            }
            return items;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM SALESFORCE_SUB_DEFINITION WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO SALESFORCE_SUB_DEFINITION (DATA_SOURCE_ID, SOBJECT_NAME) VALUES (?, ?)");
        saveStmt.setLong(1, getDataFeedID());
        saveStmt.setString(2, sobjectName);
        saveStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT SOBJECT_NAME FROM SALESFORCE_SUB_DEFINITION WHERE DATA_SOURCE_ID = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            sobjectName = rs.getString(1);
        }
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SALESFORCE_SUB;
    }
}
