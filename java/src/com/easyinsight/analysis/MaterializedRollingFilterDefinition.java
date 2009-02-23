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

    private long limitDate;
    private Date now;

    public MaterializedRollingFilterDefinition(AnalysisItem key, int interval, Date now) {
        super(key);
        if (now == null) {
            now = new Date();
        }
        this.now = now;
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
                cal.setTimeInMillis(cal.getTimeInMillis() - (60L * 60L * 1000L * 24L * 7L * 30L));
                break;
            case YEAR:
                cal.setTimeInMillis(cal.getTimeInMillis() - (60L * 60L * 1000L * 24L * 7L * 365L));
                break;
        }
        limitDate = cal.getTimeInMillis();
    }

    public boolean allows(Value value, Value preTransformValue) {
        boolean allowed = false;
        if (preTransformValue.type() == Value.DATE) {
            DateValue dateValue = (DateValue) preTransformValue;
            allowed = limitDate < dateValue.getDate().getTime() && dateValue.getDate().getTime() <= now.getTime();
        }
        return allowed;
    }
}
