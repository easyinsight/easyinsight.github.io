package com.easyinsight.export;

import org.jetbrains.annotations.Nullable;

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
    public Date runTime(Date lastTime, Date now) {
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
}
