package com.easyinsight.users;

/**
 * User: James Boe
 * Date: Apr 13, 2009
 * Time: 9:38:01 AM
 */
public class AccountAPISettings {
    private String accountKey;
    private String accountSecretKey;
    private boolean uncheckedAPIEnabled;
    private boolean validatedAPIEnabled;
    private boolean dynamicAPIEnabled;

    public AccountAPISettings() {
    }

    public AccountAPISettings(String accountKey, String accountSecretKey, boolean uncheckedAPIEnabled,
                              boolean validatedAPIEnabled, boolean dynamicAPIEnabled) {
        this.accountKey = accountKey;
        this.accountSecretKey = accountSecretKey;
        this.uncheckedAPIEnabled = uncheckedAPIEnabled;
        this.validatedAPIEnabled = validatedAPIEnabled;
        this.dynamicAPIEnabled = dynamicAPIEnabled;
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

    public boolean isUncheckedAPIEnabled() {
        return uncheckedAPIEnabled;
    }

    public void setUncheckedAPIEnabled(boolean uncheckedAPIEnabled) {
        this.uncheckedAPIEnabled = uncheckedAPIEnabled;
    }

    public boolean isValidatedAPIEnabled() {
        return validatedAPIEnabled;
    }

    public void setValidatedAPIEnabled(boolean validatedAPIEnabled) {
        this.validatedAPIEnabled = validatedAPIEnabled;
    }

    public boolean isDynamicAPIEnabled() {
        return dynamicAPIEnabled;
    }

    public void setDynamicAPIEnabled(boolean dynamicAPIEnabled) {
        this.dynamicAPIEnabled = dynamicAPIEnabled;
    }
}
