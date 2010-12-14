package com.easyinsight.datafeeds.google;

import com.easyinsight.config.ConfigLoader;
import com.easyinsight.datafeeds.*;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.database.Database;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;

import java.util.*;
import java.net.URL;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
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

    private String getToken() {
        Token tokenObject = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.GOOGLE_DOCS_TOKEN);
        if (tokenObject == null) {
            if (ConfigLoader.instance().getGoogleUserName() != null && !"".equals(ConfigLoader.instance().getGoogleUserName())) {
                return null;
            } else {
                throw new RuntimeException("Token access revoked?");
            }
        }
        return tokenObject.getTokenValue();
    }

    public List<Spreadsheet> getAvailableGoogleSpreadsheets() {
        String token = getToken();
        List<Spreadsheet> worksheets = cachedSpreadsheetResults.get(token);
        if (worksheets == null) {
            try {
                worksheets = getSpreadsheets(token);
                cachedSpreadsheetResults.put(token, worksheets);
                return worksheets;
            } catch (Exception e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            }
        }
        return worksheets;
    }

    private List<Spreadsheet> getSpreadsheets(String token) throws AuthenticationException {
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
            URL feedUrl = new URL("http://spreadsheets.google.com/feeds/spreadsheets/private/full");
            SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(token);
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
