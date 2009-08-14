package com.easyinsight.datafeeds.google;

import com.easyinsight.datafeeds.*;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.MalformedCredentialsException;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.database.Database;
import com.easyinsight.userupload.CredentialsResponse;
import com.easyinsight.PasswordStorage;
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

    private Map<Credentials, List<Spreadsheet>> cachedSpreadsheetResults = new WeakHashMap<Credentials, List<Spreadsheet>>();
    
    private static GoogleDataProvider instance = null;

    public GoogleDataProvider() {
        instance = this;
    }

    public static GoogleDataProvider instance() {
        return instance;
    }

    public CredentialsResponse testGoogleConnect(Credentials credentials, boolean encrypt) {
        CredentialsResponse credentialsResponse;
        try {
            GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(credentials);
            credentialsResponse = new CredentialsResponse(true);
            if (encrypt) {
                credentialsResponse.setEncryptedResponse(encryptCredentials(credentials));
            }            
        } catch (AuthenticationException ae) {
            credentialsResponse = new CredentialsResponse(false, ae.getMessage());
        } catch (Exception e) {
            LogClass.error(e);
            credentialsResponse = new CredentialsResponse(false, e.getMessage());
        }
        return credentialsResponse;
    }

    public Credentials encryptCredentials(Credentials creds) {
        Credentials c = new Credentials();
        c.setUserName(PasswordStorage.encryptString(creds.getUserName() + ":" + SecurityUtil.getUserName()));
        c.setPassword(PasswordStorage.encryptString(creds.getPassword() + ":" + SecurityUtil.getUserName()));
        c.setEncrypted(true);
        return c;
    }

    private Credentials decryptCredentials(Credentials creds) {
        Credentials c = new Credentials();
        String s = PasswordStorage.decryptString(creds.getUserName());
        int i = s.lastIndexOf(":" + SecurityUtil.getUserName());
        if(i == -1) {
            throw new RuntimeException();
        }
        c.setUserName(s.substring(0, i));
        s = PasswordStorage.decryptString(creds.getPassword());
        i = s.lastIndexOf(":" + SecurityUtil.getUserName());
        if(i == -1)
            throw new RuntimeException();
        c.setPassword(s.substring(0, i));
        return c;
    }

    public List<Spreadsheet> getAvailableGoogleSpreadsheets(Credentials credentials) {
        if (credentials.isEncrypted()) {
            credentials = decryptCredentials(credentials);
        }
        List<Spreadsheet> worksheets = cachedSpreadsheetResults.get(credentials);
        if (worksheets == null) {
            try {
                worksheets = getSpreadsheets(credentials);
                cachedSpreadsheetResults.put(credentials, worksheets);
                return worksheets;
            } catch (Exception e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            }
        }
        return worksheets;
    }

    private List<Spreadsheet> getSpreadsheets(Credentials credentials) throws AuthenticationException {
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
                feedDescriptor.setDataFeedID(dataFeedID);
                worksheetToFeedMap.put(worksheetURL, feedDescriptor);
            }
            existsStmt.close();            
            URL feedUrl = new URL("http://spreadsheets.google.com/feeds/spreadsheets/private/full");
            SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(credentials);
            SpreadsheetFeed spreadsheetFeed = myService.getFeed(feedUrl, SpreadsheetFeed.class);
            for (SpreadsheetEntry entry : spreadsheetFeed.getEntries()) {
                List<WorksheetEntry> worksheetEntries = entry.getWorksheets();
                List<Worksheet> worksheetList = new ArrayList<Worksheet>();
                for (WorksheetEntry worksheetEntry : worksheetEntries) {
                    String title = worksheetEntry.getTitle().getPlainText();
                    Worksheet worksheet = new Worksheet();
                    worksheet.setSpreadsheet(entry.getTitle().getPlainText());
                    worksheet.setTitle(title);
                    String url = worksheetEntry.getListFeedUrl().toString();
                    worksheet.setUrl(url);
                    worksheet.setFeedDescriptor(worksheetToFeedMap.get(url));
                    worksheetList.add(worksheet);
                }
                Spreadsheet spreadsheet = new Spreadsheet();
                spreadsheet.setTitle(entry.getTitle().getPlainText());
                spreadsheet.setChildren(worksheetList);
                worksheets.add(spreadsheet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return worksheets;
    }
}
