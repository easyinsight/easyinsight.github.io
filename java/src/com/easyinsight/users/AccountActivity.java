package com.easyinsight.users;

import java.util.*;

/**
 * User: James Boe
 * Date: Apr 10, 2009
 * Time: 4:49:14 PM
 */
public class AccountActivity {
    public static final int ACCOUNT_CREATED = 1;
    public static final int ACCOUNT_UPGRADED = 2;
    public static final int ACCOUNT_DOWNGRADED = 3;
    public static final int ACCOUNT_RECURRING = 4;
    public static final int USER_LICENSE_ADDED = 5;
    public static final int USER_LICENSE_REMOVED = 6;

    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

    private int accountType;
    private Date timestamp;
    private long accountID;
    private int userLicenses;
    private int activityID;
    private String activityNotes;

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public int getUserLicenses() {
        return userLicenses;
    }

    public void setUserLicenses(int userLicenses) {
        this.userLicenses = userLicenses;
    }

    public int getActivityID() {
        return activityID;
    }

    public void setActivityID(int activityID) {
        this.activityID = activityID;
    }

    public String getActivityNotes() {
        return activityNotes;
    }

    public void setActivityNotes(String activityNotes) {
        this.activityNotes = activityNotes;
    }

    public AccountActivity() {

    }

    public AccountActivity(int accountType, Date timestamp, long accountID, int userLicenses, int activityID, String activityNotes) {
        this.accountType = accountType;
        this.timestamp = timestamp;
        this.accountID = accountID;
        this.userLicenses = userLicenses;
        this.activityID = activityID;
        this.activityNotes = activityNotes;
    }
}
