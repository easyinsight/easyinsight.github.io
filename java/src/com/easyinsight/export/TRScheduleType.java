package com.easyinsight.export;

import org.jetbrains.annotations.Nullable;

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
    public Date runTime(Date lastTime, Date now) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, getHour());
        cal.set(Calendar.MINUTE, getMinute());
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.TUESDAY || dayOfWeek == Calendar.THURSDAY) {
            if (cal.getTime().getTime() > lastTime.getTime() && cal.getTime().getTime() < now.getTime()) {
                return cal.getTime();
            } else {
                return null;
            }
        }
        return null;
    }
}