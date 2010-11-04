package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.util.Map;

/**
 * User: jamesboe
 * Date: May 21, 2010
 * Time: 10:10:53 AM
 */
public class HighRiseDeal5To6 extends DataSourceMigration {
    public HighRiseDeal5To6(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseDealSource.DESCRIPTION), true));
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