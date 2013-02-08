package com.easyinsight.users;

import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.scheduler.TaskGenerator;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * User: jamesboe
 * Date: 2/7/13
 * Time: 2:07 PM
 */
@Entity
@Table(name="sales_email_scheduler")
@PrimaryKeyJoinColumn(name="task_generator_id")
public class SalesEmailScheduler extends TaskGenerator {
    public SalesEmailScheduler() {
        // hourly
        setTaskInterval(24 * 60 * 60);
    }

    protected ScheduledTask createTask() {
        return new SalesEmailTask();
    }
}
