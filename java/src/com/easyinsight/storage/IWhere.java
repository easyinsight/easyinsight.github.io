package com.easyinsight.storage;

import com.easyinsight.core.Key;
import com.easyinsight.core.Value;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * User: James Boe
* Date: Nov 10, 2008
* Time: 6:27:11 PM
*/
public interface IWhere {
    String createWhereSQL();
    public int setValue(PreparedStatement preparedStatement, int position) throws SQLException;
    public List<String> getExtraTables();
    Key getKey();
    boolean hasConcreteValue();
    Value getConcreteValue();
}
