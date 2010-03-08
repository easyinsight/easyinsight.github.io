package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.util.Map;

/**
 * User: jamesboe
 * Date: Oct 30, 2009
 * Time: 5:26:53 PM
 */
public class HighRise1To2 extends DataSourceMigration {
    public HighRise1To2(FeedDefinition dataSource) {
        super(dataSource);
    }

    public void migrate(Map<String, Key> keys, EIConnection conn) {
        addAnalysisItem(new AnalysisDateDimension(new NamedKey(HighRiseDealSource.STATUS_CHANGED_ON), true, AnalysisDateDimension.DAY_LEVEL));
    }

    public int fromVersion() {
        return 1;
    }

    public int toVersion() {
        return 2;
    }
}