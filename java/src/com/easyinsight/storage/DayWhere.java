package com.easyinsight.storage;

import com.easyinsight.core.Key;
import com.easyinsight.core.Value;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;

/**
 * User: James Boe
 * Date: Mar 6, 2009
 * Time: 11:24:10 AM
 */
public class DayWhere implements IWhere {

    private Key key;
    private int year;
    private int dayOfYear;

    public DayWhere(Key key, int year, int dayOfYear) {
        this.key = key;
        this.year = year;
        this.dayOfYear = dayOfYear;
    }

    public String createWhereSQL() {
        return "datedim_" + key.getKeyID() + "_id = date_dimension.date_dimension_id AND date_dimension.dim_year = ? AND date_dimension.dim_day_of_year = ?";
    }

    public int setValue(PreparedStatement preparedStatement, int position) throws SQLException {
        preparedStatement.setInt(position, year);
        preparedStatement.setInt(position + 1, dayOfYear);
        return position + 2;
    }

    public List<String> getExtraTables() {
        return Arrays.asList("date_dimension");
    }

    public Key getKey() {
        return key;
    }

    public boolean hasConcreteValue() {
        return false;
    }

    public Value getConcreteValue() {
        throw new UnsupportedOperationException();
    }
}
