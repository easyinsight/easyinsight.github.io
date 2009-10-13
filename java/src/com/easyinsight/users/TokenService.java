package com.easyinsight.users;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.config.ConfigLoader;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;

/**
 * User: jamesboe
 * Date: Aug 24, 2009
 * Time: 2:25:19 PM
 */
public class TokenService {

    public List<TokenSpecification> getTokenSpecifications() {
        List<TokenSpecification> tokenSpecs = new ArrayList<TokenSpecification>();
        TokenStorage tokenStorage = new TokenStorage();
        TokenSpecification gDocsSpec = new TokenSpecification();
        gDocsSpec.setName("Google Spreadsheets");
        gDocsSpec.setType(TokenStorage.GOOGLE_DOCS_TOKEN);
        Token gDocsToken = tokenStorage.getToken(SecurityUtil.getUserID(), TokenStorage.GOOGLE_DOCS_TOKEN);
        gDocsSpec.setDefined(gDocsToken != null);
        TokenSpecification gAnalyticsSpec = new TokenSpecification();
        gAnalyticsSpec.setName("Google Analytics");
        gAnalyticsSpec.setType(TokenStorage.GOOGLE_ANALYTICS_TOKEN);
        Token gAnalyticsToken = tokenStorage.getToken(SecurityUtil.getUserID(), TokenStorage.GOOGLE_ANALYTICS_TOKEN);
        gAnalyticsSpec.setDefined(gAnalyticsToken != null);
        gDocsSpec.setUrlToConfigure(getAuthSubURL(TokenStorage.GOOGLE_DOCS_TOKEN, 0));
        gAnalyticsSpec.setUrlToConfigure(getAuthSubURL(TokenStorage.GOOGLE_ANALYTICS_TOKEN, 0));
        tokenSpecs.add(gDocsSpec);
        tokenSpecs.add(gAnalyticsSpec);
        return tokenSpecs;
    }

    public void deleteToken(int type) {
        try {
            Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), type);
            if (token != null) {
                new TokenStorage().deleteToken(token);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

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

    public String getAuthSubURL(int type, long solution) {
        try {
            String nextURL;
            if (ConfigLoader.instance().isProduction()) {
                nextURL = "https://www.easy-insight.com/app/TokenRedirect?sourceType=" + type + "?solutionID=" + solution;
            } else {
                nextURL = "https://staging.easy-insight.com/app/TokenRedirect?sourceType=" + type + "?solutionID=" + solution;
            }
            FeedType feedType = new FeedType(type);
            String scope;
            if (feedType.equals(FeedType.GOOGLE_ANALYTICS)) {
                scope = "https://www.google.com/analytics/feeds/";
            } else if (feedType.equals(FeedType.GOOGLE)) {
                scope = "http://spreadsheets.google.com/feeds/";
            } else {
                throw new RuntimeException("Unknown type for authorization " + type);
            }
            return AuthSubUtil.getRequestUrl(nextURL, scope, true, true);
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
                spreadsheetService.setAuthSubToken(token.getTokenValue(), Utility.getPrivateKey());
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
