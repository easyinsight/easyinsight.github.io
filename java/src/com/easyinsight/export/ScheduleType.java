package com.easyinsight.export;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import nu.xom.Attribute;
import nu.xom.Element;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

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
    public static final int NEVER = 7;

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
        schedule.subclassFromXML(element);
        return schedule;
    }

    protected void subclassFromXML(Element element) {

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

    public abstract String when();

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("schedule_id", getScheduleID());
        jo.put("hour", getHour());
        jo.put("minute", getMinute());
        jo.put("offset", getTimeOffset());
        jo.put("description", when());
        jo.put("type", toString(retrieveType()));
        return jo;
    }

    public static String toString(int type) {
        switch(type) {
            case ScheduleType.DAILY:
                return "daily";
            case ScheduleType.MONTHLY:
                return "monthly";
            case ScheduleType.MWF:
                return "mwf";
            case ScheduleType.NEVER:
                return "never";
            case ScheduleType.TR:
                return "tr";
            case ScheduleType.WEEKDAYS:
                return "weekdays";
            case ScheduleType.WEEKLY:
                return "weekly";
            default:
                throw new RuntimeException("No type of schedule defined.");
        }
    }

    public ScheduleType() {

    }

    public ScheduleType(net.minidev.json.JSONObject jsonObject) {
        setScheduleID(Long.parseLong(String.valueOf(jsonObject.get("schedule_id"))));
        setHour(Integer.parseInt(String.valueOf(jsonObject.get("hour"))));
        setMinute(Integer.parseInt(String.valueOf(jsonObject.get("minute"))));
        setTimeOffset(Integer.parseInt(String.valueOf(jsonObject.get("offset"))));
    }

    public static ScheduleType fromJSON(net.minidev.json.JSONObject schedule_type) {

        String s = String.valueOf(schedule_type.get("type"));
        switch(s) {
            case "daily":
                return new DailyScheduleType(schedule_type);
            case "monthly":
                return new MonthlyScheduleType(schedule_type);
            case "mwf":
                return new MWFScheduleType(schedule_type);
            case "tr":
                return new TRScheduleType(schedule_type);
            case "weekdays":
                return new WeekdayScheduleType(schedule_type);
            case "weekly":
                return new WeeklyScheduleType(schedule_type);
            case "never":
                return new NeverScheduleType(schedule_type);
            default:
                throw new RuntimeException("Unsupported type.");
        }

    }
}
