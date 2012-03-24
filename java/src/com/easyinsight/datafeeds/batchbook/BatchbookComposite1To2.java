package com.easyinsight.datafeeds.batchbook;

import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.util.Map;

/**
 * User: jamesboe
 * Date: 3/19/12
 * Time: 10:26 AM
 */
public class BatchbookComposite1To2 extends DataSourceMigration {
    public BatchbookComposite1To2(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        addChildDataSource(new BatchbookCommunicationsPartySource(), conn);
    }

    @Override
    public int fromVersion() {
        return 1;
    }

    @Override
    public int toVersion() {
        return 2;
    }
}
