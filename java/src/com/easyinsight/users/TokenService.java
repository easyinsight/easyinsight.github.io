package com.easyinsight.users;

import com.easyinsight.security.SecurityUtil;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.util.ServiceException;

import java.net.URL;
import java.io.IOException;

/**
 * User: jamesboe
 * Date: Aug 24, 2009
 * Time: 2:25:19 PM
 */
public class TokenService {
    public boolean isTokenEstablished(int type) {
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), type);
        return token != null;
    }

    public String getAuthSubURL() {
        String nextURL = "https://staging.easy-insight.com/app/TokenRedirect";
        String scope = "http://spreadsheets.google.com/feeds/";
        return AuthSubUtil.getRequestUrl(nextURL, scope, false, true);
    }

    public String test() {
        SpreadsheetService spreadsheetService = new SpreadsheetService("easyinsight-eidocs-1");
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.GOOGLE_DOCS_TOKEN);
        if (token != null) {
            spreadsheetService.setAuthSubToken(token.getTokenValue());
            SpreadsheetFeed spreadsheetFeed = null;
            try {
                URL feedUrl = new URL("http://spreadsheets.google.com/feeds/spreadsheets/private/full");
                spreadsheetFeed = spreadsheetService.getFeed(feedUrl, SpreadsheetFeed.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            int entrySize = spreadsheetFeed.getEntries().size();
            return String.valueOf(entrySize);
        } else {
            return "Couldn't get in";
        }
    }
}
