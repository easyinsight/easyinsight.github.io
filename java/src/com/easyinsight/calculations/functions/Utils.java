package com.easyinsight.calculations.functions;

import java.util.Calendar;

/**
 * User: jamesboe
 * Date: 2/5/15
 * Time: 5:04 PM
 */
public class Utils {
    public static int inverseDayOfWeek(java.time.DayOfWeek dayOfWeek) {
        int dow;
        switch(dayOfWeek) {
            case SUNDAY:
                dow = Calendar.SUNDAY;
                break;
            case MONDAY:
                dow = Calendar.MONDAY;
                break;
            case TUESDAY:
                dow = Calendar.TUESDAY;
                break;
            case WEDNESDAY:
                dow = Calendar.WEDNESDAY;
                break;
            case THURSDAY:
                dow = Calendar.THURSDAY;
                break;
            case FRIDAY:
                dow = Calendar.FRIDAY;
                break;
            case SATURDAY:
                dow = Calendar.SATURDAY;
                break;
            default:
                throw new RuntimeException();
        }
        return dow;
    }
}
