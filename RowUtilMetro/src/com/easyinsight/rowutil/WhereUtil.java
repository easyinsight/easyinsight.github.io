package com.easyinsight.rowutil;

import com.easyinsight.rowutil.webservice.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: jamesboe
 * Date: Oct 30, 2009
 * Time: 10:32:12 AM
 */
public class WhereUtil {

    private Where where;

    public WhereUtil(Where where) {
        this.where = where;
    }

    public void and(String key, String value) {
        StringWhere stringWhere = new StringWhere();
        stringWhere.setKey(key);
        stringWhere.setValue(value);
        where.getStringWheres().add(stringWhere);
    }

    public void and(String key, int dayOfYear, int year) {
        DayWhere dayWhere = new DayWhere();
        dayWhere.setDayOfYear(dayOfYear);
        dayWhere.setKey(key);
        dayWhere.setYear(year);
        where.getDayWheres().add(dayWhere);
    }

    public void and(String key, Comparison comparison, double value) {
        NumberWhere numberWhere = new NumberWhere();
        numberWhere.setKey(key);
        numberWhere.setComparison(comparison);
        numberWhere.setValue(value);
        where.getNumberWheres().add(numberWhere);
    }

    public void and(String key, Comparison comparison, Date date) {
        try {
            DateWhere dateWhere = new DateWhere();
            dateWhere.setKey(key);
            dateWhere.setComparison(comparison);
            GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
            cal.setTime(date);
            XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            dateWhere.setValue(xmlCalendar);
            where.getDateWheres().add(dateWhere);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
