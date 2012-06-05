package com.easyinsight.billing;

import com.easyinsight.database.EIConnection;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.database.Database;
import com.easyinsight.users.Account;
import com.easyinsight.users.AccountActivityStorage;
import com.easyinsight.logging.LogClass;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import org.hibernate.Session;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 29, 2009
 * Time: 3:07:56 PM
 */
@Entity
@Table(name="billing_scheduled_task")
@PrimaryKeyJoinColumn(name="scheduled_task_id")
public class BillingScheduledTask extends ScheduledTask {
    protected void execute(Date now, EIConnection conn) throws Exception {

        //new AccountActivityStorage().updateAccountTimes(now, conn);
        expireTrials(now, conn);
        billCustomers(now, conn);
        new AccountActivityStorage().executeSalesEmails(conn);
    }

    private void expireTrials(Date now, Connection conn) throws SQLException {

        // Where the account is not of the Free tier, the account is not Closing or Closed, and billing information is not given, and where
        // the trial end time is at or before today, set the account to Delinquent.

        PreparedStatement stmt = conn.prepareStatement("UPDATE ACCOUNT SET ACCOUNT_STATE = ? WHERE ACCOUNT_TYPE != ? AND ACCOUNT_STATE != ? AND ACCOUNT_STATE != ? AND (BILLING_INFORMATION_GIVEN IS NULL OR BILLING_INFORMATION_GIVEN = FALSE) AND ACCOUNT_ID IN (SELECT ACCOUNT_ID FROM ACCOUNT_TIMED_STATE WHERE date(state_change_time) < ?)");
        stmt.setInt(1, Account.DELINQUENT);
        stmt.setInt(2, Account.PERSONAL);
        stmt.setInt(3, Account.CLOSING);
        stmt.setInt(4, Account.CLOSED);
        stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
        stmt.execute();
        stmt.close();
    }

    private void billCustomers(Date now, Connection conn) throws SQLException {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int monthOfYear = c.get(Calendar.MONTH);
        LogClass.info("Finding all accounts with day of month on " + dayOfMonth);
        String queryString = "from Account where (accountState = " + Account.ACTIVE + " or accountState = " + Account.CLOSING + ") and accountType != " + Account.PERSONAL + " and accountType != " + Account.ADMINISTRATOR + " AND manualInvoicing = ? AND (billing_month_of_year is null OR billing_month_of_year = ? or billingFailures > 0) ";

        if(dayOfMonth == c.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            queryString += " and (billingDayOfMonth >= ? OR billingFailures > 0)";
        }
        else {
            queryString += " and (billingDayOfMonth = ? OR billingFailures > 0)";
        }
        Session s = Database.instance().createSession(conn);
        List results = s.createQuery(queryString).setBoolean(0, false).setInteger(1, monthOfYear).setInteger(2, dayOfMonth).list();        
        LogClass.info("Found " + results.size() + " billing results.");
        AccountActivityStorage as = new AccountActivityStorage();
        for(Object o : results) {
            Account a = (Account) o;
            if(a.getAccountState() == Account.CLOSING)
                a.setAccountState(Account.CLOSED);
            else {
                Date d = as.getTrialTime(a.getAccountID(), conn);
                if(d == null || d.before(now)) {
                    LogClass.info("Billing for account: " + a.getAccountID());
                    s.save(a.bill());
                }

            }
            s.save(a);
        }
        s.flush();
    }
}
