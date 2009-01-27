package com.easyinsight.storage;

import com.easyinsight.core.Key;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * User: James Boe
* Date: Nov 10, 2008
* Time: 6:27:51 PM
*/
public class DateWhere implements IWhere {
    private Key key;
    private Date value;
    private Comparison comparison;

    public DateWhere(Key key, Date value, Comparison comparison) {
        this.key = key;
        this.value = value;
        this.comparison = comparison;
    }

    public String createWhereSQL() {
        String comparator = comparison.createComparison();
        return "k" + key.getKeyID() + " " + comparator + " ?";
    }

    public void setValue(PreparedStatement preparedStatement, int position) throws SQLException {
        preparedStatement.setDate(position, new java.sql.Date(value.getTime()));
    }

    public Key getKey() {
        return key;
    }
}
