package com.easyinsight.core;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
    private boolean dateTime;

    private String format;
    private static final long serialVersionUID = 8170674055682369820L;

    public DateValue() {
    }

    public boolean isDateTime() {
        return dateTime;
    }

    public void setDateTime(boolean dateTime) {
        this.dateTime = dateTime;
    }

    public void calculate(Calendar cal) {
        if (date != null) {
            cal.setTime(date);
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
            hour = cal.get(Calendar.HOUR_OF_DAY);
            minute = cal.get(Calendar.MINUTE);
            /*Calendar cal2 = Calendar.getInstance();
            cal2.set(Calendar.YEAR, year);
            cal2.set(Calendar.MONTH, month);
            cal2.set(Calendar.DAY_OF_MONTH, day);
            cal2.set(Calendar.HOUR, hour);
            cal2.set(Calendar.MINUTE, minute);
            date = cal2.getTime();
            System.out.println("and now time = " + date);*/
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
        if (format == null) {
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        } else {
            return new SimpleDateFormat(format).format(date);
        }
    }

    public DateValue(Date date) {
        this.date = date;
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
}
