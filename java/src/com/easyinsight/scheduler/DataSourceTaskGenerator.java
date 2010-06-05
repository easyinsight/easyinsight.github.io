package com.easyinsight.scheduler;

import com.easyinsight.database.EIConnection;
import com.easyinsight.export.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;
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

    @Override
    public List<ScheduledTask> generateTasks(Date now, EIConnection conn) throws SQLException {
        ScheduleType scheduleType = getScheduleType(conn);
        Date lastRunTime = findStartTaskDate();
        Date time = scheduleType.runTime(lastRunTime, now);
        if (time != null) {
            PreparedStatement findDataStmt = conn.prepareStatement("SELECT DATA_SOURCE_ID FROM SCHEDULED_DATA_SOURCE_REFRESH WHERE SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
            findDataStmt.setLong(1, activityID);
            ResultSet rs = findDataStmt.executeQuery();
            rs.next();
            DataSourceScheduledTask dataSourceScheduledTask = new DataSourceScheduledTask();
            dataSourceScheduledTask.setDataSourceID(rs.getLong(1));
            dataSourceScheduledTask.setStatus(ScheduledTask.SCHEDULED);
            dataSourceScheduledTask.setExecutionDate(time);
            dataSourceScheduledTask.setTaskGeneratorID(getTaskGeneratorID());
            setLastTaskDate(time);
            System.out.println("scheduled run for time " + time);
            return Arrays.asList((ScheduledTask) dataSourceScheduledTask);
        } else {
            return Collections.emptyList();
        }
    }

    private ScheduleType getScheduleType(EIConnection conn) throws SQLException {
        PreparedStatement loadStmt = conn.prepareStatement("SELECT SCHEDULE.schedule_type, SCHEDULE.SCHEDULE_HOUR, SCHEDULE.SCHEDULE_MINUTE, SCHEDULE.SCHEDULE_ID FROM SCHEDULE WHERE " +
                "SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        loadStmt.setLong(1, activityID);
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            int type = rs.getInt(1);
            int hour = rs.getInt(2);
            int minute = rs.getInt(3);
            long id = rs.getLong(4);
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
            schedule.customLoad(conn);
            loadStmt.close();
            return schedule;
        } else {
            throw new RuntimeException();
        }
    }
}
