package com.easyinsight.export;

import com.easyinsight.database.EIConnection;
import nu.xom.Attribute;
import nu.xom.Element;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jun 2, 2010
 * Time: 9:36:15 PM
 */
public class WeeklyScheduleType extends ScheduleType {

    private int dayOfWeek;

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public Element toXML() {
        Element element = super.toXML();
        element.addAttribute(new Attribute("dayOfWeek", String.valueOf(dayOfWeek)));
        return element;
    }

    @Override
    protected void subclassFromXML(Element element) {
        super.subclassFromXML(element);
        dayOfWeek = Integer.parseInt(element.getAttribute("dayOfWeek").getValue());
    }

    @Override
    public int retrieveType() {
        return ScheduleType.WEEKLY;
    }

    @Nullable
    public Date runTime(Date lastTime, Date now) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(cal.getTimeInMillis() - (getTimeOffset() * 60 * 1000));
        cal.set(Calendar.HOUR_OF_DAY, getHour());
        cal.set(Calendar.MINUTE, getMinute());
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        cal.setTimeInMillis(cal.getTimeInMillis() + (getTimeOffset() * 60 * 1000));

        if (dayOfWeek == this.dayOfWeek) {
            if (cal.getTime().getTime() > lastTime.getTime() && cal.getTime().getTime() < now.getTime()) {
                return cal.getTime();
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public void customLoad(EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DAY_OF_WEEK FROM weekly_schedule WHERE SCHEDULE_ID = ?");
        queryStmt.setLong(1, getScheduleID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            this.dayOfWeek = rs.getInt(1);
        }
        queryStmt.close();
    }

    @Override
    protected void childSave(EIConnection conn) throws SQLException {
        PreparedStatement nukeStmt = conn.prepareStatement("DELETE FROM WEEKLY_SCHEDULE WHERE SCHEDULE_ID = ?");
        nukeStmt.setLong(1, getScheduleID());
        nukeStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO WEEKLY_SCHEDULE (DAY_OF_WEEK, SCHEDULE_ID) VALUES (?, ?)");
        insertStmt.setInt(1, dayOfWeek);
        insertStmt.setLong(2, getScheduleID());
        insertStmt.execute();
    }

    @Override
    public String when() {
        String day;
        if (dayOfWeek == 1) {
            day = "Sunday";
        } else if (dayOfWeek == 2) {
            day = "Monday";
        } else if (dayOfWeek == 3) {
            day = "Tuesday";
        } else if (dayOfWeek == 4) {
            day = "Wednesday";
        } else if (dayOfWeek == 5) {
            day = "Thursday";
        } else if (dayOfWeek == 6) {
            day = "Friday";
        } else if (dayOfWeek == 7) {
            day = "Saturday";
        } else {
            day = "";
        }
        return "Every " + day + "  on " + getHour() + ":" + String.format("%02d", getMinute()) + " GMT";
    }

    @Override
    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = super.toJSON(md);
        String day;
        switch(dayOfWeek) {
            case 1:
                day = "sunday";
                break;
            case 2:
                day = "monday";
                break;
            case 3:
                day = "tuesday";
                break;
            case 4:
                day = "wednesday";
                break;
            case 5:
                day = "thursday";
                break;
            case 6:
                day = "friday";
                break;
            case 7:
                day = "saturday";
                break;
            default:
                day = "";
        }

        jo.put("day", day);
        return jo;
    }

    public WeeklyScheduleType() {
    }

    public WeeklyScheduleType(net.minidev.json.JSONObject jsonObject) {
        super(jsonObject);
        List<String> DAYS = Arrays.asList("sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday");
        int day = DAYS.indexOf(String.valueOf(jsonObject.get("day"))) + 1;
        setDayOfWeek(day);
    }
}