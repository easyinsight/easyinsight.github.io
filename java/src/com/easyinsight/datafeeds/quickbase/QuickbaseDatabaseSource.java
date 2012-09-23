package com.easyinsight.datafeeds.quickbase;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.google.GoogleDataProvider;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.*;
import com.easyinsight.users.Account;
import nu.xom.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/15/10
 * Time: 3:52 PM
 */
public class QuickbaseDatabaseSource extends ServerDataSourceDefinition {

    private static final String REQUEST = "<qdbapi><ticket>{0}</ticket><apptoken>{1}</apptoken><clist>{2}</clist><fmt>structured</fmt><options>num-1000</options></qdbapi>";
    private static final String REQUEST_2 = "<qdbapi><ticket>{0}</ticket><apptoken>{1}</apptoken><clist>{2}</clist><fmt>structured</fmt><options>num-1000.skp-{3}</options></qdbapi>";

    private static final String REQUESTP = "<qdbapi><ticket>{0}</ticket><apptoken>{1}</apptoken><clist>{2}</clist><query>{3}</query><fmt>structured</fmt><options>num-1000</options></qdbapi>";
    private static final String REQUESTP_2 = "<qdbapi><ticket>{0}</ticket><apptoken>{1}</apptoken><clist>{2}</clist><query>{4}</query><fmt>structured</fmt><options>num-1000.skp-{3}</options></qdbapi>";

    private String databaseID;
    private boolean indexEnabled;
    private long weightsID;

    public boolean isIndexEnabled() {
        return indexEnabled;
    }

    public void setIndexEnabled(boolean indexEnabled) {
        this.indexEnabled = indexEnabled;
    }

    public long getWeightsID() {
        return weightsID;
    }

    public void setWeightsID(long weightsID) {
        this.weightsID = weightsID;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();

        QuickbaseCompositeSource quickbaseCompositeSource = (QuickbaseCompositeSource) parentDefinition;

        if (quickbaseCompositeSource.getHost() != null && quickbaseCompositeSource.getHost().contains("rcixxx")) {
            String sessionTicket = quickbaseCompositeSource.getSessionTicket();
            String applicationToken = quickbaseCompositeSource.getApplicationToken();
            String host = quickbaseCompositeSource.getHost();

            try {
                GoogleDataProvider.getSchema(sessionTicket, applicationToken, databaseID, fields, new ArrayList<com.easyinsight.datafeeds.google.Connection>(), host, keys);
            } catch (Exception e) {
                if (quickbaseCompositeSource.isPreserveCredentials()) {
                    try {
                        quickbaseCompositeSource.exchangeTokens((EIConnection) conn, null, null);
                    } catch (Exception e1) {
                        throw new RuntimeException(e1);
                    }
                    try {
                        GoogleDataProvider.getSchema(quickbaseCompositeSource.getSessionTicket(), applicationToken, databaseID, fields, new ArrayList<com.easyinsight.datafeeds.google.Connection>(), host, keys);
                    } catch (Exception e1) {
                        throw new ReportException(new DataSourceConnectivityReportFault(e1.getMessage(), parentDefinition));
                    }
                }
            }
        }
        return fields;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM QUICKBASE_DATA_SOURCE where data_source_id = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO QUICKBASE_DATA_SOURCE (DATA_SOURCE_ID, DATABASE_ID, support_index, weights_id) " +
                "VALUES (?, ?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, databaseID);
        insertStmt.setBoolean(3, indexEnabled);
        insertStmt.setLong(4, weightsID);
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT database_id, support_index, weights_id FROM " +
                "QUICKBASE_DATA_SOURCE where data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        databaseID = rs.getString(1);
        indexEnabled = rs.getBoolean(2);
        weightsID = rs.getLong(3);
        queryStmt.close();
    }

    protected void clearData(DataStorage dataStorage) throws SQLException {
        dataStorage.truncate();
    }

    public String getDatabaseID() {
        return databaseID;
    }

    public void setDatabaseID(String databaseID) {
        this.databaseID = databaseID;
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        return new QuickbaseFeed(databaseID);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.QUICKBASE_CHILD;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PROFESSIONAL;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        QuickbaseCompositeSource quickbaseCompositeSource = (QuickbaseCompositeSource) parentDefinition;
        String sessionTicket = quickbaseCompositeSource.getSessionTicket();
        String applicationToken = quickbaseCompositeSource.getApplicationToken();
        String host = quickbaseCompositeSource.getHost();
        String fullPath = "https://" + host + "/db/" + databaseID;

        try {
            return normalRetrieval(IDataStorage, conn, quickbaseCompositeSource, sessionTicket, applicationToken, fullPath);
        } catch (ReportException e) {
            if (quickbaseCompositeSource.isPreserveCredentials()) {
                try {
                    quickbaseCompositeSource.exchangeTokens(conn, null, null);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
                return normalRetrieval(IDataStorage, conn, quickbaseCompositeSource, quickbaseCompositeSource.getSessionTicket(), applicationToken, fullPath);
            } else {
                throw e;
            }
        }
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return true;
    }



    /*@Override
    protected String getUpdateKeyName() {
        return "beutk2zd6.3";
    }*/

    /*public boolean hasNewData(Date lastRefreshDate, FeedDefinition parent, EIConnection conn) {
        QuickbaseCompositeSource quickbaseCompositeSource = (QuickbaseCompositeSource) parent;
        String fullPath = "https://" + quickbaseCompositeSource.getHost() + "/db/" + databaseID;
        if (databaseID.equals("beutk2zd6")) {
            try {
                return checkForNewData(fullPath, quickbaseCompositeSource.getSessionTicket(),
                        quickbaseCompositeSource.getApplicationToken(), lastRefreshDate, quickbaseCompositeSource);
            } catch (Exception e) {
                try {
                    quickbaseCompositeSource.exchangeTokens(conn, null, null);
                    return checkForNewData(fullPath, quickbaseCompositeSource.getSessionTicket(),
                        quickbaseCompositeSource.getApplicationToken(), lastRefreshDate, quickbaseCompositeSource);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            }
        } else {
            return false;
        }
    }*/

    /*private boolean checkForNewData(String fullPath, String sessionTicket, String applicationToken, Date lastRefreshDate, QuickbaseCompositeSource quickbaseCompositeSource) throws IOException, ParsingException {
        HttpPost httpRequest = new HttpPost(fullPath);
        httpRequest.setHeader("Accept", "application/xml");
        httpRequest.setHeader("Content-Type", "application/xml");
        httpRequest.setHeader("QUICKBASE-ACTION", "API_DoQuery");
        BasicHttpEntity entity = new BasicHttpEntity();
        String columns = "3";
        System.out.println("looking for after " + lastRefreshDate);
        String query = ("{'2'.AF.'" + lastRefreshDate.getTime() + "'}");
        String requestBody = MessageFormat.format(REQUESTP, sessionTicket, applicationToken, columns, query);
        System.out.println(requestBody);
        byte[] contentBytes = requestBody.getBytes();
        entity.setContent(new ByteArrayInputStream(contentBytes));
        entity.setContentLength(contentBytes.length);
        httpRequest.setEntity(entity);
        HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String string = client.execute(httpRequest, responseHandler);
        Document doc = new Builder().build(new ByteArrayInputStream(string.getBytes("UTF-8")));
        Nodes errors = doc.query("/qdbapi/errcode/text()");
        if (errors.size() > 0) {
            Node error = errors.get(0);
            if (!"0".equals(error.getValue())) {
                String errorDetail = doc.query("/qdbapi/errdetail/text()").get(0).getValue();
                throw new ReportException(new DataSourceConnectivityReportFault(errorDetail, quickbaseCompositeSource));
            }
        }
        Nodes records = doc.query("/qdbapi/table/records/record");
        System.out.println("checking...");
        if (records.size() > 0) {
            System.out.println("found new record");
            return true;
        }
        System.out.println("no new records");
        return false;
    }*/

    private DataSet normalRetrieval(IDataStorage IDataStorage, EIConnection conn, QuickbaseCompositeSource quickbaseCompositeSource, String sessionTicket, String applicationToken, String fullPath) {
        HttpPost httpRequest = new HttpPost(fullPath);
        httpRequest.setHeader("Accept", "application/xml");
        httpRequest.setHeader("Content-Type", "application/xml");
        httpRequest.setHeader("QUICKBASE-ACTION", "API_DoQuery");
        BasicHttpEntity entity = new BasicHttpEntity();
        StringBuilder columnBuilder = new StringBuilder();
        Map<String, Collection<AnalysisItem>> map = new HashMap<String, Collection<AnalysisItem>>();
        for (AnalysisItem analysisItem : getFields()) {
            if (analysisItem.getKey().indexed()) {
                String fieldID = analysisItem.getKey().toBaseKey().toKeyString().split("\\.")[1];
                Collection<AnalysisItem> items = map.get(fieldID);
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    map.put(fieldID, items);
                }
                items.add(analysisItem);
                columnBuilder.append(fieldID).append(".");
            }
        }
        DataSet dataSet = new DataSet();
        if (map.size() == 0) {
            return dataSet;
        }
        int count;
        int masterCount = 0;

        try {
            do {
                count = 0;
                String requestBody;
                if (masterCount == 0) {
                    requestBody = MessageFormat.format(REQUEST, sessionTicket, applicationToken, columnBuilder.toString());
                } else {
                    requestBody = MessageFormat.format(REQUEST_2, sessionTicket, applicationToken, columnBuilder.toString(), String.valueOf(masterCount));
                }
                //System.out.println(requestBody);
                byte[] contentBytes = requestBody.getBytes();
                entity.setContent(new ByteArrayInputStream(contentBytes));
                entity.setContentLength(contentBytes.length);
                httpRequest.setEntity(entity);
                HttpClient client = new DefaultHttpClient();
                ResponseHandler<String> responseHandler = new BasicResponseHandler();

                String string = client.execute(httpRequest, responseHandler);
                Document doc = new Builder().build(new ByteArrayInputStream(string.getBytes("UTF-8")));

                Nodes errors = doc.query("/qdbapi/errcode/text()");
                if (errors.size() > 0) {
                    Node error = errors.get(0);
                    if (!"0".equals(error.getValue())) {
                        String errorDetail = doc.query("/qdbapi/errdetail/text()").get(0).getValue();
                        throw new ReportException(new DataSourceConnectivityReportFault(errorDetail, quickbaseCompositeSource));
                    }
                }

                Nodes records = doc.query("/qdbapi/table/records/record");
                for (int i = 0; i < records.size(); i++) {
                    Element record = (Element) records.get(i);
                    IRow row = dataSet.createRow();
                    count++;
                    masterCount++;
                    Elements childElements = record.getChildElements();
                    for (int j = 0; j < childElements.size(); j++) {
                        Element childElement = childElements.get(j);
                        if (childElement.getLocalName().equals("f")) {
                            String fieldID = childElement.getAttribute("id").getValue();
                            Collection<AnalysisItem> items = map.get(fieldID);
                            for (AnalysisItem analysisItem : items) {    
                                String value = childElement.getValue();
                                if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION) && !"".equals(value)) {
                                    row.addValue(analysisItem.getKey(), new Date(Long.parseLong(value)));
                                } else {
                                    row.addValue(analysisItem.getKey(), value);
                                }
                            }
                        }
                    }
                }
                IDataStorage.insertData(dataSet);
                dataSet = new DataSet();
            } while (count == 1000);
            return null;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}