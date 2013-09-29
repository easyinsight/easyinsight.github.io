package com.easyinsight.scheduler;

import com.easyinsight.database.EIConnection;
import com.easyinsight.export.*;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: James Boe
 * Date: Apr 15, 2009
 * Time: 8:24:36 PM
 */
@Entity
@Table(name="data_activity_task_generator")
@PrimaryKeyJoinColumn(name="task_generator_id")
public class DataSourceTaskGenerator extends TaskGenerator {

    @Column(name="scheduled_activity_id")
    private long activityID;

    public long getActivityID() {
        return activityID;
    }

    public void setActivityID(long activityID) {
        this.activityID = activityID;
    }

    @Transient
    private long dataSourceID;

    protected ScheduledTask createTask() {
        DataSourceScheduledTask task = new DataSourceScheduledTask();
        task.setDataSourceID(dataSourceID);
        return task;
    }

    @Override
    public List<ScheduledTask> generateTasks(Date now, EIConnection conn) throws SQLException {
        ScheduleType scheduleType = getScheduleType(conn);
        Date lastRunTime = findStartTaskDate();

        PreparedStatement findDataStmt = conn.prepareStatement("SELECT DATA_SOURCE_ID, INTERVAL_TYPE, INTERVAL_UNITS FROM " +
                "SCHEDULED_DATA_SOURCE_REFRESH WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        findDataStmt.setLong(1, activityID);
        List<ScheduledTask> results;
        ResultSet rs = findDataStmt.executeQuery();
        if (rs.next()) {
            dataSourceID = rs.getLong(1);
            int intervalType = rs.getInt(2);
            if (intervalType == DataSourceRefreshActivity.HOURLY) {
                return super.generateTasks(now, conn);
            } else {
                Date time = scheduleType.runTime(lastRunTime, now);
                if (time == null) {
                    return Collections.emptyList();
                }
                DataSourceScheduledTask dataSourceScheduledTask = new DataSourceScheduledTask();
                dataSourceScheduledTask.setDataSourceID(dataSourceID);
                dataSourceScheduledTask.setStatus(ScheduledTask.SCHEDULED);
                dataSourceScheduledTask.setExecutionDate(time);
                dataSourceScheduledTask.setTaskGeneratorID(getTaskGeneratorID());
                setLastTaskDate(time);
                results = Arrays.asList((ScheduledTask) dataSourceScheduledTask);
            }
        } else {
            results = Collections.emptyList();
        }
        findDataStmt.close();
        return results;
    }

    private ScheduleType getScheduleType(EIConnection conn) throws SQLException {
        PreparedStatement loadStmt = conn.prepareStatement("SELECT SCHEDULE.schedule_type, SCHEDULE.SCHEDULE_HOUR, SCHEDULE.SCHEDULE_MINUTE, SCHEDULE.SCHEDULE_ID, SCHEDULE.time_offset " +
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
            schedule.customLoad(conn);
            loadStmt.close();
            return schedule;
        } else {
            throw new RuntimeException();
        }
    }
}
