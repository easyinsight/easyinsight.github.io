package com.easyinsight.users;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;


/**
 * User: James Boe
 * Date: Jun 24, 2008
 * Time: 12:20:16 PM
 */
public class AccountAdminTO {
    private int accountType;
    private long accountID;
    private int maxUsers;
    private long maxSize;
    private String name;
    private int accountState;
    private long groupID;
    private List<UserTransferObject> adminUsers = new ArrayList<UserTransferObject>();
    private List<UserTransferObject> allUsers = new ArrayList<UserTransferObject>();
    private boolean apiEnabled;
    private Date creationDate;
    private Date lastUserLoginDate;
    private long defaultPersonaID;
    private boolean optIn;
    private Date trialDate;

    public List<UserTransferObject> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<UserTransferObject> allUsers) {
        this.allUsers = allUsers;
    }

    public Date getTrialDate() {
        return trialDate;
    }

    public void setTrialDate(Date trialDate) {
        this.trialDate = trialDate;
    }

    public boolean isOptIn() {
        return optIn;
    }

    public void setOptIn(boolean optIn) {
        this.optIn = optIn;
    }

    public long getDefaultPersonaID() {
        return defaultPersonaID;
    }

    public void setDefaultPersonaID(long defaultPersonaID) {
        this.defaultPersonaID = defaultPersonaID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUserLoginDate() {
        return lastUserLoginDate;
    }

    public void setLastUserLoginDate(Date lastUserLoginDate) {
        this.lastUserLoginDate = lastUserLoginDate;
    }

    public boolean isApiEnabled() {
        return apiEnabled;
    }

    public void setApiEnabled(boolean apiEnabled) {
        this.apiEnabled = apiEnabled;
    }

    public List<UserTransferObject> getAdminUsers() {
        return adminUsers;
    }

    public void setAdminUsers(List<UserTransferObject> adminUsers) {
        this.adminUsers = adminUsers;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
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

    public Account toAccount(Account account) {
        account.setAccountType(accountType);
        account.setAccountID(accountID);
        if (groupID != 0) {
            account.setGroupID(groupID);
        }
        account.setName(name);
        account.setCreationDate(creationDate);
        account.setAccountState(accountState);
        account.setMaxSize(maxSize);
        account.setMaxUsers(maxUsers);
        account.setOptInEmail(optIn);
        account.setAccountState(accountState);
        account.setApiEnabled(apiEnabled);
        return account;
    }
}
