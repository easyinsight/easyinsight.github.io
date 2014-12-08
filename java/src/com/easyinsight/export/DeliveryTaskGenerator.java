package com.easyinsight.export;

import com.easyinsight.database.EIConnection;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.scheduler.TaskGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: Jun 6, 2010
 * Time: 1:46:09 PM
 */
@Entity
@Table(name="delivery_task_generator")
@PrimaryKeyJoinColumn(name="task_generator_id")
public class DeliveryTaskGenerator extends TaskGenerator {
    @Column(name="scheduled_account_activity_id")
    private long activityID;

    public long getActivityID() {
        return activityID;
    }

    public void setActivityID(long activityID) {
        this.activityID = activityID;
    }

    @Override
    public List<ScheduledTask> generateTasks(Date now, EIConnection conn) throws SQLException {
        ScheduleType scheduleType = getScheduleType(conn);
        Date lastRunTime = findStartTaskDate();
        String timezoneID = null;
        PreparedStatement tzStmt = conn.prepareStatement("SELECT timezone FROM account, scheduled_account_activity WHERE scheduled_account_activity.scheduled_account_activity_id = ? AND " +
                "scheduled_account_activity.account_id = account.account_id");
        tzStmt.setLong(1, activityID);
        ResultSet rs = tzStmt.executeQuery();
        if (rs.next()) {
            timezoneID = rs.getString(1);
        }
        tzStmt.close();
        Date time = scheduleType.runTime(lastRunTime, now, timezoneID);
        if (time != null) {
            DeliveryScheduledTask deliveryTask = new DeliveryScheduledTask();
            deliveryTask.setTaskType(ScheduledTask.EMAIL);
            deliveryTask.setActivityID(activityID);
            deliveryTask.setStatus(ScheduledTask.SCHEDULED);
            deliveryTask.setExecutionDate(time);
            deliveryTask.setTaskGeneratorID(getTaskGeneratorID());
            setLastTaskDate(time);
            return Arrays.asList((ScheduledTask) deliveryTask);
        } else {
            return Collections.emptyList();
        }
    }

    private ScheduleType getScheduleType(EIConnection conn) throws SQLException {
        PreparedStatement loadStmt = conn.prepareStatement("SELECT SCHEDULE.schedule_type, SCHEDULE.SCHEDULE_HOUR, SCHEDULE.SCHEDULE_MINUTE, SCHEDULE.SCHEDULE_ID, " +
                "SCHEDULE.time_offset, SCHEDULE.use_account_timezone FROM SCHEDULE WHERE " +
                "SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        loadStmt.setLong(1, activityID);
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            int type = rs.getInt(1);
            int hour = rs.getInt(2);
            int minute = rs.getInt(3);
            long id = rs.getLong(4);
            int offset = rs.getInt(5);
            boolean useAccountTimezone = rs.getBoolean(6);
            ScheduleType schedule;
            switch (type) {
                case ScheduleType.DAILY:
                    schedule = new DailyScheduleType();
                    break;
                case ScheduleType.WEEKDAYS:
                    schedule = new WeekdayScheduleType();
                    break;
                case ScheduleType.MWF:
                    schedule = new MWFScheduleType();
                    break;
                case ScheduleType.TR:
                    schedule = new TRScheduleType();
                    break;
                case ScheduleType.WEEKLY:
                    schedule = new WeeklyScheduleType();
                    break;
                case ScheduleType.MONTHLY:
                    schedule = new MonthlyScheduleType();
                    break;
                case ScheduleType.NEVER:
                    schedule = new NeverScheduleType();
                    break;
                default:
                    throw new RuntimeException();
            }
            schedule.setScheduleID(id);
            schedule.setHour(hour);
            schedule.setMinute(minute);
            schedule.setTimeOffset(offset);
            schedule.customLoad(conn);
            schedule.setUseAccountTimezone(useAccountTimezone);
            loadStmt.close();
            return schedule;
        } else {
            throw new RuntimeException();
        }
    }
}
