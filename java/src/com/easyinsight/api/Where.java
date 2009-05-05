package com.easyinsight.api;

import org.jetbrains.annotations.Nullable;

/**
 * User: James Boe
 * Date: Jan 15, 2009
 * Time: 2:41:29 PM
 */
public class Where {
    private StringWhere[] stringWheres;
    private NumberWhere[] numberWheres;
    private DateWhere[] dateWheres;
    private DayWhere[] dayWheres;

    @Nullable
    public StringWhere[] getStringWheres() {
        return stringWheres;
    }

    public void setStringWheres(StringWhere[] stringWheres) {
        this.stringWheres = stringWheres;
    }

    @Nullable
    public NumberWhere[] getNumberWheres() {
        return numberWheres;
    }

    public void setNumberWheres(NumberWhere[] numberWheres) {
        this.numberWheres = numberWheres;
    }

    @Nullable
    public DateWhere[] getDateWheres() {
        return dateWheres;
    }

    public void setDateWheres(DateWhere[] dateWheres) {
        this.dateWheres = dateWheres;
    }

    @Nullable
    public DayWhere[] getDayWheres() {
        return dayWheres;
    }

    public void setDayWheres(DayWhere[] dayWheres) {
        this.dayWheres = dayWheres;
    }
}
