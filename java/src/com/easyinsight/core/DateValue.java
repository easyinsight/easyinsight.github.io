package com.easyinsight.core;

import com.easyinsight.analysis.AnalysisDateDimension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * User: James Boe
 * Date: Jul 1, 2008
 * Time: 11:01:23 AM
 */
public class DateValue extends Value implements Serializable {
    private Date date;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int dateLevel;
    private boolean dateTime;
    private LocalDate localDate;
    private ZonedDateTime zonedDateTime;

    private String format;
    private static final long serialVersionUID = 8170674055682369820L;

    public DateValue() {
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    public int getDateLevel() {
        return dateLevel;
    }

    public void setDateLevel(int dateLevel) {
        this.dateLevel = dateLevel;
    }

    public boolean isDateTime() {
        return dateTime;
    }

    public void setDateTime(boolean dateTime) {
        this.dateTime = dateTime;
    }

    public void calculate(boolean dateTime, ZoneId zoneId) {
        if (date != null) {
            if (!dateTime) {
                if (getDateLevel() == AnalysisDateDimension.HOUR_LEVEL || getDateLevel() == AnalysisDateDimension.MINUTE_LEVEL) {
                    if (zonedDateTime == null) {
                        zonedDateTime = date.toInstant().atZone(zoneId);
                    }
                    year = zonedDateTime.getYear();
                    month = zonedDateTime.getMonthValue() - 1;
                    day = zonedDateTime.getDayOfMonth();
                    hour = zonedDateTime.getHour();
                    minute = zonedDateTime.getMinute();
                } else {
                    if (localDate == null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        localDate = LocalDate.of(cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH) + 1,
                                cal.get(Calendar.DAY_OF_MONTH));
                    }
                    year = localDate.getYear();
                    month = localDate.getMonthValue() - 1;
                    day = localDate.getDayOfMonth();
                }
            } else {
                if (zonedDateTime == null) {
                    zonedDateTime = date.toInstant().atZone(zoneId);
                }
                year = zonedDateTime.getYear();
                month = zonedDateTime.getMonthValue() - 1;
                day = zonedDateTime.getDayOfMonth();
                hour = zonedDateTime.getHour();
                minute = zonedDateTime.getMinute();
            }
        }
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        if (date == null) {
            return "";
        }
        if (cachedFormat == null) {
            if (format == null) {
                cachedFormat = new SimpleDateFormat("yyyy-MM-dd");
            } else {
                cachedFormat = new SimpleDateFormat(format);
            }
        }
        return cachedFormat.format(date);
    }

    private transient DateFormat cachedFormat;

    public String createPerformantString() {
        if (date != null) {
            return String.valueOf(date.getTime());
        } else {
            return "";
        }
    }

    public DateValue(Date date) {
        this.date = date;
    }

    public DateValue(Date date, LocalDate localDate) {
        this.date = date;
        this.localDate = localDate;
    }



    public DateValue(Date date, Value sortValue) {
        this.date = date;
        setSortValue(sortValue);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int type() {
        return Value.DATE;
    }

    @Nullable
    public Double toDouble() {
        return (double) date.getTime();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateValue dateValue = (DateValue) o;

        return date.equals(dateValue.date);

    }

    public int hashCode() {
        return date.hashCode();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int compareTo(@NotNull Value value) {
        if (value.type() == Value.DATE) {
            DateValue date2 = (DateValue) value;
            return getDate().compareTo(date2.getDate());
        } else if (value.type() == Value.EMPTY) {
            return -1;
        } else if (value.type() == Value.STRING) {
            return -1;
        } else if (value.type() == Value.NUMBER) {
            return -1;
        }
        return 0;
    }
}
