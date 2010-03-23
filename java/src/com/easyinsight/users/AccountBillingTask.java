package com.easyinsight.users;

import com.easyinsight.database.EIConnection;
import com.easyinsight.scheduler.ScheduledTask;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 9:13:37 PM
 */
@Entity
@Table(name="account_billing_task")
@PrimaryKeyJoinColumn(name="scheduled_task_id")
public class AccountBillingTask extends ScheduledTask {
    protected void execute(Date now, EIConnection conn) throws Exception {
        // TODO: implement
    }
}