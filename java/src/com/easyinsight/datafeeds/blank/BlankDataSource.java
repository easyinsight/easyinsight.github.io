package com.easyinsight.datafeeds.blank;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Account;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/23/14
 * Time: 11:17 AM
 */
public class BlankDataSource extends ServerDataSourceDefinition {

    @Override
    public FeedType getFeedType() {
        return FeedType.BLANK;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PUSH;
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        return new ArrayList<AnalysisItem>();
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }

    @Override
    protected void clearData(DataStorage dataStorage) throws SQLException {
        // don't clear data!
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }
}