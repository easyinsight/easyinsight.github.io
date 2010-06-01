package com.easyinsight.users;

import com.easyinsight.database.Database;
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
            insertStmt.close();
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
        updateStmt.close();
    }

    @Nullable
    public Date getTrialTime(long accountID, Connection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT state_change_time from " +
                    "account_timed_state where date(state_change_time) > ? AND account_id = ? and account_state = ?");
        queryStmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
        queryStmt.setLong(2, accountID);
        queryStmt.setInt(3, Account.ACTIVE);
        ResultSet rs = queryStmt.executeQuery();
        Date date = null;
        if (rs.next()) {
            date = rs.getDate(1);
        }
        queryStmt.close();
        return date;
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
}
