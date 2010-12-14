package com.easyinsight.datafeeds.google;

import com.google.gdata.client.authn.oauth.*;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.util.AuthenticationException;

/**
 * User: James Boe
 * Date: May 16, 2008
 * Time: 8:46:23 PM
 */
public class GoogleSpreadsheetAccess {



    public static SpreadsheetService getOrCreateSpreadsheetService(String oauthToken, String oauthTokenSecret) throws AuthenticationException, OAuthException {
        SpreadsheetService spreadsheetService = new SpreadsheetService("easyinsight-eidocs-1");
        spreadsheetService.useSsl();
        GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
        oauthParameters.setOAuthConsumerKey("www.easy-insight.com");
        oauthParameters.setOAuthConsumerSecret("OG0zlkZFPIe7JdHfLB8qXXYv");
        oauthParameters.setOAuthToken(oauthToken);
        oauthParameters.setOAuthTokenSecret(oauthTokenSecret);
        oauthParameters.setScope("https://spreadsheets.google.com/feeds/");
        OAuthSigner signer = new OAuthHmacSha1Signer();
        spreadsheetService.setOAuthCredentials(oauthParameters, signer);
        return spreadsheetService;
    }
}
