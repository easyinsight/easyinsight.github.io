package com.easyinsight.export;

import com.easyinsight.database.EIConnection;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
}