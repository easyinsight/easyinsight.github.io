package com.easyinsight.rowutil;

import com.easyinsight.rowutil.v3web.DayWhere;
import com.easyinsight.rowutil.v3web.Where;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 10:32 AM
 */
public class DayWhereClause extends WhereClause {
    private int dayOfYear;
    private int year;

    public DayWhereClause(String key, int dayOfYear, int year) {
        super(key);
        this.dayOfYear = dayOfYear;
        this.year = year;
    }

    @Override
    void addToWhere(Where where) {
        DayWhere dayWhere = new DayWhere();
        dayWhere.setKey(getKey());
        dayWhere.setDayOfYear(dayOfYear);
        dayWhere.setYear(year);
        where.getDayWheres().add(dayWhere);
    }
}
