package com.easyinsight.datafeeds.google;

import com.easyinsight.users.Credentials;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.util.AuthenticationException;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * User: James Boe
 * Date: May 16, 2008
 * Time: 8:46:23 PM
 */
public class GoogleSpreadsheetAccess {

    private static Map<Credentials, SpreadsheetService> cacheSpreadsheetServices = new WeakHashMap<Credentials, SpreadsheetService>();

    public static SpreadsheetService getOrCreateSpreadsheetService(Credentials credentials) throws AuthenticationException {
        SpreadsheetService spreadsheetService = cacheSpreadsheetServices.get(credentials);
        if (spreadsheetService == null) {
            spreadsheetService = new SpreadsheetService("nonameyet-startingthingsup-1");
            spreadsheetService.setUserCredentials(credentials.getUserName(), credentials.getPassword());
            cacheSpreadsheetServices.put(credentials, spreadsheetService);
        }
        return spreadsheetService;
    }
}
