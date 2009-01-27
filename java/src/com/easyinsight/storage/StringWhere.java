package com.easyinsight.storage;

import com.easyinsight.core.Key;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
* Date: Nov 10, 2008
* Time: 6:27:27 PM
*/
public class StringWhere implements IWhere {
    private Key key;
    private String value;

    public StringWhere(Key key, String value) {
        this.key = key;
        this.value = value;
    }

    public String createWhereSQL() {
        return "k" + key.getKeyID() + " = ?";
    }

    public void setValue(PreparedStatement preparedStatement, int position) throws SQLException {
        preparedStatement.setString(position, value);
    }

    public Key getKey() {
        return key;
    }
}
