package com.easyinsight.users;

import com.easyinsight.database.EIConnection;
import com.easyinsight.salesautomation.PersonalizedSalesEmail;
import com.easyinsight.scheduler.ScheduledTask;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 2/7/13
 * Time: 2:15 PM
 */
@Entity
@Table(name="sales_email_task")
@PrimaryKeyJoinColumn(name="scheduled_task_id")
public class SalesEmailTask extends ScheduledTask {
    @Override
    protected void execute(Date now, EIConnection conn) throws Exception {
        new PersonalizedSalesEmail().sync();
    }
}
