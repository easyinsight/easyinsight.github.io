package com.easyinsight.userupload;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;

import java.sql.SQLException;
import java.util.List;

/**
* User: jamesboe
* Date: Mar 27, 2010
* Time: 3:21:51 PM
*/
public abstract class UploadContext {
    public abstract String validateUpload(EIConnection conn) throws SQLException;
    public abstract List<AnalysisItem> guessFields(EIConnection conn, byte[] bytes) throws Exception;

    public abstract long createDataSource(String name, List<AnalysisItem> analysisItems, EIConnection conn, boolean accountVisible, byte[] bytes) throws Exception;

    public abstract List<String> getSampleValues(Key key);
}
