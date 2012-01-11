package com.easyinsight.datafeeds.harvest;

import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.util.Map;

/**
 * User: jamesboe
 * Date: 12/29/11
 * Time: 3:19 PM
 */
public class Harvest1To2 extends DataSourceMigration {
    public Harvest1To2(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        HarvestUserAssignmentSource harvestUserAssignmentSource = new HarvestUserAssignmentSource();
        addChildDataSource(harvestUserAssignmentSource, conn);
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
