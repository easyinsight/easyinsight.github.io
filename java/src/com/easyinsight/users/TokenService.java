package com.easyinsight.users;

import com.easyinsight.security.SecurityUtil;
import com.google.gdata.client.http.AuthSubUtil;

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
        String nextURL = "https://www.easy-insight.com/app/TokenRedirect";
        String scope = "http://spreadsheets.google.com/feeds/spreadsheets";
        return AuthSubUtil.getRequestUrl(nextURL, scope, false, true);
    }

    public void test() {
        
    }
}
