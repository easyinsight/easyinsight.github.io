package com.easyinsight.users;

/**
 * User: James Boe
 * Date: Apr 13, 2009
 * Time: 9:38:01 AM
 */
public class AccountSettings {
    private boolean apiEnabled;

    public AccountSettings() {
    }

    public boolean isApiEnabled() {
        return apiEnabled;
    }

    public void setApiEnabled(boolean apiEnabled) {
        this.apiEnabled = apiEnabled;
    }
}