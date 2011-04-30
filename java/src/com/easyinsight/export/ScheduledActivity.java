package com.easyinsight.export;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: jamesboe
 * Date: Jun 2, 2010
 * Time: 9:34:50 PM
 */
public abstract class ScheduledActivity {
    private ScheduleType scheduleType;
    private long scheduledActivityID;

    public static final int DATA_SOURCE_REFRESH = 1;
    public static final int REPORT_DELIVERY = 2;
    public static final int SCORECARD_DELIVERY = 3;

    private String problemMessage;
    private int problemLevel;

    public String getProblemMessage() {
        return problemMessage;
    }

    public void setProblemMessage(String problemMessage) {
        this.problemMessage = problemMessage;
    }

    public int getProblemLevel() {
        return problemLevel;
    }

    public void setProblemLevel(int problemLevel) {
        this.problemLevel = problemLevel;
    }

    public abstract int retrieveType();

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public long getScheduledActivityID() {
        return scheduledActivityID;
    }

    public void setScheduledActivityID(long scheduledActivityID) {
        this.scheduledActivityID = scheduledActivityID;
    }

    public abstract void setup(EIConnection conn) throws SQLException;

    public void save(EIConnection conn, int utcOffset) throws SQLException {
        if (scheduledActivityID == 0) {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO SCHEDULED_ACCOUNT_ACTIVITY (ACCOUNT_ID, ACTIVITY_TYPE) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            insertStmt.setLong(1, SecurityUtil.getAccountID());
            insertStmt.setInt(2, retrieveType());
            insertStmt.execute();
            this.scheduledActivityID = Database.instance().getAutoGenKey(insertStmt);
            insertStmt.close();
            this.customSave(conn, utcOffset);
            scheduleType.setTimeOffset(utcOffset);
            scheduleType.save(conn, this.scheduledActivityID);
        } else {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM SCHEDULE WHERE scheduled_account_activity_id = ?");
            deleteStmt.setLong(1, scheduledActivityID);
            deleteStmt.executeUpdate();
            deleteStmt.close();
            this.customSave(conn, utcOffset);
            scheduleType.setTimeOffset(utcOffset);
            scheduleType.save(conn, this.scheduledActivityID);
        }
    }

    protected void customSave(EIConnection conn, int utcOffset) throws SQLException {

    }

    protected void customLoad(EIConnection conn) throws SQLException {

    }


    public static ScheduledActivity createActivity(int activityType, long activityID, EIConnection conn) throws SQLException {
        ScheduledActivity scheduledActivity;
        switch (activityType) {
            case ScheduledActivity.DATA_SOURCE_REFRESH:
                scheduledActivity = new DataSourceRefreshActivity();
                break;
            case ScheduledActivity.REPORT_DELIVERY:
                scheduledActivity = new ReportDelivery();
                break;
            case ScheduledActivity.SCORECARD_DELIVERY:
                scheduledActivity = new ScorecardDelivery();
                break;
            default:
                throw new RuntimeException();
        }        
        scheduledActivity.setScheduledActivityID(activityID);
        scheduledActivity.customLoad(conn);
        ScheduleType scheduleType = loadSchedule(activityID, conn);
        scheduledActivity.setScheduleType(scheduleType);
        return scheduledActivity;
    }

    private static ScheduleType loadSchedule(long activityID, EIConnection conn) throws SQLException {
        PreparedStatement loadStmt = conn.prepareStatement("SELECT SCHEDULE.schedule_type, SCHEDULE.SCHEDULE_HOUR, SCHEDULE.SCHEDULE_MINUTE, SCHEDULE.SCHEDULE_ID," +
                "SCHEDULE.time_offset FROM SCHEDULE WHERE " +
                "SCHEDULED_ACCOUNT_ACTIVITY_ID = ?");
        loadStmt.setLong(1, activityID);
        ResultSet rs = loadStmt.executeQuery();
        if (rs.next()) {
            int type = rs.getInt(1);
            int hour = rs.getInt(2);
            int minute = rs.getInt(3);
            long id = rs.getLong(4);
            int utcOffset = rs.getInt(5);
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
            schedule.setTimeOffset(utcOffset);
            schedule.customLoad(conn);
            loadStmt.close();
            return schedule;
        } else {
            throw new RuntimeException();
        }
    }

    public void schedule() {

    }
}
