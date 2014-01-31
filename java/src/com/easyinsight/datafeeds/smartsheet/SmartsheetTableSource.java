package com.easyinsight.datafeeds.smartsheet;

import com.braintreegateway.org.apache.commons.codec.binary.Hex;
import com.easyinsight.analysis.*;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.basecampnext.BasecampNextAccount;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Account;
import net.minidev.json.parser.JSONParser;
import org.apache.amber.oauth2.client.OAuthClient;
import org.apache.amber.oauth2.client.URLConnectionClient;
import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.SocketException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/30/14
 * Time: 9:02 AM
 */
public class SmartsheetTableSource extends SmartsheetBaseSource {

    private String refreshToken;
    private String accessToken;
    private String table;

    @Override
    protected void beforeRefresh(EIConnection conn) {
        super.beforeRefresh(conn);
        if (getLastRefreshStart() != null) {
            try {
                refreshTokenInfo();
            } catch (ReportException re) {
                throw re;
            } catch (Exception e) {
                throw new ReportException(new DataSourceConnectivityReportFault(e.getMessage(), this));
            }
        }
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        List columns = (List) rawJSONRequestForObject("https://api.smartsheet.com/1.1/sheet/" + table + "/columns");
        for (Object o : columns) {
            Map column = (Map) o;
            String id = column.get("id").toString();
            String title = column.get("title").toString();
            String type = column.get("type").toString();
            Key key = keys.get(id);
            if (key == null) {
                key = new NamedKey(id);
            }
            AnalysisDimension dimension = new AnalysisDimension(key, title);
            fields.add(dimension);
            /*if ("TEXT_NUMBER".equals(type)) {

            } else if ("PICKLIST".equals(type)) {

            } else if ("DATE".equals(type)) {

            } else if ("CHECKBOX".equals(type)) {

            }*/
        }
        return fields;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        Map results = (Map) rawJSONRequestForObject("https://api.smartsheet.com/1.1/sheet/" + table);
        List rows = (List) results.get("rows");

        DataSet dataSet = new DataSet();

        for (Object obj : rows) {
            IRow row = dataSet.createRow();
            Map rowMap = (Map) obj;
            List cells = (List) rowMap.get("cells");
            for (Object cellObj : cells) {
                Map cell = (Map) cellObj;
                String columnID = cell.get("columnId").toString();
                Key key = keys.get(columnID);
                Object valueObj = cell.get("value");
                if (key != null && valueObj != null) {
                    row.addValue(key, valueObj.toString());
                }
            }
        }

        return dataSet;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SMARTSHEET_TABLE;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM SMARTSHEET WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO SMARTSHEET (DATA_SOURCE_ID, ACCESS_TOKEN, REFRESH_TOKEN, sheet_id) VALUES (?, ?, ?, ?)");
        saveStmt.setLong(1, getDataFeedID());
        saveStmt.setString(2, accessToken);
        saveStmt.setString(3, refreshToken);
        saveStmt.setString(4, table);
        saveStmt.execute();
        saveStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT ACCESS_TOKEN, REFRESH_TOKEN, sheet_id FROM SMARTSHEET WHERE data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            accessToken = rs.getString(1);
            refreshToken = rs.getString(2);
            table = rs.getString(3);
        }
    }

    public void refreshTokenInfo() throws OAuthSystemException, OAuthProblemException {
        try {

            HttpClient httpClient = new HttpClient();
            PostMethod postMethod = new PostMethod("https://api.smartsheet.com/1.1/token");
            String doHash = CLIENT_SECRET + "|" + refreshToken;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(doHash.getBytes("UTF-8"));
            String hex = Hex.encodeHexString(digest);
            postMethod.setParameter("client_id", CLIENT_ID);
            postMethod.setParameter("grant_type", "refresh_token");
            postMethod.setParameter("hash", hex);
            postMethod.setParameter("refresh_token", refreshToken);
            postMethod.setParameter("redirect_uri", "https://www.easy-insight.com/app/oauth");
            httpClient.executeMethod(postMethod);
            Map result = (Map) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(postMethod.getResponseBodyAsStream());
            System.out.println("blah");
            if (result.containsKey("error")) {
                throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize access to Smartsheet.", this));
            }
            accessToken = result.get("access_token").toString();
            refreshToken = result.get("refresh_token").toString();
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize access to Smartsheet.", this));
        }
    }

    @Override
    public void exchangeTokens(EIConnection conn, HttpServletRequest httpRequest, String externalPin) throws Exception {
        try {
            if (httpRequest != null) {
                String code = httpRequest.getParameter("code");
                if (code != null) {
                    HttpClient httpClient = new HttpClient();
                    PostMethod postMethod = new PostMethod("https://api.smartsheet.com/1.1/token");
                    String doHash = CLIENT_SECRET + "|" + code;
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    byte[] digest = md.digest(doHash.getBytes("UTF-8"));
                    String hex = Hex.encodeHexString(digest);
                    postMethod.setParameter("client_id", CLIENT_ID);
                    postMethod.setParameter("grant_type", "authorization_code");
                    postMethod.setParameter("hash", hex);
                    postMethod.setParameter("code", code);
                    postMethod.setParameter("redirect_uri", "https://www.easy-insight.com/app/oauth");
                    httpClient.executeMethod(postMethod);
                    String responseBody = new String(postMethod.getResponseBody());
                    System.out.println("blah");
                    Map result = (Map) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(postMethod.getResponseBodyAsStream());
                    accessToken = result.get("access_token").toString();
                    refreshToken = result.get("refresh_token").toString();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }

    public Collection<BasecampNextAccount> getTables() {
        List<BasecampNextAccount> availableTables = new ArrayList<BasecampNextAccount>();
        List tables = (List) rawJSONRequestForObject("https://api.smartsheet.com/1.1/sheets");
        for (Object o : tables) {
            Map map = (Map) o;
            String id = map.get("id").toString();
            String name = map.get("name").toString();
            BasecampNextAccount table = new BasecampNextAccount();
            table.setId(id);
            table.setName(name);
            availableTables.add(table);
        }
        return availableTables;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    protected Object rawJSONRequestForObject(String path) {
        HttpClient client = new HttpClient();
        HttpMethod restMethod = new GetMethod(path);

        restMethod.setRequestHeader("Authorization", "Bearer " + accessToken);
        restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
        restMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
        boolean successful = false;
        Object jsonObject = null;
        int retryCount = 0;
        do {
            try {
                client.executeMethod(restMethod);
                if (restMethod.getStatusCode() == 429 || restMethod.getStatusCode() == 502 || restMethod.getStatusCode() == 503 ||
                        restMethod.getStatusCode() == 504 || restMethod.getStatusCode() == 408) {
                    retryCount++;
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e1) {
                    }
                } else {
                    jsonObject = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(restMethod.getResponseBodyAsStream());
                    successful = true;
                }
            } catch (IOException e) {
                System.out.println("IOException " + e.getMessage());
                retryCount++;
                if (e.getMessage().contains("429") || e instanceof SocketException) {
                    //noinspection EmptyCatchBlock
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e1) {
                    }
                } else {
                    throw new RuntimeException(e);
                }
            } catch (ReportException re) {
                throw re;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } while (!successful && retryCount < 10);
        if (!successful) {
            throw new RuntimeException("Smartsheet could not be reached due to a large number of current users, please try again in a bit.");
        }
        return jsonObject;
    }
}
