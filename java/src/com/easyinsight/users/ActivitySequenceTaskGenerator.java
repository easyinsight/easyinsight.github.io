package com.easyinsight.users;

import com.easyinsight.database.EIConnection;
import com.easyinsight.export.*;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.scheduler.TaskGenerator;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/8/15
 * Time: 7:00 AM
 */
@Entity
@Table(name="activity_sequence_task_generator")
@PrimaryKeyJoinColumn(name="task_generator_id")
public class ActivitySequenceTaskGenerator extends TaskGenerator {
    @Column(name="scheduled_activity_id")
    private long activityID;

    public long getActivityID() {
        return activityID;
    }

    public void setActivityID(long activityID) {
        this.activityID = activityID;
    }

    protected ScheduledTask createTask() {
        ActivitySequenceTask task = new ActivitySequenceTask();
        task.setActivityID(activityID);
        return task;
    }

    @Override
    public List<ScheduledTask> generateTasks(Date now, EIConnection conn) throws SQLException {
        try {
            ScheduleType scheduleType = getScheduleType(conn);
            Date lastRunTime = findStartTaskDate();

            String timezoneID = null;
            PreparedStatement tzStmt = conn.prepareStatement("SELECT timezone FROM account, scheduled_account_activity WHERE scheduled_account_activity.scheduled_account_activity_id = ? AND " +
                    "scheduled_account_activity.account_id = account.account_id");
            tzStmt.setLong(1, activityID);
            ResultSet tsRZ = tzStmt.executeQuery();
            if (tsRZ.next()) {
                timezoneID = tsRZ.getString(1);
            }
            tzStmt.close();
            Date time = scheduleType.runTime(lastRunTime, now, timezoneID);
            if (time == null) {
                return Collections.emptyList();
            }
            ActivitySequenceTask task = new ActivitySequenceTask();
            task.setActivityID(activityID);
            task.setStatus(ScheduledTask.SCHEDULED);
            task.setExecutionDate(time);
            task.setTaskGeneratorID(getTaskGeneratorID());
            setLastTaskDate(time);
            return Arrays.asList((ScheduledTask) task);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private ScheduleType getScheduleType(EIConnection conn) throws SQLException {
        PreparedStatement loadStmt = conn.prepareStatement("SELECT SCHEDULE.schedule_type, SCHEDULE.SCHEDULE_HOUR, SCHEDULE.SCHEDULE_MINUTE, SCHEDULE.SCHEDULE_ID, " +
                "SCHEDULE.time_offset, SCHEDULE.use_account_timezone " +
                "FROM SCHEDULE WHERE " +
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
                default:
                    throw new RuntimeException();
            }
            schedule.setScheduleID(id);
            schedule.setHour(hour);
            schedule.setMinute(minute);
            schedule.setTimeOffset(offset);
            schedule.setUseAccountTimezone(useAccountTimezone);
            schedule.customLoad(conn);
            loadStmt.close();
            return schedule;
        } else {
            throw new RuntimeException();
        }
    }
}
