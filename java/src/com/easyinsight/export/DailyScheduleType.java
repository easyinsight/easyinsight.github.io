package com.easyinsight.export;

import net.minidev.json.JSONObject;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Jun 2, 2010
 * Time: 9:36:15 PM
 */
public class DailyScheduleType extends ScheduleType {
    @Override
    public int retrieveType() {
        return ScheduleType.DAILY;
    }

    @Nullable
    public Date runTime(Date lastTime, Date now, String timezoneID) {
        if (timezoneID != null && !"".equals(timezoneID)) {

            ZonedDateTime lastTimeZDT = lastTime.toInstant().atZone(ZoneId.systemDefault());
            ZonedDateTime nowZDT = now.toInstant().atZone(ZoneId.systemDefault());

            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(timezoneID));
            ZonedDateTime time = zonedDateTime.withHour(getHour()).withMinute(getMinute()).withSecond(0).with(ChronoField.MILLI_OF_SECOND, 0);

            if (time.isAfter(lastTimeZDT) && time.isBefore(nowZDT)) {
                Instant instant = time.toInstant();
                return Date.from(instant);
            }
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(cal.getTimeInMillis() - (getTimeOffset() * 60 * 1000));
        cal.set(Calendar.HOUR_OF_DAY, getHour());
        cal.set(Calendar.MINUTE, getMinute());
        cal.setTimeInMillis(cal.getTimeInMillis() + (getTimeOffset() * 60 * 1000));
        if (cal.getTime().getTime() > lastTime.getTime() && cal.getTime().getTime() < now.getTime()) {
            return cal.getTime();
        }
        return null;
    }

    public DailyScheduleType() {}
    public DailyScheduleType(JSONObject object) {
        super(object);
    }

    @Override
    public String when(String accountTimezone) {
        if (isUseAccountTimezone()) {
            return "Daily on " + getHour() + ":" + String.format("%02d", getMinute()) + " (" + accountTimezone + ")";
        } else {
            return "Daily on " + getHour() + ":" + String.format("%02d", getMinute()) + " (UTC" + (getTimeOffset() > 0 ? "-" : "+") + (getTimeOffset() / 60) + ":00)";
        }
    }

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date lastTime = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date nowTime = cal.getTime();

        ZoneId zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofHours(-(360 / 60)));
        System.out.println(zoneId);

        ZonedDateTime lastTimeZDT = lastTime.toInstant().atZone(ZoneId.of("GMT"));
        ZonedDateTime nowZDT = nowTime.toInstant().atZone(ZoneId.of("GMT"));

        System.out.println(ZoneId.systemDefault());

        ZonedDateTime mst1 = ZonedDateTime.now(zoneId).withHour(2).withMinute(0);
        System.out.println(mst1.isAfter(lastTimeZDT) && mst1.isBefore(nowZDT));

        ZonedDateTime mst6 = ZonedDateTime.now(zoneId).withHour(6).withMinute(0);
        System.out.println(mst6.isAfter(lastTimeZDT) && mst6.isBefore(nowZDT));

        ZonedDateTime mst12 = ZonedDateTime.now(zoneId).withHour(14).withMinute(0);
        System.out.println(mst12.isAfter(lastTimeZDT) && mst12.isBefore(nowZDT));

        ZonedDateTime mst23 = ZonedDateTime.now(zoneId).withHour(15).withMinute(0);
        System.out.println(mst23.isAfter(lastTimeZDT) && mst23.isBefore(nowZDT));
    }
}
