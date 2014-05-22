package com.easyinsight.storage;

import com.easyinsight.core.Key;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;

import java.sql.SQLException;
import java.util.List;

/**
 * User: jamesboe
 * Date: 4/10/14
 * Time: 1:56 PM
 */
public interface IStorageDialect {
    String defineTableSQL(boolean hugeTable);

    String getColumnDefinitionSQL(Key key, int type, boolean hugeTable);

    void createTempTable(String sql, Database database, boolean insert) throws SQLException;

    String defineTempInsertTable();

    String defineTempUpdateTable();

    void insertData(DataSet dataSet, List<IDataTransform> transforms, EIConnection coreDBConn, Database storageDatabase, DateDimCache dateDimCache) throws Exception;

    void commit();
}
