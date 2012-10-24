package com.easyinsight.users;

import java.util.Date;

/**
 * User: James Boe
 * Date: Jun 24, 2008
 * Time: 12:20:16 PM
 */
public class AccountTransferObject {
    private int accountType;
    private long accountID;
    private int maxUsers;
    private long maxSize;
    private String name;
    private int accountState;
    private boolean apiEnabled;
    private Date creationDate;
    private boolean optInEmail;
    private String googleAppsDomain;

    public String getGoogleAppsDomain() {
        return googleAppsDomain;
    }

    public void setGoogleAppsDomain(String googleAppsDomain) {
        this.googleAppsDomain = googleAppsDomain;
    }

    public String getSnappCloudId() {
        return snappCloudId;
    }

    public void setSnappCloudId(String snappCloudId) {
        this.snappCloudId = snappCloudId;
    }

    private String snappCloudId;

    public boolean isOptInEmail() {
        return optInEmail;
    }

    public void setOptInEmail(boolean optInEmail) {
        this.optInEmail = optInEmail;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

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

    public Account toAccount() {
        Account account = new Account();
        account.setGoogleDomainName(googleAppsDomain);
        account.setMaxUsers(maxUsers);
        account.setMaxSize(maxSize);
        account.setOptInEmail(optInEmail);
        account.setAccountState(accountState);
        account.setAccountType(accountType);
        account.setAccountID(accountID);
        account.setName(name);
        account.setApiEnabled(apiEnabled);
        account.setCreationDate(creationDate);
        account.setSnappCloudId(snappCloudId);
        return account;
    }
}
