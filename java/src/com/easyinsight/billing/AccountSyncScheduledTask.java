package com.easyinsight.billing;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.users.Account;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 12/6/12
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="account_sync_scheduled_task")
@PrimaryKeyJoinColumn(name="scheduled_task_id")
public class AccountSyncScheduledTask extends ScheduledTask {
    @Override
    protected void execute(Date now, EIConnection conn) throws Exception {
        System.out.println("starting...");
        Session s = Database.instance().createSession(conn);
        List<Account> l = (List<Account>) s.createQuery("from Account").list();

        for(Account a : l) {
            Transaction t = s.beginTransaction();
            a.syncState();
            s.save(a);
            t.commit();
        }

    }
}
