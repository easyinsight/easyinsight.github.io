package com.easyinsight.users;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.logging.LogClass;
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

    public String setToken(int type, String url) {
        System.out.println("got URL " + url);
        String queryString = url.substring(url.indexOf("?"));
        String token = AuthSubUtil.getTokenFromReply(queryString);
        if (token == null) {
          return "No token specified.";

        }

        // Exchange the token for a session token
        String sessionToken;
        try {
          sessionToken =
            AuthSubUtil.exchangeForSessionToken(token,
                                                Utility.getPrivateKey());
        } catch (IOException e1) {
          return "Exception retrieving session token.";
        } catch (GeneralSecurityException e1) {
          return "Security error while retrieving session token.";
        } catch (AuthenticationException e) {
          return "Server rejected one time use token.";
        }

        try {
          // Sanity checking usability of token
          Map<String, String> info =
            AuthSubUtil.getTokenInfo(sessionToken, Utility.getPrivateKey());
          for (Iterator<String> iter = info.keySet().iterator(); iter.hasNext();) {
            String key = iter.next();
            System.out.println("\t(key, value): (" + key + ", " + info.get(key)
                               + ")");
          }
        } catch (IOException e1) {
          return "Exception retrieving info for session token.";
        } catch (GeneralSecurityException e1) {
          return "Security error while retrieving session token info.";
        } catch (AuthenticationException e) {
          return "Auth error retrieving info for session token: " +
                         e.getMessage();
        }

        // Retrieve the authentication cookie to identify user
        /*String principal = Utility.getCookieValueWithName(req.getCookies(), Utility.LOGIN_COOKIE_NAME);
        if (principal == null) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                         "Unidentified principal.");
          return;
        }*/
        //SecurityUtil.getUserID(false);

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

    public String getAuthSubURL() {
        try {
            String nextURL = "https://staging.easy-insight.com/app/#redirectID=1";
            String scope = "http://spreadsheets.google.com/feeds/";
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
