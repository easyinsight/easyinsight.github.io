package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.DateValue;
import com.easyinsight.security.SecurityUtil;

import java.time.*;
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
    public static final int LAST_YEAR = 16;
    public static final int ALL_TIME = 17;
    public static final int CUSTOM = 18;
    public static final int ALL = 19;

    public static final int LAST_FULL_QUARTER = -1;
    public static final int LAST_FULL_YEAR = -2;
    public static final int LAST_WEEK_TO_NOW = -5;
    public static final int LAST_MONTH_TO_NOW = -3;
    public static final int LAST_QUARTER_TO_NOW = -7;
    public static final int LAST_YEAR_TO_NOW = -10;
    public static final int PREVIOUS_FULL_WEEK = -6;
    public static final int PREVIOUS_FULL_MONTH = -4;
    public static final int PREVIOUS_FULL_QUARTER = -8;
    public static final int PREVIOUS_FULL_YEAR = -9;
    public static final int NEXT_FULL_WEEK = -11;
    public static final int NEXT_FULL_MONTH = -12;
    public static final int NEXT_FULL_QUARTER = -13;
    public static final int NEXT_FULL_YEAR = -14;
    public static final int THIS_WEEK = -15;
    public static final int THIS_MONTH = -16;
    public static final int THIS_QUARTER = -17;
    public static final int THIS_YEAR = -18;
    public static final int THIS_FISCAL_YEAR = -19;
    public static final int PRIOR_FISCAL_YEAR = -20;

    private LocalDate limitDate;
    private LocalDate endDate;
    private ZonedDateTime limitDateTime;
    private ZonedDateTime endDateTime;
    private boolean dateTime;
    private int interval;
    private int mode;

    public MaterializedRollingFilterDefinition(RollingFilterDefinition rollingFilterDefinition, Date now, InsightRequestMetadata insightRequestMetadata) {
        super(rollingFilterDefinition.getField());
        if (now == null) {
            now = new Date();
        }
        interval = rollingFilterDefinition.getInterval();
        if (rollingFilterDefinition.dateTime(insightRequestMetadata)) {
            dateTime = true;
            limitDateTime = findStartDateTime(rollingFilterDefinition, now, insightRequestMetadata);
            endDateTime = findEndDateTime(rollingFilterDefinition, now, insightRequestMetadata);
        } else {
            dateTime = false;
            limitDate = findStartDate(rollingFilterDefinition, now, insightRequestMetadata);
            endDate = findEndDate(rollingFilterDefinition, now, insightRequestMetadata);
        }
        if (rollingFilterDefinition.getStartDate() != null && rollingFilterDefinition.getEndDate() == null) {
            mode = RollingFilterDefinition.AFTER;
        } else if (rollingFilterDefinition.getStartDate() == null && rollingFilterDefinition.getEndDate() != null) {
            mode = RollingFilterDefinition.BEFORE;
        } else {
            mode = 0;
        }
    }

    public static LocalDate findStartDate(RollingFilterDefinition rollingFilterDefinition, Date now) {
        return findStartDate(rollingFilterDefinition, now, new InsightRequestMetadata());
    }

    public static LocalDate findStartDate(RollingFilterDefinition rollingFilterDefinition, Date now, InsightRequestMetadata insightRequestMetadata) {
        if (rollingFilterDefinition.getStartDate() != null) {
            return (LocalDate) rollingFilterDefinition.getStartDate();
        }
        int interval = rollingFilterDefinition.getInterval();
        if (interval < 0) {
            for (CustomRollingInterval i : RollingFilterDefinition.createAdditionalIntervals()) {
                if (i.getIntervalNumber() == interval) {
                    rollingFilterDefinition.applyCalculationsBeforeRun(null, null, null, null, null, null, null, insightRequestMetadata);
                    if (rollingFilterDefinition.getStartDate() != null) {
                        return (LocalDate) rollingFilterDefinition.getStartDate();
                    }
                }
            }
        } else if (interval > ALL) {
            rollingFilterDefinition.applyCalculationsBeforeRun(null, null, null, null, null, null, null, insightRequestMetadata);
            if (rollingFilterDefinition.getStartDate() != null) {
                return (LocalDate) rollingFilterDefinition.getStartDate();
            }
        }
        int intervalAmount = -rollingFilterDefinition.getCustomIntervalAmount();
        int intervalType = rollingFilterDefinition.getCustomIntervalType();
        /*Calendar cal = Calendar.getInstance();
        cal.setTime(now);*/
        if (!(rollingFilterDefinition.getField() instanceof AnalysisDateDimension)) {
            throw new RuntimeException("Report attempted to run a rolling filter on field " + rollingFilterDefinition.getField().toDisplay() + " - " + rollingFilterDefinition.getField().getAnalysisItemID());
        }
        int firstDayOfWeek = SecurityUtil.getFirstDayOfWeek();
        DayOfWeek targetDayOfWeek;
        if (firstDayOfWeek == Calendar.SUNDAY) {
            targetDayOfWeek = DayOfWeek.SUNDAY;
        } else if (firstDayOfWeek == Calendar.MONDAY) {
            targetDayOfWeek = DayOfWeek.MONDAY;
        } else if (firstDayOfWeek == Calendar.TUESDAY) {
            targetDayOfWeek = DayOfWeek.TUESDAY;
        } else if (firstDayOfWeek == Calendar.WEDNESDAY) {
            targetDayOfWeek = DayOfWeek.WEDNESDAY;
        } else if (firstDayOfWeek == Calendar.THURSDAY) {
            targetDayOfWeek = DayOfWeek.THURSDAY;
        } else if (firstDayOfWeek == Calendar.FRIDAY) {
            targetDayOfWeek = DayOfWeek.FRIDAY;
        } else {
            targetDayOfWeek = DayOfWeek.SATURDAY;
        }


            ZoneId zoneId = insightRequestMetadata.createZoneID();

            LocalDate zdt = LocalDate.now(zoneId);
            //ZonedDateTime zdt = ZonedDateTime.now(zoneId);
            switch (interval) {
                case CUSTOM:
                    if (rollingFilterDefinition.getCustomBeforeOrAfter() == RollingFilterDefinition.LAST ||
                            rollingFilterDefinition.getCustomBeforeOrAfter() == RollingFilterDefinition.AFTER) {
                        switch (intervalType) {
                            /*case 0:
                                zdt = zdt.plusMinutes(intervalAmount);
                                break;
                            case 1:
                                zdt = zdt.plusHours(intervalAmount);
                                break;*/
                            case 2:
                                zdt = zdt.plusDays(intervalAmount);
                                break;
                            case 3:
                                zdt = zdt.plusWeeks(intervalAmount);
                                break;
                            case 4:
                                zdt = zdt.plusMonths(intervalAmount);
                                break;
                            case 5:
                                zdt = zdt.plusYears(intervalAmount);
                                break;
                        }
                    }
                    break;
                case DAY_TO_NOW:
                    //zdt = zdt.withHour(0).withMinute(0).withSecond(0).withNano(0);
                    break;
                case WEEK_TO_NOW:
                    zdt = zdt.minusWeeks(1).with(targetDayOfWeek);
                    //zdt = zdt.withHour(0).withMinute(0).withSecond(0).withNano(0);
                    break;
                case MONTH_TO_NOW:
                    zdt = zdt.withDayOfMonth(1);
                    break;
                case QUARTER_TO_NOW:
                    int month = zdt.getMonthValue() - 1;
                    int quarterMonth = month - (month % 3) + 1;
                    zdt = zdt.withMonth(quarterMonth);
                    zdt = zdt.withDayOfMonth(1);
                    break;
                case YEAR_TO_NOW:
                    zdt = zdt.withDayOfYear(1).withDayOfMonth(1);
                    break;
                case DAY:
                    zdt = zdt.minusDays(1);
                    break;
                case WEEK:
                    zdt = zdt.minusDays(6);
                    break;
                case MONTH:
                    zdt = zdt.minusDays(30);
                    break;
                case YEAR:
                    zdt = zdt.minusYears(1);
                    break;
                case QUARTER:
                    zdt = zdt.minusMonths(3);
                    break;
                case LAST_FULL_DAY:
                    zdt = zdt.minusDays(1);
                    break;
                case LAST_FULL_WEEK:
                    zdt = zdt.minusWeeks(2).with(targetDayOfWeek);
                    //zdt = zdt.withHour(0).withMinute(0).withSecond(0).withNano(0);
                    break;
                case LAST_FULL_MONTH:
                    zdt = zdt.minusMonths(1).withDayOfMonth(1);
                    break;
                case LAST_YEAR:
                    zdt = zdt.minusYears(1).withDayOfYear(1);
                    break;
                case LAST_FULL_QUARTER:
                    // TODO: ?
                    break;
                default:
                    /*if (rollingFilterDefinition.getStartDate() == null) {
                        return 0;
                    }*/
                    return (LocalDate) rollingFilterDefinition.getStartDate();
            }
            return zdt;
            /*Instant instant = zdt.toInstant();
            return Date.from(instant).getTime();*/
        //} else {


    }

    public static ZonedDateTime findStartDateTime(RollingFilterDefinition rollingFilterDefinition, Date now, InsightRequestMetadata insightRequestMetadata) {
        if (rollingFilterDefinition.getStartDate() != null) {
            return (ZonedDateTime) rollingFilterDefinition.getStartDate();
        }
        int interval = rollingFilterDefinition.getInterval();
        if (interval < 0) {
            for (CustomRollingInterval i : RollingFilterDefinition.createAdditionalIntervals()) {
                if (i.getIntervalNumber() == interval) {
                    rollingFilterDefinition.applyCalculationsBeforeRun(null, null, null, null, null, null, null, insightRequestMetadata);
                    if (rollingFilterDefinition.getStartDate() != null) {
                        return (ZonedDateTime) rollingFilterDefinition.getStartDate();
                    }
                }
            }
        } else if (interval > ALL) {
            rollingFilterDefinition.applyCalculationsBeforeRun(null, null, null, null, null, null, null, insightRequestMetadata);
            if (rollingFilterDefinition.getStartDate() != null) {
                return (ZonedDateTime) rollingFilterDefinition.getStartDate();
            }
        }
        int intervalAmount = -rollingFilterDefinition.getCustomIntervalAmount();
        int intervalType = rollingFilterDefinition.getCustomIntervalType();
        /*Calendar cal = Calendar.getInstance();
        cal.setTime(now);*/
        if (!(rollingFilterDefinition.getField() instanceof AnalysisDateDimension)) {
            throw new RuntimeException("Report attempted to run a rolling filter on field " + rollingFilterDefinition.getField().toDisplay() + " - " + rollingFilterDefinition.getField().getAnalysisItemID());
        }
        int firstDayOfWeek = SecurityUtil.getFirstDayOfWeek();
        DayOfWeek targetDayOfWeek;
        if (firstDayOfWeek == Calendar.SUNDAY) {
            targetDayOfWeek = DayOfWeek.SUNDAY;
        } else if (firstDayOfWeek == Calendar.MONDAY) {
            targetDayOfWeek = DayOfWeek.MONDAY;
        } else if (firstDayOfWeek == Calendar.TUESDAY) {
            targetDayOfWeek = DayOfWeek.TUESDAY;
        } else if (firstDayOfWeek == Calendar.WEDNESDAY) {
            targetDayOfWeek = DayOfWeek.WEDNESDAY;
        } else if (firstDayOfWeek == Calendar.THURSDAY) {
            targetDayOfWeek = DayOfWeek.THURSDAY;
        } else if (firstDayOfWeek == Calendar.FRIDAY) {
            targetDayOfWeek = DayOfWeek.FRIDAY;
        } else {
            targetDayOfWeek = DayOfWeek.SATURDAY;
        }


        ZoneId zoneId = insightRequestMetadata.createZoneID();

        ZonedDateTime zdt = ZonedDateTime.now(zoneId);
        //ZonedDateTime zdt = ZonedDateTime.now(zoneId);
        switch (interval) {
            case CUSTOM:
                if (rollingFilterDefinition.getCustomBeforeOrAfter() == RollingFilterDefinition.LAST ||
                        rollingFilterDefinition.getCustomBeforeOrAfter() == RollingFilterDefinition.AFTER) {
                    switch (intervalType) {
                        case 0:
                            zdt = zdt.plusMinutes(intervalAmount);
                            break;
                        case 1:
                            zdt = zdt.plusHours(intervalAmount);
                          break;
                        case 2:
                            zdt = zdt.plusDays(intervalAmount);
                            break;
                        case 3:
                            zdt = zdt.plusWeeks(intervalAmount);
                            break;
                        case 4:
                            zdt = zdt.plusMonths(intervalAmount);
                            break;
                        case 5:
                            zdt = zdt.plusYears(intervalAmount);
                            break;
                    }
                }
                break;
            case DAY_TO_NOW:
                zdt = zdt.withHour(0).withMinute(0).withSecond(0).withNano(0);
                break;
            case WEEK_TO_NOW:
                zdt = zdt.minusWeeks(1).with(targetDayOfWeek);
                zdt = zdt.withHour(0).withMinute(0).withSecond(0).withNano(0);
                break;
            case MONTH_TO_NOW:
                zdt = zdt.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                break;
            case QUARTER_TO_NOW:
                int month = zdt.getMonthValue() - 1;
                int quarterMonth = month - (month % 3) + 1;
                zdt = zdt.withMonth(quarterMonth);
                zdt = zdt.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                break;
            case YEAR_TO_NOW:
                zdt = zdt.withDayOfYear(1).withHour(0).withDayOfMonth(1).withMinute(0).withSecond(0).withNano(0);
                break;
            case DAY:
                zdt = zdt.minusDays(1);
                break;
            case WEEK:
                zdt = zdt.minusWeeks(1);
                break;
            case MONTH:
                zdt = zdt.minusMonths(1);
                break;
            case YEAR:
                zdt = zdt.minusYears(1);
                break;
            case QUARTER:
                zdt = zdt.minusMonths(3);
                break;
            case LAST_FULL_DAY:
                zdt = zdt.minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                break;
            case LAST_FULL_WEEK:
                zdt = zdt.minusWeeks(2).with(targetDayOfWeek);
                zdt = zdt.withHour(0).withMinute(0).withSecond(0).withNano(0);
                break;
            case LAST_FULL_MONTH:
                zdt = zdt.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                break;
            case LAST_YEAR:
                zdt = zdt.minusYears(1).withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
                break;
            case LAST_FULL_QUARTER:
                // TODO: ?
                break;
            default:
                    /*if (rollingFilterDefinition.getStartDate() == null) {
                        return 0;
                    }*/
                return (ZonedDateTime) rollingFilterDefinition.getStartDate();
        }
        return zdt;
            /*Instant instant = zdt.toInstant();
            return Date.from(instant).getTime();*/
        //} else {


    }

    public static LocalDate findEndDate(RollingFilterDefinition rollingFilterDefinition, Date now) {
        return findEndDate(rollingFilterDefinition, now, new InsightRequestMetadata());
    }

    public static LocalDate findEndDate(RollingFilterDefinition rollingFilterDefinition, Date now, InsightRequestMetadata insightRequestMetadata) {
        if (rollingFilterDefinition.getEndDate() != null) {
            return (LocalDate) rollingFilterDefinition.getEndDate();
        }
        int interval = rollingFilterDefinition.getInterval();
        if (interval < 0) {
            for (CustomRollingInterval i : RollingFilterDefinition.createAdditionalIntervals()) {
                if (i.getIntervalNumber() == interval) {
                    rollingFilterDefinition.applyCalculationsBeforeRun(null, null, null, null, null, null, null, insightRequestMetadata);
                    if (rollingFilterDefinition.getEndDate() != null) {
                        return (LocalDate) rollingFilterDefinition.getEndDate();
                    }
                }
            }
        } else if (interval > ALL) {
            rollingFilterDefinition.applyCalculationsBeforeRun(null, null, null, null, null, null, null, insightRequestMetadata);
            if (rollingFilterDefinition.getEndDate() != null) {
                return (LocalDate) rollingFilterDefinition.getEndDate();
            }
        }
        int intervalAmount = rollingFilterDefinition.getCustomIntervalAmount();
        int intervalType = rollingFilterDefinition.getCustomIntervalType();
        ZoneId zoneId = insightRequestMetadata.createZoneID();



        int firstDayOfWeek = SecurityUtil.getFirstDayOfWeek();
        DayOfWeek targetDayOfWeek;
        if (firstDayOfWeek == Calendar.SUNDAY) {
            targetDayOfWeek = DayOfWeek.SUNDAY;
        } else if (firstDayOfWeek == Calendar.MONDAY) {
            targetDayOfWeek = DayOfWeek.MONDAY;
        } else if (firstDayOfWeek == Calendar.TUESDAY) {
            targetDayOfWeek = DayOfWeek.TUESDAY;
        } else if (firstDayOfWeek == Calendar.WEDNESDAY) {
            targetDayOfWeek = DayOfWeek.WEDNESDAY;
        } else if (firstDayOfWeek == Calendar.THURSDAY) {
            targetDayOfWeek = DayOfWeek.THURSDAY;
        } else if (firstDayOfWeek == Calendar.FRIDAY) {
            targetDayOfWeek = DayOfWeek.FRIDAY;
        } else {
            targetDayOfWeek = DayOfWeek.SATURDAY;
        }

        LocalDate zdt = LocalDate.now(zoneId);
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
                        case 2:
                            zdt = zdt.plusDays(intervalAmount);
                            break;
                        case 3:
                            zdt = zdt.plusWeeks(intervalAmount);
                            break;
                        case 4:
                            zdt = zdt.plusMonths(intervalAmount);
                            break;
                        case 5:
                            zdt = zdt.plusYears(intervalAmount);
                            break;
                    }
                }
                break;
            case LAST_FULL_DAY:
                zdt = zdt.minusDays(1);
                break;
            case LAST_FULL_WEEK:
                zdt = zdt.minusWeeks(1).with(targetDayOfWeek).minusDays(1);
                break;
            case LAST_FULL_MONTH:
                zdt = zdt.withDayOfMonth(1).minusDays(1);
                break;
            case LAST_YEAR:
                zdt = zdt.withDayOfYear(1).minusDays(1);
                break;
            case LAST_FULL_QUARTER:
                // TODO: ?
                break;
            default:
                return (LocalDate) rollingFilterDefinition.getEndDate();
        }
        return zdt;
    }

    public static ZonedDateTime findEndDateTime(RollingFilterDefinition rollingFilterDefinition, Date now, InsightRequestMetadata insightRequestMetadata) {
        if (rollingFilterDefinition.getEndDate() != null) {
            return (ZonedDateTime) rollingFilterDefinition.getEndDate();
        }
        int interval = rollingFilterDefinition.getInterval();
        if (interval < 0) {
            for (CustomRollingInterval i : RollingFilterDefinition.createAdditionalIntervals()) {
                if (i.getIntervalNumber() == interval) {
                    rollingFilterDefinition.applyCalculationsBeforeRun(null, null, null, null, null, null, null, insightRequestMetadata);
                    if (rollingFilterDefinition.getEndDate() != null) {
                        return (ZonedDateTime) rollingFilterDefinition.getEndDate();
                    }
                }
            }
        } else if (interval > ALL) {
            rollingFilterDefinition.applyCalculationsBeforeRun(null, null, null, null, null, null, null, insightRequestMetadata);
            if (rollingFilterDefinition.getEndDate() != null) {
                return (ZonedDateTime) rollingFilterDefinition.getEndDate();
            }
        }
        int intervalAmount = rollingFilterDefinition.getCustomIntervalAmount();
        int intervalType = rollingFilterDefinition.getCustomIntervalType();
        ZoneId zoneId = insightRequestMetadata.createZoneID();



        int firstDayOfWeek = SecurityUtil.getFirstDayOfWeek();
        DayOfWeek targetDayOfWeek;
        if (firstDayOfWeek == Calendar.SUNDAY) {
            targetDayOfWeek = DayOfWeek.SUNDAY;
        } else if (firstDayOfWeek == Calendar.MONDAY) {
            targetDayOfWeek = DayOfWeek.MONDAY;
        } else if (firstDayOfWeek == Calendar.TUESDAY) {
            targetDayOfWeek = DayOfWeek.TUESDAY;
        } else if (firstDayOfWeek == Calendar.WEDNESDAY) {
            targetDayOfWeek = DayOfWeek.WEDNESDAY;
        } else if (firstDayOfWeek == Calendar.THURSDAY) {
            targetDayOfWeek = DayOfWeek.THURSDAY;
        } else if (firstDayOfWeek == Calendar.FRIDAY) {
            targetDayOfWeek = DayOfWeek.FRIDAY;
        } else {
            targetDayOfWeek = DayOfWeek.SATURDAY;
        }

        ZonedDateTime zdt = ZonedDateTime.now(zoneId);
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
                        case 2:
                            zdt = zdt.plusDays(intervalAmount);
                            break;
                        case 3:
                            zdt = zdt.plusWeeks(intervalAmount);
                            break;
                        case 4:
                            zdt = zdt.plusMonths(intervalAmount);
                            break;
                        case 5:
                            zdt = zdt.plusYears(intervalAmount);
                            break;
                    }
                }
                break;
            case LAST_FULL_DAY:
                zdt = zdt.minusDays(1).withHour(23).withMinute(59).withSecond(59);
                break;
            case LAST_FULL_WEEK:
                zdt = zdt.minusWeeks(1).with(targetDayOfWeek).minusDays(1).withHour(23).withMinute(59).withSecond(59);
                break;
            case LAST_FULL_MONTH:
                zdt = zdt.withDayOfMonth(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);
                break;
            case LAST_YEAR:
                zdt = zdt.withDayOfYear(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);
                break;
            case LAST_FULL_QUARTER:
                // TODO: ?
                break;
            default:
                return (ZonedDateTime) rollingFilterDefinition.getEndDate();
        }
        return zdt;
    }

    @Override
    public void log(InsightRequestMetadata insightRequestMetadata, FilterDefinition filterDefinition) {
        if (limitDate != null) {
            insightRequestMetadata.addAudit(filterDefinition, "Start date on processing in memory is " + limitDate);
        }
        if (endDate != null) {
            insightRequestMetadata.addAudit(filterDefinition, "End date on processing in memory is " + endDate);
        }

        if (limitDateTime != null) {
            insightRequestMetadata.addAudit(filterDefinition, "Start date/time on processing in memory is " + limitDateTime);
        }
        if (endDateTime != null) {
            insightRequestMetadata.addAudit(filterDefinition, "End date/time on processing in memory is " + endDateTime);
        }
    }

    public boolean allows(Value value) {
        if (interval == MaterializedRollingFilterDefinition.ALL) {
            return true;
        }
        boolean allowed = false;
        if (dateTime) {
            if (interval == LAST_DAY) {
                allowed = true;
            } else if (value.type() == Value.DATE) {
                DateValue dateValue = (DateValue) value;

                if (mode == RollingFilterDefinition.AFTER) {
                    allowed = limitDateTime.isBefore(dateValue.getZonedDateTime()) || limitDateTime.isEqual(dateValue.getZonedDateTime());
                } else if (mode == RollingFilterDefinition.BEFORE) {
                    allowed = dateValue.getZonedDateTime().isBefore(endDateTime) || dateValue.getZonedDateTime().isEqual(endDateTime);
                } else {
                    allowed = (limitDateTime.isBefore(dateValue.getZonedDateTime()) || limitDateTime.isEqual(dateValue.getZonedDateTime())) &&
                            (dateValue.getZonedDateTime().isBefore(endDateTime) || dateValue.getZonedDateTime().isEqual(endDateTime));
                }
            } else {
                DateValue originalValue = (DateValue) value.getOriginalValue();
                if (originalValue != null) {
                    if (mode == RollingFilterDefinition.AFTER) {
                        allowed = limitDate.isBefore(originalValue.getLocalDate()) || limitDate.isEqual(originalValue.getLocalDate());
                    } else if (mode == RollingFilterDefinition.BEFORE) {
                        allowed = originalValue.getLocalDate().isBefore(endDate) || originalValue.getLocalDate().isEqual(endDate);
                    } else {
                        allowed = (limitDate.isBefore(originalValue.getLocalDate()) || limitDate.isEqual(originalValue.getLocalDate())) &&
                                (originalValue.getLocalDate().isBefore(endDate) || originalValue.getLocalDate().isEqual(endDate));
                    }
                }
            }
        } else {
            if (interval == LAST_DAY) {
                allowed = true;
            } else if (value.type() == Value.DATE) {
                DateValue dateValue = (DateValue) value;
                if (mode == RollingFilterDefinition.AFTER) {
                    allowed = limitDate.isBefore(dateValue.getLocalDate()) || limitDate.isEqual(dateValue.getLocalDate());
                } else if (mode == RollingFilterDefinition.BEFORE) {
                    allowed = dateValue.getLocalDate().isBefore(endDate) || dateValue.getLocalDate().isEqual(endDate);
                } else {

                    allowed = (limitDate.isBefore(dateValue.getLocalDate()) || limitDate.isEqual(dateValue.getLocalDate())) &&
                            (dateValue.getLocalDate().isBefore(endDate) || dateValue.getLocalDate().isEqual(endDate));
                }
            } else {
                DateValue originalValue = (DateValue) value.getOriginalValue();
                if (originalValue != null) {
                    if (mode == RollingFilterDefinition.AFTER) {
                        allowed = limitDate.isBefore(originalValue.getLocalDate()) || limitDate.isEqual(originalValue.getLocalDate());
                    } else if (mode == RollingFilterDefinition.BEFORE) {
                        allowed = originalValue.getLocalDate().isBefore(endDate) || originalValue.getLocalDate().isEqual(endDate);
                    } else {
                        allowed = (limitDate.isBefore(originalValue.getLocalDate()) || limitDate.isEqual(originalValue.getLocalDate())) &&
                                (originalValue.getLocalDate().isBefore(endDate) || originalValue.getLocalDate().isEqual(endDate));
                    }
                }
            }
        }

        return allowed;
    }
}
