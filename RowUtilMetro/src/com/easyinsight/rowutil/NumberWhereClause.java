package com.easyinsight.rowutil;

import com.easyinsight.rowutil.v3web.Comparison;
import com.easyinsight.rowutil.v3web.NumberWhere;
import com.easyinsight.rowutil.v3web.Where;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 10:32 AM
 */
public class NumberWhereClause extends WhereClause {

    private double value;
    private WhereComparison whereComparison;

    public NumberWhereClause(String key, double value, WhereComparison whereComparison) {
        super(key);
        this.value = value;
        this.whereComparison = whereComparison;
    }

    @Override
    void addToWhere(Where where) {
        NumberWhere numberWhere = new NumberWhere();
        numberWhere.setKey(getKey());
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
        numberWhere.setValue(value);
        numberWhere.setComparison(comparison);
        where.getNumberWheres().add(numberWhere);
    }
}
