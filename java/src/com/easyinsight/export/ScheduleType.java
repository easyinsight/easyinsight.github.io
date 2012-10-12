package com.easyinsight.export;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import nu.xom.Attribute;
import nu.xom.Element;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Jun 2, 2010
 * Time: 9:36:09 PM
 */
public abstract class ScheduleType {
    private int hour;
    private int minute;
    private long scheduleID;
    private int timeOffset;

    public static final int DAILY = 1;
    public static final int WEEKDAYS = 2;
    public static final int MWF = 3;
    public static final int TR = 4;
    public static final int WEEKLY = 5;
    public static final int MONTHLY = 6;

    public abstract int retrieveType();

    public Element toXML() {
        Element scheduleXML = new Element("scheduleType");
        scheduleXML.addAttribute(new Attribute("hour", String.valueOf(hour)));
        scheduleXML.addAttribute(new Attribute("minute", String.valueOf(minute)));
        scheduleXML.addAttribute(new Attribute("timeOffset", String.valueOf(timeOffset)));
        scheduleXML.addAttribute(new Attribute("scheduleType", String.valueOf(retrieveType())));
        return scheduleXML;
    }

    public static ScheduleType fromXML(Element element) {
        int type = Integer.parseInt(element.getAttribute("scheduleType").getValue());
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
        schedule.setHour(Integer.parseInt(element.getAttribute("hour").getValue()));
        schedule.setMinute(Integer.parseInt(element.getAttribute("minute").getValue()));
        schedule.setTimeOffset(Integer.parseInt(element.getAttribute("timeOffset").getValue()));
        return schedule;
    }

    public int getTimeOffset() {
        return timeOffset;
    }

    public void setTimeOffset(int timeOffset) {
        this.timeOffset = timeOffset;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public long getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(long scheduleID) {
        this.scheduleID = scheduleID;
    }

    /*public void timeToGMT(int utcOffset) {
        int hours = utcOffset / 60;
        this.hour = (hour + hours) % 24;
    }

    public void timeFromGMT(int utcOffset) {
        int hours = utcOffset / 60;
        this.hour = Math.abs((hour - hours) % 24);
    }*/

    public long save(EIConnection conn, long scheduledActivityID) throws SQLException {
        //timeToGMT(utcOffset);
        PreparedStatement insertScheduleStmt = conn.prepareStatement("INSERT INTO SCHEDULE (SCHEDULE_HOUR, SCHEDULE_MINUTE, SCHEDULE_TYPE, " +
                "SCHEDULED_ACCOUNT_ACTIVITY_ID, TIME_OFFSET) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
        insertScheduleStmt.setInt(1, getHour());
        insertScheduleStmt.setInt(2, getMinute());
        insertScheduleStmt.setInt(3, retrieveType());
        insertScheduleStmt.setLong(4, scheduledActivityID);
        insertScheduleStmt.setInt(5, timeOffset);
        insertScheduleStmt.execute();
        long scheduleID = Database.instance().getAutoGenKey(insertScheduleStmt);
        this.scheduleID = scheduleID;
        insertScheduleStmt.close();
        childSave(conn);
        return scheduleID;
    }

    

    protected void childSave(EIConnection conn) throws SQLException {

    }

    public void customLoad(EIConnection conn) throws SQLException {

    }

    @Nullable
    public abstract Date runTime(Date lastTime, Date now);
}
