package com.easyinsight.users;

import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.*;
import java.util.Date;

import com.easyinsight.salesautomation.SalesEmail;
import org.jetbrains.annotations.Nullable;

import javax.mail.MessagingException;

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

    public void generateSalesEmailSchedules(long userID, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO user_sales_email_schedule (user_id, target_number, send_date) VALUES (?, ?, ?)");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        stmt.setLong(1, userID);
        stmt.setInt(2, SalesEmail.ONE_DAY);
        stmt.setTimestamp(3, new Timestamp(cal.getTime().getTime()));
        stmt.execute();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        for (int i = 1; i <= 4; i++) {
            stmt.setLong(1, userID);
            stmt.setInt(2, i + 1);
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            stmt.setTimestamp(3, new Timestamp(cal.getTime().getTime()));
            stmt.execute();
        }
        stmt.close();
    }

    public void generateWelcomeBackEmailSchedules(long userID, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO user_sales_email_schedule (user_id, target_number, send_date) VALUES (?, ?, ?)");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        stmt.setLong(1, userID);
        stmt.setInt(2, SalesEmail.ONE_DAY_BACK);
        stmt.setTimestamp(3, new Timestamp(cal.getTime().getTime()));
        stmt.execute();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        for (int i = 1; i <= 2; i++) {
            stmt.setLong(1, userID);
            stmt.setInt(2, i + SalesEmail.ONE_WEEK_BACK);
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            stmt.setTimestamp(3, new Timestamp(cal.getTime().getTime()));
            stmt.execute();
        }
        stmt.close();
    }

    public void executeSalesEmails(EIConnection conn) throws SQLException, UnsupportedEncodingException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT USER_ID, TARGET_NUMBER, user_sales_email_schedule_id FROM user_sales_email_schedule WHERE SEND_DATE < ?");
        queryStmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
        ResultSet rs = queryStmt.executeQuery();
        PreparedStatement updateStmt = conn.prepareStatement("DELETE FROM user_sales_email_schedule WHERE user_sales_email_schedule_id = ?");
        while (rs.next()) {
            long userID = rs.getLong(1);
            int targetNumber = rs.getInt(2);
            long scheduleID = rs.getLong(3);
            try {
                SalesEmail.leadNurture(userID, targetNumber, conn);
            } catch (Exception e) {
                LogClass.error(e);
            }
            updateStmt.setLong(1, scheduleID);
            updateStmt.executeUpdate();
        }
        updateStmt.close();
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
}
