package com.easyinsight.users;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * User: James Boe
 * Date: Apr 10, 2009
 * Time: 8:35:06 PM
 */
public class AccountActivityStorage {
    public void saveAccountActivity(AccountActivity accountActivity) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO ACCOUNT_ACTIVITY (" +
                    "ACTIVITY_DATE, USER_LICENSES, ACCOUNT_TYPE, ACCOUNT_ID, ACTIVITY_TYPE, ACTIVITY_NOTES) VALUES (?, ?, ?, ?, ?, ?)");
            insertStmt.setTimestamp(1, new java.sql.Timestamp(accountActivity.getTimestamp().getTime()));
            insertStmt.setInt(2, accountActivity.getUserLicenses());
            insertStmt.setInt(3, accountActivity.getAccountType());
            insertStmt.setLong(4, accountActivity.getAccountID());
            insertStmt.setInt(5, accountActivity.getActivityID());
            insertStmt.setString(6, accountActivity.getActivityNotes());
            insertStmt.execute();
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.instance().closeConnection(conn);
        }

    }

    private Map<Long, List<AccountActivity>> getActivitiesForMonth(Calendar cal, Connection conn) {
        Map<Long, List<AccountActivity>> activityMap = new HashMap<Long, List<AccountActivity>>();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ACTIVITY_DATE, USER_LICENSES, ACCOUNT_TYPE," +
                    "ACCOUNT_ID, ACTIVITY_TYPE, ACTIVITY_NOTES FROM ACCOUNT_ACTIVITY WHERE MONTH(ACTIVITY_DATE) = ? AND " +
                    "YEAR(ACTIVITY_DATE) = ?");
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            queryStmt.setInt(1, month);
            queryStmt.setInt(2, year);
            ResultSet accountRS = queryStmt.executeQuery();
            while (accountRS.next()) {
                long accountID = accountRS.getLong(4);
                List<AccountActivity> activityList = activityMap.get(accountID);
                if (activityList == null) {
                    activityList = new ArrayList<AccountActivity>();
                    activityMap.put(accountID, activityList);
                }
                activityList.add(new AccountActivity(accountRS.getInt(3), new Date(accountRS.getTimestamp(1).getTime()), accountRS.getLong(4),
                        accountRS.getInt(2), accountRS.getInt(5), accountRS.getString(6)));
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return activityMap;
    }

    public void monthlyRun() {
        EIConnection conn = Database.instance().getConnection();
        try {
            Date now = new Date();
            java.sql.Timestamp timestamp = new Timestamp(now.getTime());
            conn.setAutoCommit(false);
            PreparedStatement insertPaymentStmt = conn.prepareStatement("INSERT INTO ACCOUNT_PAYMENT (PAYMENT_REQUIRED," +
                    "BILLING_DATE, PAYMENT_MADE, ACCOUNT_ID) VALUES (?, ?, ?, ?)");
            Map<Long, Double> costMap = calculateCostMap(conn);
            for (Map.Entry<Long, Double> costEntry : costMap.entrySet()) {
                Long accountID = costEntry.getKey();
                Double cost = costEntry.getValue();
                insertPaymentStmt.setDouble(1, cost);
                insertPaymentStmt.setTimestamp(2, timestamp);
                insertPaymentStmt.setBoolean(3, false);
                insertPaymentStmt.setLong(4, accountID);
                insertPaymentStmt.execute();
            }
            conn.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.instance().closeConnection(conn);
        }
    }

    private Map<Long, Double> calculateCostMap(Connection conn) {
        Map<Long, Double> costMap = new HashMap<Long, Double>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Map<Long, List<AccountActivity>> activityMap = getActivitiesForMonth(cal, conn);
        for (Map.Entry<Long, List<AccountActivity>> entry : activityMap.entrySet()) {
            long accountID = entry.getKey();
            List<AccountActivity> accountActivities = entry.getValue();
            ListIterator<AccountActivity> iter = accountActivities.listIterator(accountActivities.size());
            List<BillingWindow> billingWindows = new ArrayList<BillingWindow>();
            int previousActivityLength = 0;
            while (iter.hasPrevious()) {
                AccountActivity previous = iter.previous();
                cal.setTime(previous.getTimestamp());
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH) - (dayOfMonth - 1) - previousActivityLength;
                billingWindows.add(new BillingWindow(days, previous.getAccountType(), previous.getUserLicenses()));
                previousActivityLength = days;
            }
            double cost = calculateCost(billingWindows, cal);
            costMap.put(accountID, cost);
        }
        return costMap;
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
