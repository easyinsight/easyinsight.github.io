package com.easyinsight.storage;

import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.StringValue;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

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

    public int setValue(PreparedStatement preparedStatement, int position) throws SQLException {
        preparedStatement.setString(position, value);
        return position + 1;
    }

    public List<String> getExtraTables() {
        return new ArrayList<String>();
    }

    public Key getKey() {
        return key;
    }

    public boolean hasConcreteValue() {
        return true;
    }

    public Value getConcreteValue() {
        return new StringValue(value);
    }
}
