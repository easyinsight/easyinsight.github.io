package com.easyinsight.users;

/**
 * User: James Boe
 * Date: Apr 13, 2009
 * Time: 9:38:01 AM
 */
public class AccountAPISettings {
    private String accountKey;
    private String accountSecretKey;
    private String userKey;
    private String userSecretKey;
    private boolean apiEnabled;

    public AccountAPISettings() {
    }

    public AccountAPISettings(String accountKey, String accountSecretKey, String userKey, String userSecretKey,
                              boolean apiEnabled) {
        this.accountKey = accountKey;
        this.accountSecretKey = accountSecretKey;
        this.userKey = userKey;
        this.userSecretKey = userSecretKey;
        this.apiEnabled = apiEnabled;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserSecretKey() {
        return userSecretKey;
    }

    public void setUserSecretKey(String userSecretKey) {
        this.userSecretKey = userSecretKey;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getAccountSecretKey() {
        return accountSecretKey;
    }

    public void setAccountSecretKey(String accountSecretKey) {
        this.accountSecretKey = accountSecretKey;
    }

    public boolean isApiEnabled() {
        return apiEnabled;
    }

    public void setApiEnabled(boolean apiEnabled) {
        this.apiEnabled = apiEnabled;
    }
}
