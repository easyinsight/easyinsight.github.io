package com.easyinsight.storage;

import com.easyinsight.core.Key;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
* Date: Nov 10, 2008
* Time: 6:27:11 PM
*/
public interface IWhere {
    String createWhereSQL();
    public void setValue(PreparedStatement preparedStatement, int position) throws SQLException;
    Key getKey();
}
