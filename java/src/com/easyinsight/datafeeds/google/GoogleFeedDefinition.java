package com.easyinsight.datafeeds.google;

import com.easyinsight.analysis.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Account;
import com.easyinsight.core.*;
import com.easyinsight.userupload.IDataTypeGuesser;
import com.easyinsight.userupload.DataTypeGuesser;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.net.URL;

import com.google.gdata.util.ServiceException;
import org.apache.amber.oauth2.client.OAuthClient;
import org.apache.amber.oauth2.client.URLConnectionClient;
import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;

/**
 * User: James Boe
 * Date: May 14, 2008
 * Time: 2:28:56 PM
 */
public class GoogleFeedDefinition extends ServerDataSourceDefinition {
    private String worksheetURL;

    public String getWorksheetURL() {
        return worksheetURL;
    }

    public void setWorksheetURL(String worksheetURL) {
        this.worksheetURL = worksheetURL;
    }

    public boolean isLiveData() {
        return true;
    }

    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    public boolean requiresConfiguration() {
        return false;
    }

    private String pin;

    private String tokenKey;
    private String tokenSecret;

    private String refreshToken;
    private String accessToken;

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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    @Override
    public void exchangeTokens(EIConnection conn, HttpServletRequest request, String externalPin) throws Exception {
        if (request != null) {
            String code = request.getParameter("code");
            if (code != null) {
                OAuthClientRequest oAuthClientRequest = OAuthClientRequest.tokenLocation("https://www.googleapis.com/oauth2/v3/token").
                        setGrantType(GrantType.AUTHORIZATION_CODE).setClientId("196763839405.apps.googleusercontent.com").
                        setClientSecret("bRmYcsSJcp0CBehRRIcxl1hK").
                        setRedirectURI("https://www.easy-insight.com/app/oauth").
                        setParameter("access_type", "offline").setParameter("approval_prompt", "force").
                        setCode(code).buildBodyMessage();

                System.out.println("and access type of offline here too?");
                OAuthClient client = new OAuthClient(new URLConnectionClient());
                OAuthJSONAccessTokenResponse response = client.accessToken(oAuthClientRequest);
                System.out.println("access token = " + response.getAccessToken());
                System.out.println("refresh token = " + response.getRefreshToken());
                accessToken = response.getAccessToken();
                refreshToken = response.getRefreshToken();

                PreparedStatement ps = conn.prepareStatement("SELECT GOOGLE_FEED.DATA_FEED_ID FROM GOOGLE_FEED, UPLOAD_POLICY_USERS, USER WHERE TOKEN_KEY = ? AND " +
                        "GOOGLE_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND UPLOAD_POLICY_USERS.USER_ID = USER.USER_ID AND " +
                        "USER.ACCOUNT_ID = ?");
                ps.setString(1, tokenKey);
                ps.setLong(2, SecurityUtil.getAccountID());
                ResultSet rs = ps.executeQuery();
                System.out.println("We also need to update: ");
                while (rs.next()) {
                    System.out.println("\t" + rs.getLong(1));
                }
            }
        }
    }

    public void customStorage(Connection conn) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM GOOGLE_FEED WHERE DATA_FEED_ID = ? ");
        PreparedStatement insertGoogleStmt = conn.prepareStatement("INSERT INTO GOOGLE_FEED (DATA_FEED_ID, WORKSHEETURL, TOKEN_KEY, TOKEN_SECRET, REFRESH_TOKEN, ACCESS_TOKEN) " +
                "VALUES (?, ?, ?, ?, ?, ?)");
        try {
            clearStmt.setLong(1, getDataFeedID());
            clearStmt.executeUpdate();
            clearStmt.close();
            insertGoogleStmt.setLong(1, getDataFeedID());
            insertGoogleStmt.setString(2, worksheetURL);
            insertGoogleStmt.setString(3, tokenKey);
            insertGoogleStmt.setString(4, tokenSecret);
            insertGoogleStmt.setString(5, refreshToken);
            insertGoogleStmt.setString(6, accessToken);
            insertGoogleStmt.execute();
        } finally {
            insertGoogleStmt.close();
        }
    }

    public void customLoad(Connection conn) throws SQLException {
        PreparedStatement getAnalysisStmt = conn.prepareStatement("SELECT WORKSHEETURL, TOKEN_KEY, TOKEN_SECRET, REFRESH_TOKEN, ACCESS_TOKEN FROM GOOGLE_FEED WHERE " +
                "DATA_FEED_ID = ?");
        try {
            getAnalysisStmt.setLong(1, getDataFeedID());
            ResultSet rs = getAnalysisStmt.executeQuery();
            rs.next();
            this.worksheetURL = rs.getString(1);
            this.tokenKey = rs.getString(2);
            this.tokenSecret = rs.getString(3);
            this.refreshToken = rs.getString(4);
            this.accessToken = rs.getString(5);
        } finally {
            getAnalysisStmt.close();
        }
    }

    @NotNull
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        return populateFields(createDataSet(keys));
    }

    @Override
    public Map<String, Key> newDataSourceFields(FeedDefinition parentDefinition) {
        Map<String, Key> keyMap = new HashMap<String, Key>();
        if (getDataFeedID() == 0) {
        } else {
            for (AnalysisItem field : getFields()) {
                keyMap.put(field.getKey().toKeyString(), field.getKey());
            }
        }
        return keyMap;
    }

    private DataSet createDataSet(Map<String, Key> keys) throws ReportException {
        DataSet dataSet;
        try {
            SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(tokenKey, tokenSecret, accessToken);
            URL listFeedUrl = new URL(worksheetURL);
            ListFeed feed = myService.getFeed(listFeedUrl, ListFeed.class);
            dataSet = new DataSet();
            for (ListEntry listEntry : feed.getEntries()) {
                IRow row = dataSet.createRow();
                boolean atLeastOneValue = false;
                boolean countKeyExists = false;
                for (String tag : listEntry.getCustomElements().getTags()) {
                    if ("count".equals(tag.toLowerCase())) {
                        countKeyExists = true;
                    }
                    Value value;
                    String string = listEntry.getCustomElements().getValue(tag);
                    if (string == null) {
                        value = new EmptyValue();
                    } else {
                        if (string.length() > 0) {
                            atLeastOneValue = true;
                        }
                        value = new StringValue(string);
                    }
                    NamedKey key = (NamedKey) keys.get(tag);
                    if (key == null) {
                        key = new NamedKey(tag);
                        keys.put(tag, key);
                    }
                    row.addValue(new NamedKey(tag), value);
                }
                if (!countKeyExists) {
                    NamedKey countKey = (NamedKey) keys.get("Count");
                    if (countKey == null) {
                        countKey = new NamedKey("Count");
                        keys.put("Count", countKey);
                    }
                    row.addValue(countKey, 1);
                }
                if (!atLeastOneValue) {
                    dataSet.removeRow(row);
                }
            }
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataSet;
    }

    public boolean isConfigured() {
        return worksheetURL != null && !worksheetURL.isEmpty();
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PERSONAL;
    }

    private List<AnalysisItem> populateFields(DataSet dataSet) {
        IDataTypeGuesser guesser = new DataTypeGuesser();
        for (IRow row : dataSet.getRows()) {
            for (Key key : row.getKeys()) {
                Value value = row.getValue(key);
                if (value == null) {
                    value = new EmptyValue();
                }
                guesser.addValue(key, value);
            }
        }
        return guesser.createFeedItems();
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        return new DataSet();
    }

    public FeedType getFeedType() {
        return FeedType.GOOGLE;
    }

    @Override
    public boolean checkDateTime(String name, Key key) {
        for (AnalysisItem field : getFields()) {
            if (field.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension date = (AnalysisDateDimension) field;
                if (field.toOriginalDisplayName().equals(name)) {
                    return !(date.getCustomDateFormat() != null && !date.getCustomDateFormat().contains("H") && !date.getCustomDateFormat().contains("h"));
                }
            }
        }
        return true;
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        return new GoogleSpreadsheetFeed(worksheetURL, tokenKey, tokenSecret, accessToken, refreshToken);
    }

    public GoogleSpreadsheetResponse getPossibleSpreadsheets() throws OAuthException, ServiceException, IOException {
        URL feedUrl = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
        SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(tokenKey, tokenSecret, accessToken);
        SpreadsheetFeed spreadsheetFeed = myService.getFeed(feedUrl, SpreadsheetFeed.class);
        List<Spreadsheet> worksheets = new ArrayList<>();
        for (SpreadsheetEntry entry : spreadsheetFeed.getEntries()) {
            try {
                List<WorksheetEntry> worksheetEntries = entry.getWorksheets();
                List<Worksheet> worksheetList = new ArrayList<Worksheet>();
                for (WorksheetEntry worksheetEntry : worksheetEntries) {
                    String title = worksheetEntry.getTitle().getPlainText();
                    Worksheet worksheet = new Worksheet();
                    worksheet.setSpreadsheet(entry.getTitle().getPlainText());
                    worksheet.setTitle(title);
                    String url = worksheetEntry.getListFeedUrl().toString();
                    worksheet.setUrl(url);
                    worksheetList.add(worksheet);
                }
                Spreadsheet spreadsheet = new Spreadsheet();
                spreadsheet.setTitle(entry.getTitle().getPlainText());
                spreadsheet.setChildren(worksheetList);
                worksheets.add(spreadsheet);
            } catch (Exception e) {
                LogClass.error(e);
                LogClass.debug("Skipping over spreadsheet");
            }
        }
        GoogleSpreadsheetResponse response = new GoogleSpreadsheetResponse();
        response.setSpreadsheets(worksheets);
        return response;
    }
}
