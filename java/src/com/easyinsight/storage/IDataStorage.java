package com.easyinsight.storage;

import com.easyinsight.dataset.DataSet;

import java.sql.SQLException;
import java.util.List;

/**
 * User: jamesboe
 * Date: 9/9/11
 * Time: 10:25 AM
 */
public interface IDataStorage {
    void commit() throws SQLException;

    void insertData(DataSet dataSet) throws Exception;

    void updateData(DataSet dataSet, List<IWhere> wheres) throws Exception;
}
