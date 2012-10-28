package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.storage.IDataStorage;
import nu.xom.*;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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

    public void validateTableSetup(EIConnection conn) throws SQLException {
        DataStorage storage = DataStorage.writeConnection(this, conn);
        storage.commit();
        storage.closeConnection();
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
                    AnalysisDateDimension dateDim = new AnalysisDateDimension(keys.get(fieldName), friendlyName, AnalysisDateDimension.DAY_LEVEL);
                    dateDim.setDateOnlyField(true);
                    items.add(dateDim);
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
        clearStmt.close();
        PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO SALESFORCE_SUB_DEFINITION (DATA_SOURCE_ID, SOBJECT_NAME) VALUES (?, ?)");
        saveStmt.setLong(1, getDataFeedID());
        saveStmt.setString(2, sobjectName);
        saveStmt.execute();
        saveStmt.close();
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
        loadStmt.close();
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SALESFORCE_SUB;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        SalesforceBaseDataSource salesforceBaseDataSource = (SalesforceBaseDataSource) parentDefinition;
        DataSet dataSet;
        try {
            dataSet = argh(salesforceBaseDataSource);
        } catch (HttpResponseException e) {
            if ("Unauthorized".equals(e.getMessage())) {
                try {
                    salesforceBaseDataSource.refreshTokenInfo();
                    new FeedStorage().updateDataFeedConfiguration(salesforceBaseDataSource);
                    return argh(salesforceBaseDataSource);
                } catch (HttpResponseException hre) {
                    throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize Easy Insight to access your Salesforce data.", salesforceBaseDataSource));
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            } else {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataSet;
    }



    private DataSet argh(SalesforceBaseDataSource salesforceBaseDataSource) throws IOException, ParsingException {
        DataSet dataSet = new DataSet();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT+");
        Set<String> keys = new HashSet<String>();
        int fieldCount = 0;
        for (AnalysisItem analysisItem : getFields()) {
            if (analysisItem.getKey().indexed()) {
                String keyString = analysisItem.getKey().toKeyString();
                boolean alreadyThere = keys.add(keyString);
                if (alreadyThere) {
                    fieldCount++;
                    queryBuilder.append(keyString);
                    queryBuilder.append(",");
                }
            }
        }
        if (fieldCount == 0) {
            return dataSet;
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append("+from+");
        queryBuilder.append(sobjectName);

        String url = salesforceBaseDataSource.getInstanceName() + "/services/data/v20.0/query/?q=" + queryBuilder.toString();
        boolean moreData;
        do {

            HttpGet httpRequest = new HttpGet(url);
            httpRequest.setHeader("Accept", "application/xml");
            httpRequest.setHeader("Content-Type", "application/xml");
            httpRequest.setHeader("Authorization", "OAuth " + salesforceBaseDataSource.getAccessToken());


            org.apache.http.client.HttpClient cc = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            System.out.println(url);
            String string = cc.execute(httpRequest, responseHandler);

            Builder builder = new Builder();
            Document doc = builder.build(new ByteArrayInputStream(string.getBytes()));
            Nodes records = doc.query("/QueryResult/records");
            for (int i = 0; i < records.size(); i++) {
                IRow row = dataSet.createRow();
                Node record = records.get(i);
                for (AnalysisItem analysisItem : getFields()) {
                    if (analysisItem.getKey().indexed()) {
                        Nodes results = record.query(analysisItem.getKey().toKeyString() + "/text()");
                        if (results.size() > 0) {
                            String value = results.get(0).getValue();
                            row.addValue(analysisItem.getKey(), value);
                        }
                    }
                }
            }
            Nodes nextRecords = doc.query("/QueryResult/nextRecordsUrl/text()");
            if (nextRecords.size() == 1) {
                url = nextRecords.get(0).getValue();
                moreData = true;
            } else {
                moreData = false;
            }
        } while (moreData);
        return dataSet;
    }
}
