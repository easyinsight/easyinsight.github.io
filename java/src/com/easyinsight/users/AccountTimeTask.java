package com.easyinsight.users;

import com.easyinsight.scheduler.ScheduledTask;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Date;
import java.sql.Connection;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 9:13:37 PM
 */
@Entity
@Table(name="account_time_task")
@PrimaryKeyJoinColumn(name="scheduled_task_id")
public class AccountTimeTask extends ScheduledTask {
    protected void execute(Date now, Connection conn) throws Exception {
        new AccountActivityStorage().updateAccountTimes(now, conn);
    }
}
