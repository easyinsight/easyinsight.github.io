package com.easyinsight.datafeeds.google;

import java.util.List;

/**
 * User: jamesboe
 * Date: 12/14/10
 * Time: 1:32 PM
 */
public class GoogleSpreadsheetResponse {
    private List<Spreadsheet> spreadsheets;
    private boolean validOAuth;

    public GoogleSpreadsheetResponse(List<Spreadsheet> spreadsheets, boolean validOAuth) {
        this.spreadsheets = spreadsheets;
        this.validOAuth = validOAuth;
    }

    public GoogleSpreadsheetResponse(boolean validOAuth) {
        this.validOAuth = validOAuth;
    }

    public GoogleSpreadsheetResponse() {
    }

    public List<Spreadsheet> getSpreadsheets() {
        return spreadsheets;
    }

    public void setSpreadsheets(List<Spreadsheet> spreadsheets) {
        this.spreadsheets = spreadsheets;
    }

    public boolean isValidOAuth() {
        return validOAuth;
    }

    public void setValidOAuth(boolean validOAuth) {
        this.validOAuth = validOAuth;
    }
}
