package com.easyinsight.storage;

import com.easyinsight.core.Key;
import com.easyinsight.database.Database;

import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: 4/10/14
 * Time: 1:56 PM
 */
public interface IStorageDialect {
    String defineTableSQL(boolean hugeTable);

    String getColumnDefinitionSQL(Key key, int type, boolean hugeTable);

    void createTempTable(String sql, Database database) throws SQLException;

    String defineTempInsertTable();

    String defineTempUpdateTable();
}
