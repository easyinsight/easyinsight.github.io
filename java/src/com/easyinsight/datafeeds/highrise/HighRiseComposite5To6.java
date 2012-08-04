package com.easyinsight.datafeeds.highrise;

import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.util.Map;

/**
 * User: jamesboe
 * Date: Mar 23, 2010
 * Time: 2:10:43 PM
 */
public class HighRiseComposite5To6 extends DataSourceMigration {
    public HighRiseComposite5To6(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        addChildDataSource(new HighRiseActivitySource(), conn);
    }

    @Override
    public int fromVersion() {
        return 5;
    }

    @Override
    public int toVersion() {
        return 6;
    }
}
