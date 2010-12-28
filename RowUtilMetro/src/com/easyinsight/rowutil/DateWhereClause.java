package com.easyinsight.rowutil;

import com.easyinsight.rowutil.v3web.Comparison;
import com.easyinsight.rowutil.v3web.DateWhere;
import com.easyinsight.rowutil.v3web.Where;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 10:32 AM
 */
public class DateWhereClause extends WhereClause {
    private Date value;
    private WhereComparison whereComparison;

    public DateWhereClause(String key, Date value, WhereComparison whereComparison) {
        super(key);
        this.value = value;
        this.whereComparison = whereComparison;
    }

    @Override
    void addToWhere(Where where) throws DatatypeConfigurationException {
        DateWhere dateWhere = new DateWhere();
        dateWhere.setKey(getKey());
        Comparison comparison;
        switch (whereComparison) {
            case EQUAL_TO:
                comparison = Comparison.EQUAL_TO;
                break;
            case LESS_THAN:
                comparison = Comparison.LESS_THAN;
                break;
            case GREATER_THAN:
                comparison = Comparison.GREATER_THAN;
                break;
            default:
                throw new RuntimeException("You must define a comparison as part of a number where.");
        }
        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
        cal.setTime(value);
        XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        dateWhere.setValue(xmlCalendar);
        dateWhere.setComparison(comparison);
        where.getDateWheres().add(dateWhere);
    }
}
