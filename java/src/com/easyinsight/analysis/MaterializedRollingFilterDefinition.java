package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.DateValue;

import java.util.Date;
import java.util.Calendar;

/**
 * User: James Boe
 * Date: Oct 28, 2008
 * Time: 11:10:36 AM
 */
public class MaterializedRollingFilterDefinition extends MaterializedFilterDefinition {

    public static final int DAY = 1;
    public static final int WEEK = 2;
    public static final int MONTH = 3;
    public static final int QUARTER = 4;
    public static final int YEAR = 5;
    public static final int DAY_TO_NOW = 6;
    public static final int WEEK_TO_NOW = 7;
    public static final int MONTH_TO_NOW = 8;
    public static final int QUARTER_TO_NOW = 9;
    public static final int YEAR_TO_NOW = 10;
    public static final int LAST_DAY = 11;
    public static final int LAST_FULL_DAY = 12;
    public static final int LAST_FULL_WEEK = 13;
    public static final int LAST_FULL_MONTH = 14;
    public static final int LAST_FULL_QUARTER = 15;

    private long limitDate;
    private long endDate;
    private Date now;
    private int interval;

    public MaterializedRollingFilterDefinition(AnalysisItem key, int interval, Date now) {
        super(key);
        this.interval = interval;
        if (now == null) {
            now = new Date();
        }
        this.now = now;
        limitDate = findStartDate(interval, now);
        endDate = findEndDate(interval, now);
    }

    public static long findStartDate(int interval, Date now) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        switch (interval) {
            case DAY_TO_NOW:
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                break;
            case WEEK_TO_NOW:
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.DAY_OF_WEEK, 0);
                break;
            case MONTH_TO_NOW:
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.DAY_OF_MONTH, 0);
                break;
            case QUARTER_TO_NOW:
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                int quarterMonth = cal.get(Calendar.MONTH) - cal.get(Calendar.MONTH) % 3;
                cal.set(Calendar.MONTH, quarterMonth);
                break;
            case YEAR_TO_NOW:
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.DAY_OF_YEAR, 0);
                break;
            case DAY:
                cal.setTimeInMillis(cal.getTimeInMillis() - (60 * 60 * 1000 * 24));
                break;
            case WEEK:
                cal.setTimeInMillis(cal.getTimeInMillis() - (60L * 60L * 1000L * 24L * 7L));
                break;
            case MONTH:
                cal.setTimeInMillis(cal.getTimeInMillis() - (60L * 60L * 1000L * 24L * 30L));
                break;
            case YEAR:
                cal.setTimeInMillis(cal.getTimeInMillis() - (60L * 60L * 1000L * 24L * 365L));
                break;
            case QUARTER:
                cal.setTimeInMillis(cal.getTimeInMillis() - (60L * 60L * 1000L * 24L * 30L * 3L));
                break;
            case LAST_FULL_DAY:
                cal.add(Calendar.DAY_OF_YEAR, -1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                break;
            case LAST_FULL_WEEK:
                cal.add(Calendar.WEEK_OF_YEAR, -1);
                cal.set(Calendar.DAY_OF_WEEK, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                break;
            case LAST_FULL_MONTH:
                cal.add(Calendar.MONTH, -1);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                break;
            case LAST_FULL_QUARTER:
                // TODO: ?
                break;
        }
        return cal.getTimeInMillis();
    }

    public static long findEndDate(int interval, Date now) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        switch (interval) {
            case DAY_TO_NOW:
            case WEEK_TO_NOW:
            case MONTH_TO_NOW:
            case QUARTER_TO_NOW:
            case YEAR_TO_NOW:
            case DAY:
            case WEEK:
            case MONTH:
            case YEAR:
            case QUARTER:
                // do nothing, now is fine
                break;
            case LAST_FULL_DAY:
                cal.add(Calendar.DAY_OF_YEAR, -1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                cal.add(Calendar.DAY_OF_YEAR, 1);
                cal.add(Calendar.MILLISECOND, -1);
                break;
            case LAST_FULL_WEEK:
                cal.add(Calendar.WEEK_OF_YEAR, -1);
                cal.set(Calendar.DAY_OF_WEEK, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                cal.add(Calendar.WEEK_OF_YEAR, 1);
                cal.add(Calendar.MILLISECOND, -1);
                break;
            case LAST_FULL_MONTH:
                cal.add(Calendar.MONTH, -1);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                cal.add(Calendar.MONTH, 1);
                cal.add(Calendar.MILLISECOND, -1);
                break;
            case LAST_FULL_QUARTER:
                // TODO: ?
                break;
        }
        return cal.getTimeInMillis();
    }

    public boolean allows(Value value) {
        boolean allowed = false;
        if (interval == LAST_DAY) {
            allowed = true;
        } else if (value.type() == Value.DATE) {
            DateValue dateValue = (DateValue) value;
            allowed = limitDate < dateValue.getDate().getTime() && dateValue.getDate().getTime() <= endDate;
        }
        return allowed;
    }

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(new Date(findStartDate(MaterializedRollingFilterDefinition.LAST_FULL_DAY, date)) + " to " + new Date(findEndDate(MaterializedRollingFilterDefinition.LAST_FULL_DAY, date)));
        System.out.println(new Date(findStartDate(MaterializedRollingFilterDefinition.LAST_FULL_WEEK, date)) + " to " + new Date(findEndDate(MaterializedRollingFilterDefinition.LAST_FULL_WEEK, date)));
        System.out.println(new Date(findStartDate(MaterializedRollingFilterDefinition.LAST_FULL_MONTH, date)) + " to " + new Date(findEndDate(MaterializedRollingFilterDefinition.LAST_FULL_MONTH, date)));
    }
}
