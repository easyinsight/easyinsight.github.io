package com.easyinsight.users;

import com.easyinsight.scheduler.TaskGenerator;
import com.easyinsight.scheduler.ScheduledTask;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 9:13:15 PM
 */
@Entity
@Table(name="account_time_scheduler")
@PrimaryKeyJoinColumn(name="task_generator_id")
public class AccountTimeScheduler extends TaskGenerator {

    public AccountTimeScheduler() {
        // daily
        setTaskInterval(24 * 60 * 60 * 1000);
    }

    protected ScheduledTask createTask() {
        return new AccountTimeTask();
    }
}
