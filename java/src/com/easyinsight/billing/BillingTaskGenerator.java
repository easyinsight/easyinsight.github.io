package com.easyinsight.billing;

import com.easyinsight.scheduler.TaskGenerator;
import com.easyinsight.scheduler.ScheduledTask;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.Date;
import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 29, 2009
 * Time: 2:46:59 PM
 */
@Entity
@Table(name="billing_task_generator")
@PrimaryKeyJoinColumn(name="task_generator_id")
public class BillingTaskGenerator extends TaskGenerator {
    public BillingTaskGenerator() {
        super();
        setTaskInterval(24 * 60 * 60 * 1000);
    }

    protected ScheduledTask createTask() {
        return new BillingScheduledTask();
    }
}
