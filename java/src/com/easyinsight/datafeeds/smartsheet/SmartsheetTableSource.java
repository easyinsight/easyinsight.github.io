package com.easyinsight.datafeeds.smartsheet;

import com.braintreegateway.org.apache.commons.codec.binary.Hex;
import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.HTMLConnectionFactory;
import com.easyinsight.datafeeds.basecampnext.BasecampNextAccount;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.html.RedirectUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Account;
import com.easyinsight.userupload.DataTypeGuesser;
import net.minidev.json.parser.JSONParser;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.SocketException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private boolean rebuildFields = true;

    public boolean isRebuildFields() {
        return rebuildFields;
    }

    public void setRebuildFields(boolean rebuildFields) {
        this.rebuildFields = rebuildFields;
    }

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
        if (rebuildFields) {
            if (getDataFeedID() != 0) {
                List<AnalysisItem> fields = new ArrayList<>();
                Map<String, AnalysisItem> map = new HashMap<>();
                for (AnalysisItem analysisItem : getFields()) {
                    fields.add(analysisItem);
                    map.put(analysisItem.getKey().toKeyString(), analysisItem);
                }
                boolean discoveryRequired = false;
                Map<String, AnalysisItem> newFields = new HashMap<>();
                List columns = (List) rawJSONRequestForObject("https://api.smartsheet.com/1.1/sheet/" + table + "/columns");
                Set<String> keys = new HashSet<>();
                for (Object o : columns) {
                    Map column = (Map) o;
                    String id = column.get("id").toString();
                    keys.add(id);
                    AnalysisItem analysisItem = map.get(id);
                    if (analysisItem == null) {
                        String title = column.get("title").toString();
                        String type = column.get("type").toString();
                        if ("DATE".equals(type)) {
                            AnalysisDateDimension date = new AnalysisDateDimension(new NamedKey(id), title, AnalysisDateDimension.DAY_LEVEL);
                            date.setDateOnlyField(true);
                            newFields.put(id, date);
                        } else if ("DATETIME".equals(type)) {
                            AnalysisDateDimension date = new AnalysisDateDimension(new NamedKey(id), title, AnalysisDateDimension.DAY_LEVEL);
                            date.setDateOnlyField(false);
                            newFields.put(id, date);
                        } else {
                            discoveryRequired = true;
                            newFields.put(id, new AnalysisDimension(new NamedKey(id), title));
                        }
                    }
                }
                if (discoveryRequired) {
                    Map results = (Map) rawJSONRequestForObject("https://api.smartsheet.com/1.1/sheet/" + table);
                    List rows = (List) results.get("rows");
                    DataTypeGuesser guesser = new DataTypeGuesser();
                    for (int i = 0; i < rows.size(); i++ ) {
                        Object obj = rows.get(i);

                        Map rowMap = (Map) obj;
                        List cells = (List) rowMap.get("cells");
                        for (Object cellObj : cells) {
                            Map cell = (Map) cellObj;
                            String columnID = cell.get("columnId").toString();
                            AnalysisItem testing = newFields.get(columnID);
                            if (testing != null) {
                                Object valueObj = cell.get("value");
                                if (valueObj != null) {
                                    String string = valueObj.toString();
                                    guesser.addValue(testing.getKey(), new StringValue(string));
                                }
                            }
                        }
                    }
                    //for (AnalysisItem item : newFields.values()) {
                    List<AnalysisItem> generatedFields = guesser.createFeedItems();
                    for (AnalysisItem item : generatedFields) {
                        keys.remove(item.getKey().toKeyString());
                        AnalysisItem existing = newFields.get(item.getKey().toKeyString());
                        item.setDisplayName(existing.getDisplayName());
                    }
                    fields.addAll(generatedFields);
                    //}
                    List<AnalysisItem> others = new ArrayList<>();
                    for (Map.Entry<String, AnalysisItem> entry : newFields.entrySet()) {
                        if (keys.contains(entry.getKey())) {
                            others.add(entry.getValue());
                        }
                    }
                    fields.addAll(others);
                } else if (newFields.size() > 0) {
                    fields.addAll(newFields.values());
                }
                rebuildFields = discoveryRequired;
                cacheFields = fields;
            }
        }
    }

    private List<AnalysisItem> cacheFields = new ArrayList<AnalysisItem>();

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();

        if (getDataFeedID() == 0) {
            return fields;
        }

        return cacheFields;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            Map results = (Map) rawJSONRequestForObject("https://api.smartsheet.com/1.1/sheet/" + table);
            Map<String, AnalysisItem> fieldMap = new HashMap<>();
            for (AnalysisItem field : getFields()) {
                if (field.isConcrete()) {
                    fieldMap.put(field.getKey().toKeyString(), field);
                }
            }
            List rows = (List) results.get("rows");

            DataSet dataSet = new DataSet();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yy");

            for (int i = 0; i < rows.size(); i++ ){
                Object obj = rows.get(i);
                IRow row = dataSet.createRow();
                Map rowMap = (Map) obj;
                List cells = (List) rowMap.get("cells");
                for (Object cellObj : cells) {
                    Map cell = (Map) cellObj;
                    String columnID = cell.get("columnId").toString();
                    Key key = keys.get(columnID);
                    Object valueObj = cell.get("value");
                    if (key != null && valueObj != null) {
                        AnalysisItem item = fieldMap.get(columnID);
                        if (item != null && item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                            try {
                                String str = valueObj.toString();
                                if (str.contains("-")) {
                                    Date date = sdf1.parse(str);
                                    row.addValue(key, date);
                                } else if (str.contains("/")) {
                                    Date date = sdf2.parse(str);
                                    row.addValue(key, date);
                                } else {
                                    row.addValue(key, str);
                                }
                            } catch (ParseException e) {
                                row.addValue(key, valueObj.toString());
                            }
                        } else if (item != null && item.hasType(AnalysisItemTypes.DIMENSION)) {
                            try {
                                double dVal = Double.parseDouble(valueObj.toString());
                                int asInt = (int) dVal;
                                double asDAgain = (double) asInt;
                                if ((asDAgain - asInt) < .0001) {
                                    System.out.println(asInt);
                                    row.addValue(key, new StringValue(String.valueOf(asInt)));
                                } else {
                                    row.addValue(key, valueObj.toString());
                                }
                            } catch (NumberFormatException e) {
                                row.addValue(key, valueObj.toString());
                            }
                        } else {
                            row.addValue(key, valueObj.toString());
                        }
                    }
                }
            }

            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
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
        PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO SMARTSHEET (DATA_SOURCE_ID, ACCESS_TOKEN, REFRESH_TOKEN, sheet_id, rebuild_fields) VALUES (?, ?, ?, ?, ?)");
        saveStmt.setLong(1, getDataFeedID());
        saveStmt.setString(2, accessToken);
        saveStmt.setString(3, refreshToken);
        saveStmt.setString(4, table);
        saveStmt.setBoolean(5, rebuildFields);
        saveStmt.execute();
        saveStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT ACCESS_TOKEN, REFRESH_TOKEN, sheet_id, rebuild_fields FROM SMARTSHEET WHERE data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            accessToken = rs.getString(1);
            refreshToken = rs.getString(2);
            table = rs.getString(3);
            rebuildFields = rs.getBoolean(4);
        }
        queryStmt.close();
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
        return new ArrayList<>();
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

    protected boolean otherwiseChanged() {
        boolean blah = rebuildFields;
        rebuildFields = false;
        return blah;
    }

    public String postOAuthSetup(HttpServletRequest request) {
        if (table == null || "".equals(table)) {
            return RedirectUtil.getURL(request, "/app/html/dataSources/" + getApiKey() + "/smartsheetAccountSelection");
        } else {
            return null;
        }
    }

    public void configureFactory(HTMLConnectionFactory factory) {
        factory.type(HTMLConnectionFactory.TYPE_OAUTH);
    }
}
