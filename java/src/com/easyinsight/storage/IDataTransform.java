package com.easyinsight.storage;

import com.easyinsight.analysis.ActualRow;
import com.easyinsight.analysis.IRow;
import com.easyinsight.database.EIConnection;

/**
* User: jamesboe
* Date: 4/3/12
* Time: 11:36 AM
*/
public interface IDataTransform {
    public void handle(EIConnection conn, IRow row);
}
