package com.easyinsight.storage;

import com.easyinsight.core.Key;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
* Date: Nov 10, 2008
* Time: 6:27:32 PM
*/
public class NumericWhere implements IWhere {
    private Key key;
    private double value;
    private Comparison comparison;

    public NumericWhere(Key key, double value, Comparison comparison) {
        this.key = key;
        this.value = value;
        this.comparison = comparison;
    }

    public String createWhereSQL() {
        String comparator = comparison.createComparison();
        return "k" + key.getKeyID() + " " + comparator + " ?";
    }

    public void setValue(PreparedStatement preparedStatement, int position) throws SQLException {
        preparedStatement.setDouble(position, value);
    }

    public Key getKey() {
        return key;
    }
}
