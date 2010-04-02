package com.easyinsight.datafeeds.google;

import com.easyinsight.config.ConfigLoader;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.Utility;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;

import java.util.Map;
import java.util.WeakHashMap;
import java.net.URL;

/**
 * User: James Boe
 * Date: May 16, 2008
 * Time: 8:46:23 PM
 */
public class GoogleSpreadsheetAccess {

    private static Map<String, SpreadsheetService> cacheSpreadsheetServices = new WeakHashMap<String, SpreadsheetService>();

    public static SpreadsheetService getOrCreateSpreadsheetService(String token) throws AuthenticationException {
        SpreadsheetService spreadsheetService = cacheSpreadsheetServices.get(token);
        if (spreadsheetService == null) {
            spreadsheetService = new SpreadsheetService("easyinsight-eidocs-1");
            if (token == null) {
                if (ConfigLoader.instance().getGoogleUserName() != null && !"".equals(ConfigLoader.instance().getGoogleUserName())) {
                    spreadsheetService.setUserCredentials(ConfigLoader.instance().getGoogleUserName(), ConfigLoader.instance().getGooglePassword());
                }
            } else {
                spreadsheetService.setAuthSubToken(token, Utility.getPrivateKey());
            }
            cacheSpreadsheetServices.put(token, spreadsheetService);
        }
        return spreadsheetService;
    }

    public static void main(String[] args) throws Exception {
        SpreadsheetService spreadsheetService = new SpreadsheetService("easyinsight-eidocs-1");
        spreadsheetService.setAuthSubToken("CKCBo-7SChDpzLDhBw");
        SpreadsheetFeed spreadsheetFeed = null;
        try {
            URL feedUrl = new URL("http://spreadsheets.google.com/feeds/spreadsheets/private/full");
            spreadsheetFeed = spreadsheetService.getFeed(feedUrl, SpreadsheetFeed.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int entrySize = spreadsheetFeed.getEntries().size();
    }
}
