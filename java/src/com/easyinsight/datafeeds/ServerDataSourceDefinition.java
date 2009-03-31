package com.easyinsight.datafeeds;

import com.easyinsight.users.Credentials;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.analysis.AnalysisItem;

import java.util.Map;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Mar 30, 2009
 * Time: 8:50:59 PM
 */
public abstract class ServerDataSourceDefinition extends FeedDefinition {
    public abstract int getRequiredAccountTier();
    public abstract FeedType getFeedType();
    public abstract int getCredentialsDefinition();
    public abstract String validateCredentials(Credentials credentials);
    public abstract DataSet getDataSet(Credentials credentials, Map<String, Key> keys);
    public abstract Map<String, Key> newDataSourceFields(Credentials credentials);
    public abstract List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet);
    public abstract void customStorage(Connection conn) throws SQLException;
    public abstract void customLoad(Connection conn) throws SQLException;
}
