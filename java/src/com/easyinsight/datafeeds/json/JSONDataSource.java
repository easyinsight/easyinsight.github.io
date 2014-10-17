package com.easyinsight.datafeeds.json;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
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

    public static final int NEXT_PAGE_URL = 1;
    public static final int ITERATE_BY_LIMIT_AND_PAGE = 2;
    public static final int ITERATE_BY_LIMIT_AND_OFFSET = 3;


    private String nextPageString;
    private int paginationMethod;



    private int perPageLimit;
    private int firstPageNumber;
    private String pageField;
    private String offsetField;
    private String limitField;

    private String url;
    private String userName;
    private String password;
    private String jsonPath;

    private int httpMethod = GET;

    private boolean liveSource;

    public String getOffsetField() {
        return offsetField;
    }

    public void setOffsetField(String offsetField) {
        this.offsetField = offsetField;
    }

    public int getFirstPageNumber() {
        return firstPageNumber;
    }

    public void setFirstPageNumber(int firstPageNumber) {
        this.firstPageNumber = firstPageNumber;
    }

    public int getPaginationMethod() {
        return paginationMethod;
    }

    public void setPaginationMethod(int paginationMethod) {
        this.paginationMethod = paginationMethod;
    }

    public int getPerPageLimit() {
        return perPageLimit;
    }

    public void setPerPageLimit(int perPageLimit) {
        this.perPageLimit = perPageLimit;
    }

    public String getPageField() {
        return pageField;
    }

    public void setPageField(String pageField) {
        this.pageField = pageField;
    }

    public String getLimitField() {
        return limitField;
    }

    public void setLimitField(String limitField) {
        this.limitField = limitField;
    }

    public boolean isLiveSource() {
        return liveSource;
    }

    public void setLiveSource(boolean liveSource) {
        this.liveSource = liveSource;
    }

    public String getNextPageString() {
        return nextPageString;
    }

    public void setNextPageString(String nextPageString) {
        this.nextPageString = nextPageString;
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
                "HTTP_METHOD, JSON_PATH, next_page_string, live_source, pagination_method, per_page_limit, first_page_number, page_field, limit_field, offset_field) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        insertStmt.setString(1, getUrl());
        insertStmt.setLong(2, getDataFeedID());
        insertStmt.setString(3, getUserName());
        insertStmt.setString(4, getPassword() != null ? PasswordStorage.encryptString(getPassword()) : null);
        insertStmt.setInt(5, getHttpMethod());
        insertStmt.setString(6, getJsonPath());
        insertStmt.setString(7, getNextPageString());
        insertStmt.setBoolean(8, isLiveSource());
        insertStmt.setInt(9, getPaginationMethod());
        insertStmt.setInt(10, getPerPageLimit());
        insertStmt.setInt(11, getFirstPageNumber());
        insertStmt.setString(12, getPageField());
        insertStmt.setString(13, getLimitField());
        insertStmt.setString(14, getOffsetField());
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT URL, AUTH_USERNAME, AUTH_PASSWORD, HTTP_METHOD, JSON_PATH, NEXT_PAGE_STRING, LIVE_SOURCE," +
                "pagination_method, per_page_limit, first_page_number, page_field, limit_field, offset_field FROM JSON_SOURCE WHERE DATA_SOURCE_ID = ?");
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
            setLiveSource(rs.getBoolean(7));
            setPaginationMethod(rs.getInt(8));
            setPerPageLimit(rs.getInt(9));
            setFirstPageNumber(rs.getInt(10));
            setPageField(rs.getString(11));
            setLimitField(rs.getString(12));
            setOffsetField(rs.getString(13));
        }
        loadStmt.close();
    }

    private class JSONClient {
        private HttpClient httpClient;

        private JSONClient() {
            HttpClient client = new HttpClient();
            if (getUserName() != null && !"".equals(getUserName())) {
                client.getParams().setAuthenticationPreemptive(true);
                Credentials defaultcreds = new UsernamePasswordCredentials(getUserName(), getPassword());
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            }
            this.httpClient = client;
        }

        public HttpClient getHttpClient() {
            return httpClient;
        }

        private HttpMethod createMethod() {
            HttpMethod restMethod;
            if (httpMethod == GET) {
                restMethod = new GetMethod(getUrl());
            } else if (httpMethod == POST) {
                restMethod = new PostMethod(getUrl());
            } else {
                throw new RuntimeException("Unknown http method " + httpMethod);
            }
            restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
            return restMethod;
        }

        private HttpMethod createPagedMethod(int page, int ctr) {
            HttpMethod restMethod;
            if (paginationMethod == NEXT_PAGE_URL) {
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
            } else if (paginationMethod == ITERATE_BY_LIMIT_AND_PAGE) {
                if (httpMethod == GET) {
                    if (url.contains("?")) {
                        restMethod = new GetMethod(url + "&" + limitField + "=" + perPageLimit + "&" + pageField + "=" + firstPageNumber);
                    } else {
                        restMethod = new GetMethod(url + "?" + limitField + "=" + perPageLimit + "&" + pageField + "=" + firstPageNumber);
                    }
                } else if (httpMethod == POST) {
                    if (url.contains("?")) {
                        restMethod = new PostMethod(url + "&" + limitField + "=" + perPageLimit + "&" + pageField + "=" + firstPageNumber);
                    } else {
                        restMethod = new PostMethod(url + "?" + limitField + "=" + perPageLimit + "&" + pageField + "=" + firstPageNumber);
                    }
                } else {
                    throw new RuntimeException("Unknown http method " + httpMethod);
                }
            } else if (paginationMethod == ITERATE_BY_LIMIT_AND_OFFSET) {
                if (httpMethod == GET) {
                    if (url.contains("?")) {
                        restMethod = new GetMethod(url + "&" + limitField + "=" + perPageLimit + "&" + offsetField + "=" + ctr);
                    } else {
                        restMethod = new GetMethod(url + "?" + limitField + "=" + perPageLimit + "&" + offsetField + "=" + ctr);
                    }
                } else if (httpMethod == POST) {
                    if (url.contains("?")) {
                        restMethod = new PostMethod(url + "&" + limitField + "=" + perPageLimit + "&" + offsetField + "=" + ctr);
                    } else {
                        restMethod = new PostMethod(url + "?" + limitField + "=" + perPageLimit + "&" + offsetField + "=" + ctr);
                    }
                } else {
                    throw new RuntimeException("Unknown http method " + httpMethod);
                }
            } else {
                throw new RuntimeException("Unknown pagination method.");
            }
            return restMethod;
        }
    }

    public JSONSetup testJSONConnectivityAndSuggestJSONPath() {
        String suggestedJSONPath = null;
        String responseLine;
        try {
            JSONClient jsonClient = new JSONClient();

            HttpMethod restMethod = jsonClient.createMethod();

            jsonClient.getHttpClient().executeMethod(restMethod);

            suggestedJSONPath = null;
            responseLine = "";
            if (restMethod.getStatusCode() == 404) {
                responseLine = "We received a 404, Page Not Found when trying to retrieve the specified URL.";
            } else if (restMethod.getStatusCode() == 401 || restMethod.getStatusCode() == 403) {
                responseLine = "Authorization with the specified credentials failed when trying to retrieve the specified URL.";
            } else {

                List<Map> coreArray = null;
                Map retMap = null;

                Object obj = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(restMethod.getResponseBodyAsStream());
                if (obj instanceof List) {
                    List list = (List) obj;
                    coreArray = new ArrayList<Map>();
                    for (Object o : list) {
                        if (o instanceof Map) {
                            coreArray.add((Map) o);
                        }
                    }
                } else if (obj instanceof Map) {
                    retMap = (Map) obj;
                }

                if (coreArray != null) {
                    // we think we have the data as is
                } else if (retMap != null) {
                    List biggestList = null;
                    String biggestListKey = null;
                    for (Object o : retMap.entrySet()) {
                        Map.Entry entry = (Map.Entry) o;
                        Object v = entry.getValue();
                        if (v instanceof List && biggestList == null) {
                            biggestList = (List) v;
                            biggestListKey = (String) entry.getKey();
                        } else if (v instanceof List && ((List) v).size() > biggestList.size()) {
                            biggestList = (List) v;
                            biggestListKey = (String) entry.getKey();
                        }
                    }
                    if (biggestListKey != null) {
                        suggestedJSONPath = "$." + biggestListKey;
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            responseLine = "Something went wrong with the requested parameters.";
        }
        JSONSetup jsonSetup = new JSONSetup();
        jsonSetup.setFieldLine(responseLine);
        jsonSetup.setSuggestedJSONPath(suggestedJSONPath);
        return jsonSetup;
    }

    public JSONSetup testJSONPaging() {
        String errorLine = null;
        try {
            JSONClient jsonClient = new JSONClient();

            HttpMethod restMethod = jsonClient.createPagedMethod(getFirstPageNumber(), 0);

            jsonClient.getHttpClient().executeMethod(restMethod);
            if (restMethod.getStatusCode() == 404) {
                errorLine = "We received a 404, Page Not Found when trying to retrieve the specified URL.";
            } else if (restMethod.getStatusCode() == 401) {
                errorLine = "Authorization with the specified credentials failed when trying to retrieve the specified URL.";
            } else {
                String firstResponse = restMethod.getResponseBodyAsString();
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
                    Object obj = JsonPath.read(restMethod.getResponseBodyAsStream(), jsonPath);
                    if (obj instanceof List) {
                        coreArray = (List<Map>) obj;
                    }
                }
                if (coreArray == null) {
                    errorLine = "We couldn't find data using the combination of the previous configuration and the paging parameters.";
                } else {
                    restMethod = jsonClient.createPagedMethod(getFirstPageNumber() + 1, getPerPageLimit());
                    jsonClient.getHttpClient().executeMethod(restMethod);
                    if (restMethod.getStatusCode() == 404) {
                        errorLine = "We received a 404, Page Not Found when trying to retrieve the specified URL for the second page of data.";
                    } else if (restMethod.getStatusCode() == 401) {
                        errorLine = "Authorization with the specified credentials failed when trying to retrieve the specified URL for the second page of data.";
                    } else {
                        String secondResponse = restMethod.getResponseBodyAsString();
                        if (firstResponse.equals(secondResponse)) {
                            errorLine = "The response from the server for the second page of data was identical to the first page of data.";
                        } else {
                            coreArray = null;
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
                                Object obj = JsonPath.read(restMethod.getResponseBodyAsStream(), jsonPath);
                                if (obj instanceof List) {
                                    coreArray = (List<Map>) obj;
                                }
                            }
                            if (coreArray == null) {
                                // guess we can' do this?
                                //errorLine = "We couldn't find data on the second page using the combination of the previous configuration and the paging parameters.";
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            errorLine = "Something went wrong with the requested parameters.";
        }
        JSONSetup setup = new JSONSetup();
        setup.setFieldLine(errorLine);
        return setup;
    }

    public JSONSetup jsonString() throws IOException {
        List<String> fieldNames = new ArrayList<>();
        List<AnalysisItem> generatedFields = new ArrayList<>();
        String responseBody = "";
        int pages = 0;
        String responseLine = "";
        String suggestedJSONPath = "";
        try {
            JSONClient jsonClient = new JSONClient();

            HttpMethod restMethod = jsonClient.createMethod();

            jsonClient.getHttpClient().executeMethod(restMethod);



            if (restMethod.getStatusCode() == 404) {
                responseLine = "We received a 404, Page Not Found when trying to retrieve the specified URL.";
            } else if (restMethod.getStatusCode() == 401) {
                responseLine = "Authorization with the specified credentials failed when trying to retrieve the specified URL.";
            } else {

                responseBody = restMethod.getResponseBodyAsString();
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
                    Object obj = JsonPath.read(restMethod.getResponseBodyAsStream(), jsonPath);
                    if (obj instanceof List) {
                        coreArray = (List<Map>) obj;
                    }
                }

                if (coreArray == null) {
                    responseLine = "We couldn't find a JSON array at the specified URL. You might need to try a different URL or specify a different JSONPath to find the array within the returned JSON data.";
                } else if (coreArray.size() == 0) {
                    responseLine = "We couldn't find a JSON array at the specified URL. You might need to try a different URL or specify a different JSONPath to find the array within the returned JSON data.";
                } else {
                    DataTypeGuesser guesser = new DataTypeGuesser();
                    Map<String, Key> keyMap = new HashMap<String, Key>();
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

                    generatedFields = guesser.createFeedItems();
                    for (AnalysisItem field : generatedFields) {
                        fieldNames.add(field.toDisplay());
                    }
                }
            }
        } catch (Exception e) {
            responseLine = "We encountered an internal server error in trying to process the specified parameters. We've logged the error for our engineers to investigate.";
            LogClass.error(e);
        }

        JSONSetup jsonSetup = new JSONSetup();

        if (responseBody.length() > 100000) {
            jsonSetup.setResult("Result too large to display.");
        } else {
            jsonSetup.setResult(responseBody);
        }
        jsonSetup.setResults(pages);
        jsonSetup.setGeneratedFields(generatedFields);
        jsonSetup.setFields(fieldNames);
        jsonSetup.setFieldLine(responseLine);
        jsonSetup.setSuggestedJSONPath(suggestedJSONPath);
        return jsonSetup;
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
                Object obj = JsonPath.read(restMethod.getResponseBodyAsStream(), jsonPath);
                if (obj instanceof List) {
                    coreArray = (List<Map>) obj;
                } else {

                }
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
            HttpClient client = new HttpClient();
            if (userName != null || "".equals(userName)) {
                client.getParams().setAuthenticationPreemptive(true);
                Credentials defaultcreds = new UsernamePasswordCredentials(userName, password);
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            }
            int page = 1;
            int ctr = 0;
            boolean hasMorePages;

            String lastResult = null;

            boolean continueRetrieval = false;
            do {
                HttpMethod restMethod;
                if (paginationMethod == NEXT_PAGE_URL) {
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
                } else if (paginationMethod == ITERATE_BY_LIMIT_AND_PAGE) {
                    if (httpMethod == GET) {
                        if (url.contains("?")) {
                            restMethod = new GetMethod(url + "&" + limitField + "=" + perPageLimit + "&" + pageField + "=" + firstPageNumber);
                        } else {
                            restMethod = new GetMethod(url + "?" + limitField + "=" + perPageLimit + "&" + pageField + "=" + firstPageNumber);
                        }
                    } else if (httpMethod == POST) {
                        if (url.contains("?")) {
                            restMethod = new PostMethod(url + "&" + limitField + "=" + perPageLimit + "&" + pageField + "=" + firstPageNumber);
                        } else {
                            restMethod = new PostMethod(url + "?" + limitField + "=" + perPageLimit + "&" + pageField + "=" + firstPageNumber);
                        }
                    } else {
                        throw new RuntimeException("Unknown http method " + httpMethod);
                    }
                } else if (paginationMethod == ITERATE_BY_LIMIT_AND_OFFSET) {
                    if (httpMethod == GET) {
                        if (url.contains("?")) {
                            restMethod = new GetMethod(url + "&" + limitField + "=" + perPageLimit + "&" + offsetField + "=" + ctr);
                        } else {
                            restMethod = new GetMethod(url + "?" + limitField + "=" + perPageLimit + "&" + offsetField + "=" + ctr);
                        }
                    } else if (httpMethod == POST) {
                        if (url.contains("?")) {
                            restMethod = new PostMethod(url + "&" + limitField + "=" + perPageLimit + "&" + offsetField + "=" + ctr);
                        } else {
                            restMethod = new PostMethod(url + "?" + limitField + "=" + perPageLimit + "&" + offsetField + "=" + ctr);
                        }
                    } else {
                        throw new RuntimeException("Unknown http method " + httpMethod);
                    }
                } else if (paginationMethod == 0) {
                    if (httpMethod == GET) {
                        restMethod = new GetMethod(url);
                    } else if (httpMethod == POST) {
                        restMethod = new PostMethod(url);
                    } else {
                        throw new RuntimeException("Unknown http method " + httpMethod);
                    }
                } else {
                    throw new RuntimeException("Unknown pagination method " + paginationMethod);
                }

                restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
                client.executeMethod(restMethod);
                List<Map> coreArray = null;
                String jsonString = restMethod.getResponseBodyAsString();
                if (jsonString.equals(lastResult)) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Two API calls in a row returned the same data, indicating a problem with your paging parameters.", this));
                }
                lastResult = jsonString;
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
                    if (page == 1) {
                        throw new ReportException(new DataSourceConnectivityReportFault("We couldn't find a JSON array at the specified URL. You might need to try a different URL or specify a different JSONPath to find the array within the returned JSON data.", this));
                    } else {
                        break;
                    }
                } else if (coreArray.size() == 0) {
                    if (page == 1) {
                        throw new ReportException(new DataSourceConnectivityReportFault("We couldn't find a JSON array at the specified URL. You might need to try a different URL or specify a different JSONPath to find the array within the returned JSON data.", this));
                    } else {
                        break;
                    }
                }
                int pageResults = 0;
                for (Map object : coreArray) {
                    IRow row = dataSet.createRow();
                    pageResults++;
                    ctr++;
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
                                try {
                                    row.addValue(keys.get(keyName), Double.parseDouble(value.toString()));
                                } catch (NumberFormatException e) {
                                    // ignore
                                }
                            }
                        } else {
                            row.addValue(keys.get(keyName), value.toString());
                        }
                    }
                }
                page++;
                if (page > 500) {
                    break;
                }
                Thread.sleep(1000);
                if (paginationMethod == NEXT_PAGE_URL) {
                    continueRetrieval = pageResults > 0 && nextPageString != null && !"".equals(nextPageString);
                } else if (paginationMethod == ITERATE_BY_LIMIT_AND_OFFSET) {
                    continueRetrieval = pageResults == perPageLimit;
                } else if (paginationMethod == ITERATE_BY_LIMIT_AND_PAGE) {
                    continueRetrieval = pageResults == perPageLimit;
                }

            } while (continueRetrieval);
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return dataSet;
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        if (liveSource) {
            return new JSONFeed();
        } else {
            return super.createFeedObject(parent);
        }
    }
}
