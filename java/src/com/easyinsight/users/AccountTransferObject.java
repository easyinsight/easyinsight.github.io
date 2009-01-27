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
    private AccountType accountType;
    //private List<UserTransferObject> users = new ArrayList<UserTransferObject>();
    private long accountID;
    private List<SubscriptionLicense> licenses = new ArrayList<SubscriptionLicense>();
    private int maxUsers;
    private long maxSize;
    //private BillingParty billingParty;

    /*public BillingParty getBillingParty() {
        return billingParty;
    }

    public void setBillingParty(BillingParty billingParty) {
        this.billingParty = billingParty;
    }*/

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

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    /*public List<UserTransferObject> getUsers() {
        return users;
    }

    public void setUsers(List<UserTransferObject> users) {
        this.users = users;
    } */

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
        account.setAccountType(accountType.getAccountType());
        account.setLicenses(licenses);
        account.setAccountID(accountID);
        return account;
    }
}
