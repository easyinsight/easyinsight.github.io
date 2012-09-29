package com.easyinsight.userupload;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.*;
import com.easyinsight.core.StringValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.json.JSONDataSource;
import com.easyinsight.security.SecurityUtil;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.parser.JSONParser;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.sql.SQLException;
import java.util.*;

/**
* User: jamesboe
* Date: Mar 27, 2010
* Time: 3:21:36 PM
*/
public class JSONUploadContext extends UploadContext {
    private String userName;
    private int httpMethod;
    private String url;
    private String password;
    private String jsonPath;
    private String nextPagePath;
    private String resultsJSONPath;

    private transient UploadFormat uploadFormat;
    private transient UserUploadService.RawUploadData rawUploadData;

    @Override
    public String validateUpload(EIConnection conn) throws SQLException {
        try {
            HttpClient client = new HttpClient();
            if (userName != null && !"".equals(userName)) {
                client.getParams().setAuthenticationPreemptive(true);
                Credentials defaultcreds = new UsernamePasswordCredentials(userName, password);
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            }
            HttpMethod restMethod;
            if (httpMethod == JSONDataSource.GET) {
                restMethod = new GetMethod(url);
            } else if (httpMethod == JSONDataSource.POST) {
                restMethod = new PostMethod(url);
            } else {
                throw new RuntimeException("Unknown http method " + httpMethod);
            }
            restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
            client.executeMethod(restMethod);

            if (restMethod.getStatusCode() == 404) {
                return "We received a 404, Page Not Found when trying to retrieve the specified URL.";
            } else if (restMethod.getStatusCode() == 401) {
                return "Authorization with the specified credentials failed when trying to retrieve the specified URL.";
            }

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

            if (jsonString.length() > 100) {
                jsonString = jsonString.substring(0, 100);
            }
            if (coreArray == null) {
                return "We couldn't find a JSON array at the specified URL. You might need to try a different URL or specify a different JSONPath to find the array within the returned JSON data:\n" + jsonString;
            } else if (coreArray.size() == 0) {
                return "We couldn't find a JSON array at the specified URL. You might need to try a different URL or specify a different JSONPath to find the array within the returned JSON data:\n" + jsonString;
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }

    private Map<Key, Set<String>> sampleMap;

    @Override
    public List<AnalysisItem> guessFields(EIConnection conn) throws Exception {
        List<AnalysisItem> fieldList;
        try {
            HttpClient client = new HttpClient();
            if (userName != null && !"".equals(userName)) {
                client.getParams().setAuthenticationPreemptive(true);
                Credentials defaultcreds = new UsernamePasswordCredentials(userName, password);
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            }
            HttpMethod restMethod;
            if (httpMethod == JSONDataSource.GET) {
                restMethod = new GetMethod(url);
            } else if (httpMethod == JSONDataSource.POST) {
                restMethod = new PostMethod(url);
            } else {
                throw new RuntimeException("Unknown http method " + httpMethod);
            }
            restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
            client.executeMethod(restMethod);

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

            DataTypeGuesser guesser = new DataTypeGuesser();
            for (Object obj : coreArray) {
                Map map = (Map) obj;
                for (Object keyObj : map.keySet()) {
                    String keyName = (String) keyObj;
                    Object value = map.get(keyName);
                    Key key = new NamedKey(keyName);
                    if (value != null) {
                        guesser.addValue(key, new StringValue(value.toString()));
                    }
                }
            }
            sampleMap = guesser.getGuessesMap();
            fieldList = guesser.createFeedItems();
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fieldList;
    }

    @Override
    public long createDataSource(String name, List<AnalysisItem> analysisItems, EIConnection conn, boolean accountVisible) throws Exception {
        JSONDataSource jsonDataSource = new JSONDataSource();
        jsonDataSource.setFeedName(name);
        jsonDataSource.setHttpMethod(httpMethod);
        jsonDataSource.setUrl(url);
        jsonDataSource.setUserName(userName);
        jsonDataSource.setPassword(password);
        jsonDataSource.setJsonPath(jsonPath);
        jsonDataSource.setNextPageString(nextPagePath);
        jsonDataSource.setResultsJSONPath(resultsJSONPath);
        long id = jsonDataSource.create(conn, analysisItems, null);
        jsonDataSource.refreshData(SecurityUtil.getAccountID(), new Date(), conn, null, "", null, true);
        return id;
    }

    @Override
    public List<String> getSampleValues(Key key) {
        return new ArrayList<String>(sampleMap.get(key));
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public String getNextPagePath() {
        return nextPagePath;
    }

    public void setNextPagePath(String nextPagePath) {
        this.nextPagePath = nextPagePath;
    }

    public String getResultsJSONPath() {
        return resultsJSONPath;
    }

    public void setResultsJSONPath(String resultsJSONPath) {
        this.resultsJSONPath = resultsJSONPath;
    }
}