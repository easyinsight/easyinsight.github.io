package com.easyinsight.billing;

import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.database.Database;
import com.easyinsight.users.Account;
import com.easyinsight.users.AccountActivityStorage;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.sql.Connection;

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
        Calendar c = Calendar.getInstance();
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        String queryString = "from Account where (accountState = " + Account.ACTIVE + " or accountState = " + Account.CLOSING + ") and accountType != " + Account.FREE + " and accountType != " + Account.ADMINISTRATOR;

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
