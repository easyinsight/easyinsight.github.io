package com.easyinsight.users;

import org.hibernate.Session;

import java.util.List;
import java.util.ArrayList;

import com.easyinsight.util.RandomTextGenerator;

/**
 * User: James Boe
 * Date: Jun 24, 2008
 * Time: 12:20:16 PM
 */
public class AccountTransferObject {
    private int accountType;
    private long accountID;
    private List<SubscriptionLicense> licenses = new ArrayList<SubscriptionLicense>();
    private int maxUsers;
    private long maxSize;
    private String name;
    private int accountState;
    private boolean apiEnabled;

    public boolean isApiEnabled() {
        return apiEnabled;
    }

    public void setApiEnabled(boolean apiEnabled) {
        this.apiEnabled = apiEnabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAccountState() {
        return accountState;
    }

    public void setAccountState(int accountState) {
        this.accountState = accountState;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public List<SubscriptionLicense> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<SubscriptionLicense> licenses) {
        this.licenses = licenses;
    }

    public Account toAccount() {
        Account account = new Account();
        account.setAccountType(accountType);
        account.setLicenses(licenses);
        account.setAccountID(accountID);
        account.setName(name);
        account.setApiEnabled(apiEnabled);
        return account;
    }
}
