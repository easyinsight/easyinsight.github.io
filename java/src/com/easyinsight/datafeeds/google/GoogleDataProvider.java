package com.easyinsight.datafeeds.google;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.database.Database;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import flex.messaging.FlexContext;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;

import java.sql.SQLException;
import java.util.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;

/**
 * User: jboe
 * Date: Jan 6, 2008
 * Time: 7:44:31 PM
 */
public class GoogleDataProvider {

    private Map<String, List<Spreadsheet>> cachedSpreadsheetResults = new WeakHashMap<String, List<Spreadsheet>>();
    
    private static GoogleDataProvider instance = null;

    public GoogleDataProvider() {
        instance = this;
    }

    public static GoogleDataProvider instance() {
        return instance;
    }

    public GoogleSpreadsheetResponse registerToken(String verifier) {
        EIConnection conn = Database.instance().getConnection();
        try {
            OAuthConsumer consumer = (OAuthConsumer) FlexContext.getHttpRequest().getSession().getAttribute("oauthConsumer");
            OAuthProvider provider = (OAuthProvider) FlexContext.getHttpRequest().getSession().getAttribute("oauthProvider");

            provider.retrieveAccessToken(consumer, verifier.trim());
            String tokenKey = consumer.getToken();
            String tokenSecret = consumer.getTokenSecret();
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO GOOGLE_DOCS_TOKEN (TOKEN_KEY, TOKEN_SECRET, USER_ID) VALUES (?, ?, ?)");
            insertStmt.setString(1, tokenKey);
            insertStmt.setString(2, tokenSecret);
            insertStmt.setLong(3, SecurityUtil.getUserID());
            insertStmt.execute();
            return getAvailableGoogleSpreadsheets(conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public GoogleSpreadsheetResponse getAvailableGoogleSpreadsheets() {
        EIConnection conn = Database.instance().getConnection();
        try {
            return getAvailableGoogleSpreadsheets(conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private GoogleSpreadsheetResponse getAvailableGoogleSpreadsheets(EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT GOOGLE_DOCS_TOKEN.token_key, GOOGLE_DOCS_TOKEN.token_secret FROM " +
                "GOOGLE_DOCS_TOKEN WHERE GOOGLE_DOCS_TOKEN.user_id = ?");
        queryStmt.setLong(1, SecurityUtil.getUserID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            List<Spreadsheet> spreadsheets = getSpreadsheets(rs.getString(1), rs.getString(2));
            return new GoogleSpreadsheetResponse(spreadsheets, true);
        } else {
            return new GoogleSpreadsheetResponse(false);
        }
    }

    private List<Spreadsheet> getSpreadsheets(String tokenKey, String tokenSecret) {
        List<Spreadsheet> worksheets = new ArrayList<Spreadsheet>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existsStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, WORKSHEETURL FROM GOOGLE_FEED, DATA_FEED, UPLOAD_POLICY_USERS " +
                    "WHERE GOOGLE_FEED.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND UPLOAD_POLICY_USERS.FEED_ID = " +
                    "UPLOAD_POLICY_USERS.FEED_ID AND USER_ID = ? AND UPLOAD_POLICY_USERS.USER_ID = ?");
            existsStmt.setLong(1, SecurityUtil.getUserID());
            existsStmt.setInt(2, Roles.OWNER);
            ResultSet rs = existsStmt.executeQuery();
            Map<String, FeedDescriptor> worksheetToFeedMap = new HashMap<String, FeedDescriptor>();
            while (rs.next()) {
                long dataFeedID = rs.getLong(1);
                String worksheetURL = rs.getString(2);
                FeedDescriptor feedDescriptor = new FeedDescriptor();
                feedDescriptor.setId(dataFeedID);
                worksheetToFeedMap.put(worksheetURL, feedDescriptor);
            }
            existsStmt.close();            
            URL feedUrl = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
            SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(tokenKey, tokenSecret);
            SpreadsheetFeed spreadsheetFeed = myService.getFeed(feedUrl, SpreadsheetFeed.class);
            for (SpreadsheetEntry entry : spreadsheetFeed.getEntries()) {
                System.out.println("checking " + entry.getTitle().getPlainText());
                try {
                    List<WorksheetEntry> worksheetEntries = entry.getWorksheets();
                    List<Worksheet> worksheetList = new ArrayList<Worksheet>();
                    for (WorksheetEntry worksheetEntry : worksheetEntries) {
                        String title = worksheetEntry.getTitle().getPlainText();
                        Worksheet worksheet = new Worksheet();
                        worksheet.setSpreadsheet(entry.getTitle().getPlainText());
                        worksheet.setTitle(title);
                        String url = worksheetEntry.getListFeedUrl().toString();
                        System.out.println("*** " + title + " - " + url);
                        worksheet.setUrl(url);
                        worksheet.setFeedDescriptor(worksheetToFeedMap.get(url));
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
        } catch (Exception e) {
            throw new RuntimeException(e);        
        } finally {
            Database.closeConnection(conn);
        }
        return worksheets;
    }
}
