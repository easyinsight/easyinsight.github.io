package com.easyinsight.datafeeds.json;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Account;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 6/22/12
 * Time: 9:52 AM
 */
public class JSONDataSource extends ServerDataSourceDefinition {

    public static final int GET = 1;
    public static final int POST = 2;

    private String url;
    private String userName;
    private String password;
    private int httpMethod = GET;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(int httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    public ReportFault validateDataConnectivity() {
        return null;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM JSON_SOURCE WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO JSON_SOURCE (URL, DATA_SOURCE_ID, AUTH_USERNAME, AUTH_PASSWORD," +
                "HTTP_METHOD) VALUES (?, ?, ?, ?, ?)");
        insertStmt.setString(1, getUrl());
        insertStmt.setLong(2, getDataFeedID());
        insertStmt.setString(3, getUserName());
        insertStmt.setString(4, getPassword() != null ? PasswordStorage.encryptString(getPassword()) : null);
        insertStmt.setInt(5, getHttpMethod());
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT URL, AUTH_USERNAME, AUTH_PASSWORD, HTTP_METHOD FROM JSON_SOURCE WHERE DATA_SOURCE_ID = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            setUrl(rs.getString(1));
            setUserName(rs.getString(2));
            String password = rs.getString(3);
            if (password != null) {
                this.password = PasswordStorage.decryptString(password);
            }
            setHttpMethod(rs.getInt(4));
        }
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fieldList = new ArrayList<AnalysisItem>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            HttpClient client = new HttpClient();
            if (userName != null && !"".equals(userName)) {
                client.getParams().setAuthenticationPreemptive(true);
                Credentials defaultcreds = new UsernamePasswordCredentials(userName, password);
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            }
            HttpMethod restMethod;
            if (httpMethod == GET) {
                restMethod = new GetMethod(url);
            } else if (httpMethod == POST) {
                restMethod = new PostMethod(url);
            } else {
                throw new RuntimeException("Unknown http method " + httpMethod);
            }
            restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
            client.executeMethod(restMethod);
            JSONArray array = new JSONArray(restMethod.getResponseBodyAsString());

            if (array.length() == 0) {

            } else {
                JSONObject object = (JSONObject) array.get(0);
                Iterator iter = object.keys();
                while (iter.hasNext()) {
                    String keyName = (String) iter.next();
                    Key key = keys.get(keyName);
                    if (key == null) {
                        key = new NamedKey(keyName);
                    }
                    String value = object.getString(keyName);
                    try {
                        Double.parseDouble(value);
                        fieldList.add(new AnalysisMeasure(key, AggregationTypes.SUM));
                    } catch (NumberFormatException e) {
                        try {
                            sdf.parse(value);
                            fieldList.add(new AnalysisDateDimension(key, true, AnalysisDateDimension.DAY_LEVEL));
                        } catch (ParseException e1) {
                            fieldList.add(new AnalysisDimension(key, true));
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fieldList;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.JSON;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
        for (AnalysisItem analysisItem : getFields()) {
            map.put(analysisItem.getKey().toKeyString(), analysisItem);
        }
        DataSet dataSet = new DataSet();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            HttpClient client = new HttpClient();
            if (userName != null && !"".equals(userName)) {
                client.getParams().setAuthenticationPreemptive(true);
                Credentials defaultcreds = new UsernamePasswordCredentials(userName, password);
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            }
            HttpMethod restMethod;
            if (httpMethod == GET) {
                restMethod = new GetMethod(url);
            } else if (httpMethod == POST) {
                restMethod = new PostMethod(url);
            } else {
                throw new RuntimeException("Unknown http method " + httpMethod);
            }
            restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
            client.executeMethod(restMethod);
            JSONArray array = new JSONArray(restMethod.getResponseBodyAsString());
            for (int i = 0; i < array.length(); i++) {
                IRow row = dataSet.createRow();
                JSONObject object = (JSONObject) array.get(i);
                Iterator iter = object.keys();
                while (iter.hasNext()) {
                    String keyName = (String) iter.next();
                    String value = object.getString(keyName);
                    AnalysisItem item = map.get(keyName);
                    if (item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                        row.addValue(keys.get(keyName), sdf.parse(value));
                    } else if (item.hasType(AnalysisItemTypes.MEASURE)) {
                        row.addValue(keys.get(keyName), Double.parseDouble(value));
                    } else {
                        row.addValue(keys.get(keyName), value);
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
