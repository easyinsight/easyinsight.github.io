package com.easyinsight.users;

import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ActivitySequence;
import com.easyinsight.export.DataSourceRefreshActivity;
import com.easyinsight.export.DeliveryScheduledTask;
import com.easyinsight.export.ScheduledActivity;
import com.easyinsight.scheduler.DataSourceScheduledTask;
import com.easyinsight.scheduler.ScheduledTask;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 9:13:37 PM
 */
@Entity
@Table(name="activity_sequence_task")
@PrimaryKeyJoinColumn(name="scheduled_task_id")
public class ActivitySequenceTask extends ScheduledTask {

    @Column(name="scheduled_activity_id")
    private long activityID;

    public long getActivityID() {
        return activityID;
    }

    public void setActivityID(long activityID) {
        this.activityID = activityID;
    }

    protected void execute(Date now, EIConnection conn) throws Exception {
        ActivitySequence sequence = (ActivitySequence) ScheduledActivity.createActivity(ScheduledActivity.SEQUENCE, activityID, conn);
        List<ScheduledActivity> activities = sequence.getActivities();
        for (ScheduledActivity activity : activities) {
            if (activity.retrieveType() == ScheduledActivity.DATA_SOURCE_REFRESH) {
                DataSourceRefreshActivity dataSourceRefreshActivity = (DataSourceRefreshActivity) activity;
                DataSourceScheduledTask task = new DataSourceScheduledTask();
                task.setDataSourceID(dataSourceRefreshActivity.getDataSourceID());
                task.internalExecute(now, conn);
            } else if (activity.retrieveType() == ScheduledActivity.GENERAL_DELIVERY || activity.retrieveType() == ScheduledActivity.REPORT_DELIVERY) {
                DeliveryScheduledTask task = new DeliveryScheduledTask();
                task.setActivityID(activity.getScheduledActivityID());
                task.internalExecute(now, conn);
            }
        }
    }
}