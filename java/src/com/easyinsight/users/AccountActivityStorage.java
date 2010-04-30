package com.easyinsight.users;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import java.sql.*;
import java.util.*;
import java.util.Date;

import org.jetbrains.annotations.Nullable;

/**
 * User: James Boe
 * Date: Apr 10, 2009
 * Time: 8:35:06 PM
 */
public class AccountActivityStorage {

    public void saveAccountTimeChange(long accountID, int accountState, Date when, Connection conn) {
        try {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO account_timed_state (account_id, " +
                    "account_state, state_change_time) values (?, ?, ?)");
            insertStmt.setLong(1, accountID);
            insertStmt.setInt(2, accountState);
            insertStmt.setTimestamp(3, new Timestamp(when.getTime()));
            insertStmt.execute();
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }

    }

    public void updateTrialTime(long accountID, Connection conn, Date newTrialDate) throws SQLException {
        PreparedStatement updateStmt = conn.prepareStatement("UPDATE ACCOUNT_TIMED_STATE SET STATE_CHANGE_TIME = ? WHERE ACCOUNT_ID = ? AND ACCOUNT_STATE = ?");
        updateStmt.setTimestamp(1, new java.sql.Timestamp(newTrialDate.getTime()));
        updateStmt.setLong(2, accountID);
        updateStmt.setInt(3, Account.ACTIVE);
        updateStmt.executeUpdate();
    }

    @Nullable
    public Date getTrialTime(long accountID, Connection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT state_change_time from " +
                    "account_timed_state where date(state_change_time) > ? AND account_id = ? and account_state = ?");
        queryStmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
        queryStmt.setLong(2, accountID);
        queryStmt.setInt(3, Account.ACTIVE);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            return rs.getDate(1);
        } else {
            return null;
        }
    }

    public void updateAccountTimes(Date date, Connection conn) {
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT account_id, account_state, state_change_time from " +
                    "account_timed_state where date(state_change_time) = ? order by state_change_time");
            PreparedStatement updateAccountStmt = conn.prepareStatement("UPDATE account SET account_state = ? WHERE " +
                    "account_id = ?");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_YEAR, -1);
            System.out.println(cal.getTime());
            queryStmt.setDate(1, new java.sql.Date(cal.getTimeInMillis()));
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long accountID = rs.getLong(1);
                int accountType = rs.getInt(2);
                // TODO: execute billing
                updateAccountStmt.setInt(1, accountType);
                updateAccountStmt.setLong(2, accountID);
                updateAccountStmt.executeUpdate();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void saveAccountActivity(AccountActivity accountActivity) {
        Connection conn = Database.instance().getConnection();
        try {
            saveAccountActivity(accountActivity, conn);
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void saveAccountActivity(AccountActivity accountActivity, Connection conn) throws SQLException {
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO ACCOUNT_ACTIVITY (" +
                "ACTIVITY_DATE, USER_LICENSES, ACCOUNT_TYPE, ACCOUNT_ID, ACTIVITY_TYPE, ACTIVITY_NOTES, ACCOUNT_STATE," +
                "MAX_USERS, MAX_SIZE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        insertStmt.setTimestamp(1, new java.sql.Timestamp(accountActivity.getTimestamp().getTime()));
        insertStmt.setInt(2, accountActivity.getUserLicenses());
        insertStmt.setInt(3, accountActivity.getAccountType());
        insertStmt.setLong(4, accountActivity.getAccountID());
        insertStmt.setInt(5, accountActivity.getActivityID());
        insertStmt.setString(6, accountActivity.getActivityNotes());
        insertStmt.setInt(7, accountActivity.getAccountState());
        insertStmt.setInt(8, accountActivity.getMaxUsers());
        insertStmt.setLong(9, accountActivity.getMaxSize());
        insertStmt.execute();
    }

    @Nullable
    public AccountActivity getLastActivity(long accountID) {
        AccountActivity accountActivity = null;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ACTIVITY_DATE, USER_LICENSES, ACCOUNT_TYPE," +
                "ACCOUNT_ID, ACTIVITY_TYPE, ACTIVITY_NOTES, ACCOUNT_STATE, MAX_USERS, MAX_SIZE FROM ACCOUNT_ACTIVITY WHERE ACTIVITY_DATE < ? AND " +
                    "ACCOUNT_ID = ? ORDER BY " +
                "ACTIVITY_DATE DESC LIMIT 1");
            queryStmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
            queryStmt.setLong(2, accountID);
            ResultSet accountRS = queryStmt.executeQuery();
            if (accountRS.next()) {
                accountActivity = new AccountActivity(accountRS.getInt(3), new Date(accountRS.getTimestamp(1).getTime()), accountRS.getLong(4),
                        accountRS.getInt(2), accountRS.getInt(5), accountRS.getString(6), accountRS.getLong(9), accountRS.getInt(8), accountRS.getInt(7));
            }
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
        return accountActivity;
    }

    private Map<Long, List<AccountActivity>> getActivitiesForMonth(Calendar cal, Connection conn) {
        Map<Long, List<AccountActivity>> activityMap = new HashMap<Long, List<AccountActivity>>();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ACTIVITY_DATE, USER_LICENSES, ACCOUNT_TYPE," +
                    "ACCOUNT_ID, ACTIVITY_TYPE, ACTIVITY_NOTES, ACCOUNT_STATE, MAX_USERS, MAX_SIZE FROM ACCOUNT_ACTIVITY WHERE MONTH(ACTIVITY_DATE) = ? AND " +
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
                        accountRS.getInt(2), accountRS.getInt(5), accountRS.getString(6), accountRS.getLong(9), accountRS.getInt(8), accountRS.getInt(7)));
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
            Database.closeConnection(conn);
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
            case Account.BASIC:
                baseAccountMonthlyCost = 0;
                userLicenseCost = 10;
                break;
            case Account.PREMIUM:
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
