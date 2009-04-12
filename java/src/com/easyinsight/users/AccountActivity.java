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

    // if upgraded, what did it get upgraded to...

    // if downgraded, what did it get downgraded to...

    public static void main(String[] args) {
        Calendar testCal = Calendar.getInstance();
        testCal.set(Calendar.MONTH, 2);
        testCal.set(Calendar.DAY_OF_MONTH, 1);
        List<AccountActivity> accountActivities = new ArrayList<AccountActivity>();
        accountActivities.add(new AccountActivity(Account.INDIVIDUAL, testCal.getTime(), 1, 1));
        testCal.set(Calendar.MONTH, 2);
        testCal.set(Calendar.DAY_OF_MONTH, 15);
        accountActivities.add(new AccountActivity(Account.PROFESSIONAL, testCal.getTime(), 1, 1));
        Calendar cal = Calendar.getInstance();
        ListIterator<AccountActivity> iter = accountActivities.listIterator(accountActivities.size());
        List<BillingWindow> billingWindows = new ArrayList<BillingWindow>();
        int previousActivityLength = 0;
        while (iter.hasPrevious()) {
            AccountActivity previous = iter.previous();
            cal.setTime(previous.timestamp);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH) - (dayOfMonth - 1) - previousActivityLength;
            billingWindows.add(new BillingWindow(days, previous.accountType, previous.userLicenses));
            previousActivityLength = days;
        }

        double cost = calculateCost(billingWindows, cal);

        System.out.println("cost = " + cost);
    }

    private static double calculateCost(List<BillingWindow> billingWindows, Calendar cal) {
        double sum = 0;
        for (BillingWindow billingWindow : billingWindows) {
            sum += billingWindow.computeCost(cal);
        }
        return sum;
    }

    private static class BillingWindow {
        private double days;
        private int accountType;
        private int licenses;

        private BillingWindow(int days, int accountType, int licenses) {
            this.days = days;
            this.accountType = accountType;
            this.licenses = licenses;
        }

        public double computeCost(Calendar cal) {
            double baseCost = calculateCost(accountType, licenses);
            double daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            double proRatedTime = days / daysInMonth;
            return baseCost * proRatedTime;
        }
    }

    private static double calculateCost(int accountType, int licenses) {
        double baseAccountMonthlyCost;
        double userLicenseCost;
        switch (accountType) {
            case Account.INDIVIDUAL:
                baseAccountMonthlyCost = 0;
                userLicenseCost = 10;
                break;
            case Account.PROFESSIONAL:
                baseAccountMonthlyCost = 0;
                userLicenseCost = 50;
                break;
            case Account.ENTERPRISE:
                baseAccountMonthlyCost = 200;
                userLicenseCost = 100;
                break;
            default:
                throw new RuntimeException();
        }
        return baseAccountMonthlyCost + (userLicenseCost * licenses);
    }
}
