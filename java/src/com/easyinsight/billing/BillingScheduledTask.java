package com.easyinsight.billing;

import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.database.Database;
import com.easyinsight.users.Account;
import com.easyinsight.users.AccountActivityStorage;

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
    protected void execute(Date now, Connection conn) throws Exception {
        expireTrials(now, conn);
        billCustomers(now, conn);

    }

    private void expireTrials(Date now, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE ACCOUNT SET ACCOUNT_STATE = ? WHERE ACCOUNT_TYPE != ? AND BILLING_INFORMATION_GIVEN != TRUE AND ACCOUNT_ID IN (SELECT ACCOUNT_ID FROM ACCOUNT_TIMED_STATE WHERE date(state_change_time) < ?)");
        stmt.setInt(1, Account.DELINQUENT);
        stmt.setInt(2, Account.PERSONAL);
        stmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
        stmt.execute();
        stmt.close();

        PreparedStatement freeStmt = conn.prepareStatement("UPDATE ACCOUNT SET ACCOUNT_STATE = ? WHERE ACCOUNT_TYPE = ? AND ACCOUNT_ID IN (SELECT ACCOUNT_ID FROM ACCOUNT_TIMED_STATE WHERE date(state_change_time) < ?)");
        freeStmt.setInt(1, Account.ACTIVE);
        freeStmt.setInt(2, Account.PERSONAL);
        freeStmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
        freeStmt.execute();
        freeStmt.close();
    }

    private void billCustomers(Date now, Connection conn) throws SQLException {
        Calendar c = Calendar.getInstance();
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        String queryString = "from Account where (accountState = " + Account.ACTIVE + " or accountState = " + Account.CLOSING + ") and accountType != " + Account.PERSONAL + " and accountType != " + Account.ADMINISTRATOR;

        if(dayOfMonth == c.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            queryString += " and billingDayOfMonth >= ?";
        }
        else {
            queryString += " and billingDayOfMonth = ? ";
        }
        Session s = Database.instance().createSession(conn);
        List results = s.createQuery(queryString).setInteger(0, dayOfMonth).list();
        AccountActivityStorage as = new AccountActivityStorage();
        for(Object o : results) {
            Account a = (Account) o;
            if(a.getAccountState() == Account.CLOSING)
                a.setAccountState(Account.CLOSED);
            else {
                Date d = as.getTrialTime(a.getAccountID(), conn);
                if(d == null || d.before(now))
                    s.save(a.bill());

            }
            s.save(a);
        }
        s.flush();
    }
}
