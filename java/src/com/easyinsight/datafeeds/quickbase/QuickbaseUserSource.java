package com.easyinsight.datafeeds.quickbase;

import com.easyinsight.analysis.*;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.google.GoogleDataProvider;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.storage.IDataStorage;
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
public class QuickbaseUserSource extends ServerDataSourceDefinition {

    private static final String REQUEST = "<qdbapi><ticket>{0}</ticket><apptoken>{1}</apptoken></qdbapi>";

    public static final String LAST_NAME = "Last Name";
    public static final String LAST_ACCESS = "Last Access";
    public static final String FIRST_NAME = "First Name";
    public static final String ROLE_NAME = "Role Name";
    public static final String ROLE_ID = "Role ID";
    public static final String NAME = "Name";
    public static final String USER_ID = "User ID";

    public QuickbaseUserSource() {
        setFeedName("Quickbase Users");
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        return Arrays.asList((AnalysisItem) new AnalysisDimension(keys.get(NAME)), new AnalysisDimension(keys.get(FIRST_NAME)),
                new AnalysisDimension(keys.get(LAST_NAME)), new AnalysisDateDimension(keys.get(LAST_ACCESS), true, AnalysisDateDimension.DAY_LEVEL),
                new AnalysisDimension(keys.get(ROLE_ID)), new AnalysisDimension(keys.get(ROLE_NAME)), new AnalysisDimension(keys.get(USER_ID)));
    }


    protected void clearData(DataStorage dataStorage) throws SQLException {
        dataStorage.truncate();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.QUICKBASE_USER_CHILD;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PROFESSIONAL;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(NAME, FIRST_NAME, LAST_NAME, LAST_ACCESS, ROLE_ID, ROLE_NAME, USER_ID);
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        QuickbaseCompositeSource quickbaseCompositeSource = (QuickbaseCompositeSource) parentDefinition;
        String sessionTicket = quickbaseCompositeSource.getSessionTicket();
        String applicationToken = quickbaseCompositeSource.getApplicationToken();
        String host = quickbaseCompositeSource.getHost();
        String dbid = quickbaseCompositeSource.getApplicationId();
        String fullPath = "https://" + host + "/db/" + dbid;

        try {
            return normalRetrieval(quickbaseCompositeSource, sessionTicket, applicationToken, fullPath, keys);
        } catch (ReportException e) {
            if (quickbaseCompositeSource.isPreserveCredentials()) {
                try {
                    quickbaseCompositeSource.exchangeTokens(conn, null, null);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
                return normalRetrieval(quickbaseCompositeSource, quickbaseCompositeSource.getSessionTicket(), applicationToken, fullPath, keys);
            } else {
                throw e;
            }
        }
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return true;
    }

    private DataSet normalRetrieval(QuickbaseCompositeSource quickbaseCompositeSource, String sessionTicket, String applicationToken, String fullPath, Map<String, Key> keys) {
        HttpPost httpRequest = new HttpPost(fullPath);
        httpRequest.setHeader("Accept", "application/xml");
        httpRequest.setHeader("Content-Type", "application/xml");
        httpRequest.setHeader("QUICKBASE-ACTION", "API_UserRoles");
        BasicHttpEntity entity = new BasicHttpEntity();
        StringBuilder columnBuilder = new StringBuilder();
        DataSet dataSet = new DataSet();
        try {
            String requestBody;
            requestBody = MessageFormat.format(REQUEST, sessionTicket, applicationToken, columnBuilder.toString());
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

            Nodes records = doc.query("/qdbapi/users/user");
            for (int i = 0; i < records.size(); i++) {
                Element record = (Element) records.get(i);
                IRow row = dataSet.createRow();
                addValue(row, NAME, keys, "./name/text()", record);
                addValue(row, FIRST_NAME, keys, "./firstName/text()", record);
                addValue(row, LAST_NAME, keys, "./lastName/text()", record);
                addValue(row, ROLE_ID, keys, "./roles/role/@id", record);
                addValue(row, ROLE_NAME, keys, "./roles/role/name/text()", record);
                addValue(row, USER_ID, keys, "./@id", record);

                try {
                    Nodes nodes =record.query("./lastAccess/text()");
                    if(nodes.size() > 0 && nodes.get(0).getValue().length() > 0) {
                        long curDate = Long.parseLong(nodes.get(0).getValue());
                        row.addValue(keys.get(LAST_ACCESS), new Date(curDate));
                    } else {
                        row.addValue(keys.get(LAST_ACCESS), new EmptyValue());
                    }
                } catch (NumberFormatException nfe) {
                    row.addValue(keys.get(LAST_ACCESS), new EmptyValue());
                }


            }
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private static void addValue(IRow row, String key, Map<String, Key> keys, String query, Element record) {
        Nodes nodes = record.query(query);
        if(nodes.size() > 0) {
            row.addValue(keys.get(key), nodes.get(0).getValue());
        } else {
            row.addValue(keys.get(key), new EmptyValue());
        }
    }
}