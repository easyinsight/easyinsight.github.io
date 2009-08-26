package com.easyinsight.users;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.datafeeds.FeedType;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.AuthenticationException;

import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Iterator;

/**
 * User: jamesboe
 * Date: Aug 24, 2009
 * Time: 2:25:19 PM
 */
public class TokenService {

    public String setToken(int type, String sessionToken) {
        /*System.out.println("got URL " + url);
        String queryString = url.substring(url.indexOf("?"));*/
        
        Token tokenObject = new Token();
        tokenObject.setUserID(SecurityUtil.getUserID());
        tokenObject.setTokenType(type);
        tokenObject.setTokenValue(sessionToken);
        new TokenStorage().saveToken(tokenObject);
        return null;
    }

    public boolean isTokenEstablished(int type) {
        try {
            Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), type);
            return token != null;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public String getAuthSubURL(int type) {
        try {
            String nextURL = "https://staging.easy-insight.com/app?sourceType=" + type;
            FeedType feedType = new FeedType(type);
            String scope;
            if (feedType.equals(FeedType.GOOGLE_ANALYTICS)) {
                scope = "https://www.google.com/analytics/feeds/";
            } else if (feedType.equals(FeedType.GOOGLE)) {
                scope = "http://spreadsheets.google.com/feeds/";
            } else {
                throw new RuntimeException("Unknown type for authorization " + type);
            }
            return AuthSubUtil.getRequestUrl(nextURL, scope, false, true);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public String test() {
        try {
            SpreadsheetService spreadsheetService = new SpreadsheetService("easyinsight-eidocs-1");
            Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.GOOGLE_DOCS_TOKEN);
            if (token != null) {
                spreadsheetService.setAuthSubToken(token.getTokenValue());
                SpreadsheetFeed spreadsheetFeed;
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
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
