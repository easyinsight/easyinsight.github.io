package com.easyinsight.datafeeds.quickbase;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.pipeline.CompositeReportPipeline;
import com.easyinsight.pipeline.Pipeline;
import com.easyinsight.storage.DataStorage;
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

    private static final String REQUEST = "<qdbapi><ticket>{0}</ticket><apptoken>{1}</apptoken><clist>{2}</clist><fmt>structured</fmt><options>num-20000</options></qdbapi>";
    private static final String REQUEST_2 = "<qdbapi><ticket>{0}</ticket><apptoken>{1}</apptoken><clist>{2}</clist><fmt>structured</fmt><options>num-20000.skp-{3}</options></qdbapi>";

    private String databaseID;

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM QUICKBASE_DATA_SOURCE where data_source_id = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO QUICKBASE_DATA_SOURCE (DATA_SOURCE_ID, DATABASE_ID) " +
                "VALUES (?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, databaseID);
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT database_id FROM " +
                "QUICKBASE_DATA_SOURCE where data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        databaseID = rs.getString(1);
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
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        QuickbaseCompositeSource quickbaseCompositeSource = (QuickbaseCompositeSource) parentDefinition;
        String sessionTicket = quickbaseCompositeSource.getSessionTicket();
        String applicationToken = quickbaseCompositeSource.getApplicationToken();
        String host = quickbaseCompositeSource.getHost();
        String fullPath = "https://" + host + "/db/" + databaseID;
        HttpPost httpRequest = new HttpPost(fullPath);
        httpRequest.setHeader("Accept", "application/xml");
        httpRequest.setHeader("Content-Type", "application/xml");
        httpRequest.setHeader("QUICKBASE-ACTION", "API_DoQuery");
        BasicHttpEntity entity = new BasicHttpEntity();
        StringBuilder columnBuilder = new StringBuilder();
        Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
        System.out.println("Data source " + getFeedName());
        for (AnalysisItem analysisItem : getFields()) {
            System.out.println("\tChecking " + analysisItem.toDisplay() + " with named key " + analysisItem.getKey().toBaseKey().getKeyID());
            if (analysisItem.getKey().indexed()) {
                String fieldID = analysisItem.getKey().toBaseKey().toKeyString().split("\\.")[1];
                map.put(fieldID, analysisItem);
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
                            AnalysisItem analysisItem = map.get(fieldID);
                            String value = childElement.getValue();
                            if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION) && !"".equals(value)) {
                                row.addValue(analysisItem.createAggregateKey(), new Date(Long.parseLong(value)));
                            } else {
                                row.addValue(analysisItem.createAggregateKey(), value);
                            }
                        }
                    }
                }
                dataStorage.insertData(dataSet);
                dataSet = new DataSet();
            } while (count == 20000);

            System.out.println("our set size = " + dataSet.getRows().size());
            Pipeline pipeline = new CompositeReportPipeline();
            WSListDefinition analysisDefinition = new WSListDefinition();
            analysisDefinition.setColumns(new ArrayList<AnalysisItem>(map.values()));
            pipeline.setup(analysisDefinition, FeedRegistry.instance().getFeed(getDataFeedID(), conn), new InsightRequestMetadata());
            dataSet = pipeline.toDataSet(dataSet);
            for (AnalysisItem analysisItem : map.values()) {
                dataSet.getDataSetKeys().replaceKey(analysisItem.createAggregateKey(), analysisItem.getKey());
            }
            System.out.println("after aggregation, size = " + dataSet.getRows().size());
            return null;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
