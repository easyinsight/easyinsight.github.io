package com.easyinsight.users;

/**
 * User: jamesboe
 * Date: May 3, 2010
 * Time: 3:23:33 PM
 */
public class WelcomeBackInfo {
    private String firstName;
    private String lastName;
    private String accountName;
    private boolean optIn;
    private int accountTier;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean isOptIn() {
        return optIn;
    }

    public void setOptIn(boolean optIn) {
        this.optIn = optIn;
    }

    public int getAccountTier() {
        return accountTier;
    }

    public void setAccountTier(int accountTier) {
        this.accountTier = accountTier;
    }
}
