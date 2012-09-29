package com.easyinsight.datafeeds.json;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Account;
import com.easyinsight.userupload.DataTypeGuesser;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
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
    private String jsonPath;
    private String nextPageString;
    private String resultsJSONPath;
    private int httpMethod = GET;

    public String getNextPageString() {
        return nextPageString;
    }

    public void setNextPageString(String nextPageString) {
        this.nextPageString = nextPageString;
    }

    public String getResultsJSONPath() {
        return resultsJSONPath;
    }

    public void setResultsJSONPath(String resultsJSONPath) {
        this.resultsJSONPath = resultsJSONPath;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

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
        try {
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

        } catch (IOException e) {
        } catch (JSONException e) {
        }
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
        deleteStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO JSON_SOURCE (URL, DATA_SOURCE_ID, AUTH_USERNAME, AUTH_PASSWORD," +
                "HTTP_METHOD, JSON_PATH, next_page_string, results_json_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        insertStmt.setString(1, getUrl());
        insertStmt.setLong(2, getDataFeedID());
        insertStmt.setString(3, getUserName());
        insertStmt.setString(4, getPassword() != null ? PasswordStorage.encryptString(getPassword()) : null);
        insertStmt.setInt(5, getHttpMethod());
        insertStmt.setString(6, getJsonPath());
        insertStmt.setString(7, getNextPageString());
        insertStmt.setString(8, getResultsJSONPath());
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT URL, AUTH_USERNAME, AUTH_PASSWORD, HTTP_METHOD, JSON_PATH, NEXT_PAGE_STRING, RESULTS_JSON_PATH" +
                " FROM JSON_SOURCE WHERE DATA_SOURCE_ID = ?");
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
            setJsonPath(rs.getString(5));
            setNextPageString(rs.getString(6));
            setResultsJSONPath(rs.getString(7));
        }
        loadStmt.close();
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        Map<String, Key> keyMap = new HashMap<String, Key>(keys);
        List<AnalysisItem> fieldList;
        try {
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

            if (restMethod.getStatusCode() == 404) {
                throw new ReportException(new DataSourceConnectivityReportFault("We received a 404, Page Not Found when trying to retrieve the specified URL.", this));
            } else if (restMethod.getStatusCode() == 401) {
                throw new ReportException(new DataSourceConnectivityReportFault("Authorization with the specified credentials failed when trying to retrieve the specified URL.", this));
            }

            List<Map> coreArray = null;
            if (jsonPath == null || "".equals(jsonPath.trim())) {
                Object obj = new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(restMethod.getResponseBodyAsStream());
                if (obj instanceof List) {
                    List list = (List) obj;
                    coreArray = new ArrayList<Map>();
                    for (Object o : list) {
                        if (o instanceof Map) {
                            coreArray.add((Map) o);
                        }
                    }
                }
            } else {
                coreArray = JsonPath.read(restMethod.getResponseBodyAsStream(), jsonPath);
            }

            if (coreArray == null) {
                throw new ReportException(new DataSourceConnectivityReportFault("We couldn't find a JSON array at the specified URL. You might need to try a different URL or specify a different JSONPath to find the array within the returned JSON data.", this));
            } else if (coreArray.size() == 0) {
                throw new ReportException(new DataSourceConnectivityReportFault("We couldn't find a JSON array at the specified URL. You might need to try a different URL or specify a different JSONPath to find the array within the returned JSON data.", this));
            }

            DataTypeGuesser guesser = new DataTypeGuesser();
            for (Object obj : coreArray) {
                Map map = (Map) obj;
                for (Object keyObj : map.keySet()) {
                    String keyName = (String) keyObj;
                    Object value = map.get(keyName);
                    Key key = keyMap.get(keyName);
                    if (key == null) {
                        key = new NamedKey(keyName);
                        keyMap.put(keyName, key);
                    }
                    if (value != null) {
                        guesser.addValue(key, new StringValue(value.toString()));
                    }

                }
            }
            fieldList = guesser.createFeedItems();
            if (fieldList.size() == 0) {

            }
        } catch (ReportException re) {
            throw re;
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
        DataSet dataSet = new DataSet();
        try {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            HttpClient client = new HttpClient();
            if (userName != null || "".equals(userName)) {
                client.getParams().setAuthenticationPreemptive(true);
                Credentials defaultcreds = new UsernamePasswordCredentials(userName, password);
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            }
            int page = 1;
            boolean hasMorePages = false;
            int totalCount = 0;
            do {
                HttpMethod restMethod;
                if (httpMethod == GET) {
                    if (page == 1) {
                        restMethod = new GetMethod(url);
                    } else {
                        restMethod = new GetMethod(nextPageString.replace("{n}", String.valueOf(page)));
                    }
                } else if (httpMethod == POST) {
                    if (page == 1) {
                        restMethod = new PostMethod(url);
                    } else {
                        restMethod = new PostMethod(nextPageString.replace("{n}", String.valueOf(page)));
                    }
                } else {
                    throw new RuntimeException("Unknown http method " + httpMethod);
                }

                restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
                client.executeMethod(restMethod);
                List<Map> coreArray = null;
                String jsonString = restMethod.getResponseBodyAsString();
                if (jsonPath == null || "".equals(jsonPath.trim())) {
                    Object obj = new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(jsonString);
                    if (obj instanceof List) {
                        List list = (List) obj;
                        coreArray = new ArrayList<Map>();
                        for (Object o : list) {
                            if (o instanceof Map) {
                                coreArray.add((Map) o);
                            }
                        }
                    }
                } else {
                    coreArray = JsonPath.read(jsonString, jsonPath);
                }

                if (coreArray == null) {
                    throw new ReportException(new DataSourceConnectivityReportFault("We couldn't find a JSON array at the specified URL. You might need to try a different URL or specify a different JSONPath to find the array within the returned JSON data.", this));
                } else if (coreArray.size() == 0) {
                    throw new ReportException(new DataSourceConnectivityReportFault("We couldn't find a JSON array at the specified URL. You might need to try a different URL or specify a different JSONPath to find the array within the returned JSON data.", this));
                }
                for (Map object : coreArray) {
                    IRow row = dataSet.createRow();
                    totalCount++;
                    for (AnalysisItem item : getFields()) {
                        String keyName = item.getKey().toKeyString();
                        Object value = object.get(keyName);
                        if (value == null) {
                            row.addValue(keys.get(keyName), new EmptyValue());
                        } else if (item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                            AnalysisDateDimension date = (AnalysisDateDimension) item;
                            DateFormat df = new SimpleDateFormat(date.getCustomDateFormat());
                            try {
                                row.addValue(keys.get(keyName), df.parse(value.toString()));
                            } catch (ParseException e) {
                                row.addValue(keys.get(keyName), new EmptyValue());
                            }
                        } else if (item.hasType(AnalysisItemTypes.MEASURE)) {
                            if (value instanceof Number) {
                                row.addValue(keys.get(keyName), (Number) value);
                            } else {
                                row.addValue(keys.get(keyName), Double.parseDouble(value.toString()));
                            }
                        } else {
                            row.addValue(keys.get(keyName), value.toString());
                        }
                    }
                }

                if (resultsJSONPath != null && !"".equals(resultsJSONPath)) {
                    Integer totalResults = null;
                    try {
                        totalResults = JsonPath.read(jsonString, resultsJSONPath);
                    } catch (Exception e) {
                        throw new ReportException(new DataSourceConnectivityReportFault("We were unable to parse the result count value with the specified expression.", this));
                    }
                    hasMorePages = (totalCount < totalResults);
                    page++;
                }
            } while (hasMorePages);
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
