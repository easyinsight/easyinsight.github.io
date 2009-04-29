package com.easyinsight.users;

import com.easyinsight.scheduler.TaskGenerator;
import com.easyinsight.scheduler.ScheduledTask;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 9:13:15 PM
 */
public class AccountBillingScheduler extends TaskGenerator {
    protected ScheduledTask createTask() {
        return new AccountBillingTask();
    }
}