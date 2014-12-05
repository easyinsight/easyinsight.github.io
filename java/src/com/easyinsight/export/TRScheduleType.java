package com.easyinsight.export;

import net.minidev.json.JSONObject;
import org.jetbrains.annotations.Nullable;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Jun 2, 2010
 * Time: 9:36:15 PM
 */
public class TRScheduleType extends ScheduleType {
    @Override
    public int retrieveType() {
        return ScheduleType.TR;
    }

    @Nullable
    public Date runTime(Date lastTime, Date now, String timezoneID) {
        if (timezoneID != null && !"".equals(timezoneID)) {

            ZonedDateTime lastTimeZDT = lastTime.toInstant().atZone(ZoneId.systemDefault());
            ZonedDateTime nowZDT = now.toInstant().atZone(ZoneId.systemDefault());

            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(timezoneID));
            ZonedDateTime time = zonedDateTime.withHour(getHour()).withMinute(getMinute()).withSecond(0).with(ChronoField.MILLI_OF_SECOND, 0);

            if (time.getDayOfWeek().equals(DayOfWeek.TUESDAY) || time.getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
                if (time.isAfter(lastTimeZDT) && time.isBefore(nowZDT)) {
                    Instant instant = time.toInstant();
                    return Date.from(instant);
                }
            }
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(cal.getTimeInMillis() - (getTimeOffset() * 60 * 1000));
        cal.set(Calendar.HOUR_OF_DAY, getHour());
        cal.set(Calendar.MINUTE, getMinute());
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        cal.setTimeInMillis(cal.getTimeInMillis() + (getTimeOffset() * 60 * 1000));
        if (dayOfWeek == Calendar.TUESDAY || dayOfWeek == Calendar.THURSDAY) {
            if (cal.getTime().getTime() > lastTime.getTime() && cal.getTime().getTime() < now.getTime()) {
                return cal.getTime();
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public String when(String accountTimezone) {
        if (isUseAccountTimezone()) {
            return "T/R on " + getHour() + ":" + String.format("%02d", getMinute()) + " (" + accountTimezone + ")";
        } else {
            return "T/R on " + getHour() + ":" + String.format("%02d", getMinute()) + " (UTC" + (getTimeOffset() > 0 ? "-" : "+") + (getTimeOffset() / 60) + ":00)";
        }
    }

    public TRScheduleType() {
    }

    public TRScheduleType(JSONObject jsonObject) {
        super(jsonObject);
    }
}