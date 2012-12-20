package com.easyinsight.billing;

import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.scheduler.TaskGenerator;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 12/6/12
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="account_sync_task_generator")
@PrimaryKeyJoinColumn(name="task_generator_id")
public class AccountSyncTaskGenerator extends TaskGenerator {
    public AccountSyncTaskGenerator() {
           super();
           setTaskInterval(/*24 * 60 * */ 60 * 1000);
       }

       protected ScheduledTask createTask() {
           return new AccountSyncScheduledTask();
       }
}
