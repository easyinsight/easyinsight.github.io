package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.DateValue;

import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

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
    public static final int LAST_YEAR = 16;
    public static final int ALL_TIME = 17;
    public static final int CUSTOM = 18;
    public static final int ALL = 19;

    private long limitDate;
    private long endDate;
    private int interval;
    private int intervalType;
    private int intervalAmount;
    private int mode;

    public MaterializedRollingFilterDefinition(RollingFilterDefinition rollingFilterDefinition, Date now, InsightRequestMetadata insightRequestMetadata) {
        super(rollingFilterDefinition.getField());
        if (now == null) {
            now = new Date();
        }
        limitDate = findStartDate(rollingFilterDefinition, now, insightRequestMetadata);
        endDate = findEndDate(rollingFilterDefinition, now, insightRequestMetadata);
        AnalysisDateDimension date = (AnalysisDateDimension) rollingFilterDefinition.getField();
        if (date.isTimeshift()) {
            endDate = endDate + insightRequestMetadata.getUtcOffset() * 1000 * 60;
            limitDate = limitDate + insightRequestMetadata.getUtcOffset() * 1000 * 60;
        }
        if (rollingFilterDefinition.getInterval() > ALL) {
            if (rollingFilterDefinition.getStartDate() != null && rollingFilterDefinition.getEndDate() == null) {
                mode = RollingFilterDefinition.AFTER;
            } else if (rollingFilterDefinition.getStartDate() == null && rollingFilterDefinition.getEndDate() != null) {
                mode = RollingFilterDefinition.BEFORE;
            }
        } else {
            mode = rollingFilterDefinition.getCustomBeforeOrAfter();
        }
    }

    public static long findStartDate(RollingFilterDefinition rollingFilterDefinition, Date now) {
        return findStartDate(rollingFilterDefinition, now, new InsightRequestMetadata());
    }

    public static long findStartDate(RollingFilterDefinition rollingFilterDefinition, Date now, InsightRequestMetadata insightRequestMetadata) {
        int interval = rollingFilterDefinition.getInterval();
        int intervalAmount = -rollingFilterDefinition.getCustomIntervalAmount();
        int intervalType = rollingFilterDefinition.getCustomIntervalType();
        Calendar cal = Calendar.getInstance();
            cal.setTime(now);
        if (!(rollingFilterDefinition.getField() instanceof AnalysisDateDimension)) {
            throw new RuntimeException("Report attempted to run a rolling filter on field " + rollingFilterDefinition.getField().toDisplay() + " - " + rollingFilterDefinition.getField().getAnalysisItemID());
        }
        if (((AnalysisDateDimension) rollingFilterDefinition.getField()).isTimeshift()) {
            int time = insightRequestMetadata.getUtcOffset() / 60;
            String string;
            if (time > 0) {
                string = "GMT-"+time;
            } else if (time < 0) {
                string = "GMT+"+time;
            } else {
                string = "GMT";
            }
            TimeZone timeZone = TimeZone.getTimeZone(string);
            cal.setTimeZone(timeZone);
        }
        switch (interval) {
            case CUSTOM:
                if (rollingFilterDefinition.getCustomBeforeOrAfter() == RollingFilterDefinition.LAST ||
                        rollingFilterDefinition.getCustomBeforeOrAfter() == RollingFilterDefinition.AFTER) {
                    switch (intervalType) {
                        case 0:
                            cal.add(Calendar.MINUTE, intervalAmount);
                            break;
                        case 1:
                            cal.add(Calendar.HOUR_OF_DAY, intervalAmount);
                            break;
                        case 2:
                            cal.add(Calendar.DAY_OF_YEAR, intervalAmount);
                            break;
                        case 3:
                            cal.add(Calendar.WEEK_OF_YEAR, intervalAmount);
                            break;
                        case 4:
                            cal.add(Calendar.MONTH, intervalAmount);
                            break;
                        case 5:
                            cal.add(Calendar.YEAR, intervalAmount);
                            break;
                    }
                }
                break;
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
                cal.set(Calendar.DAY_OF_WEEK, 1);
                break;
            case MONTH_TO_NOW:
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case QUARTER_TO_NOW:
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                int quarterMonth = cal.get(Calendar.MONTH) - cal.get(Calendar.MONTH) % 3;
                cal.set(Calendar.MONTH, quarterMonth);
                cal.set(Calendar.DAY_OF_MONTH, 1);
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
            case LAST_YEAR:
                cal.add(Calendar.YEAR, -1);
                cal.set(Calendar.MONTH, Calendar.JANUARY);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                break;
            case LAST_FULL_QUARTER:
                // TODO: ?
                break;
            default:
                if (rollingFilterDefinition.getStartDate() == null) {
                    return 0;
                }
                return rollingFilterDefinition.getStartDate().getTime();
        }
        //if (!((AnalysisDateDimension) rollingFilterDefinition.getField()).isTimeshift()) {

            int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
            int year = cal.get(Calendar.YEAR);
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.DAY_OF_YEAR, dayOfYear);
            cal.set(Calendar.YEAR, year);
        //}
        return cal.getTimeInMillis();
    }

    public static long findEndDate(RollingFilterDefinition rollingFilterDefinition, Date now) {
        return findEndDate(rollingFilterDefinition, now, new InsightRequestMetadata());
    }

    public static long findEndDate(RollingFilterDefinition rollingFilterDefinition, Date now, InsightRequestMetadata insightRequestMetadata) {
        int interval = rollingFilterDefinition.getInterval();
        int intervalAmount = rollingFilterDefinition.getCustomIntervalAmount();
        int intervalType = rollingFilterDefinition.getCustomIntervalType();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        if (((AnalysisDateDimension) rollingFilterDefinition.getField()).isTimeshift()) {
            int time = insightRequestMetadata.getUtcOffset() / 60;
            String string;
            if (time > 0) {
                string = "GMT-"+time;
            } else if (time < 0) {
                string = "GMT+"+time;
            } else {
                string = "GMT";
            }
            TimeZone timeZone = TimeZone.getTimeZone(string);
            cal.setTimeZone(timeZone);
        }
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
            case ALL_TIME:
                // do nothing, now is fine
                break;
            case CUSTOM:
                if (rollingFilterDefinition.getCustomBeforeOrAfter() == RollingFilterDefinition.NEXT ||
                        rollingFilterDefinition.getCustomBeforeOrAfter() == RollingFilterDefinition.BEFORE) {
                    switch (intervalType) {
                        case 0:
                            cal.add(Calendar.MINUTE, intervalAmount);
                            break;
                        case 1:
                            cal.add(Calendar.HOUR_OF_DAY, intervalAmount);
                            break;
                        case 2:
                            cal.add(Calendar.DAY_OF_YEAR, intervalAmount);
                            break;
                        case 3:
                            cal.add(Calendar.WEEK_OF_YEAR, intervalAmount);
                            break;
                        case 4:
                            cal.add(Calendar.MONTH, intervalAmount);
                            break;
                        case 5:
                            cal.add(Calendar.YEAR, intervalAmount);
                            break;
                    }
                }
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
            case LAST_YEAR:
                cal.add(Calendar.YEAR, -1);
                cal.set(Calendar.MONTH, 0);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                break;
            case LAST_FULL_QUARTER:
                // TODO: ?
                break;
            default:
                if (rollingFilterDefinition.getEndDate() == null) {
                    return 0;
                }
                return rollingFilterDefinition.getEndDate().getTime();
        }
        return cal.getTimeInMillis();
    }

    public boolean allows(Value value) {
        if (interval == MaterializedRollingFilterDefinition.ALL) {
            return true;
        }
        boolean allowed = false;
        if (interval == LAST_DAY) {
            allowed = true;
        } else if (value.type() == Value.DATE) {
            DateValue dateValue = (DateValue) value;
            if (mode == RollingFilterDefinition.AFTER) {
                allowed = limitDate <= dateValue.getDate().getTime();
            } else if (mode == RollingFilterDefinition.BEFORE) {
                allowed = dateValue.getDate().getTime() <= endDate;
            } else {
                allowed = limitDate <= dateValue.getDate().getTime() && dateValue.getDate().getTime() <= endDate;
            }
        } else {
            DateValue originalValue = (DateValue) value.getOriginalValue();
            if (originalValue != null) {
                if (mode == RollingFilterDefinition.AFTER) {
                    allowed = limitDate <= originalValue.getDate().getTime();
                } else if (mode == RollingFilterDefinition.BEFORE) {
                    allowed = originalValue.getDate().getTime() <= endDate;
                } else {
                    allowed = limitDate <= originalValue.getDate().getTime() && originalValue.getDate().getTime() <= endDate;
                }
            }
        }
        return allowed;
    }
}
