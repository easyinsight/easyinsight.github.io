package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisText;
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
public class HighRiseDeal7To8 extends DataSourceMigration {
    public HighRiseDeal7To8(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseDealSource.AUTHOR), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseDealSource.CURRENCY), true));
    }

    @Override
    public int fromVersion() {
        return 7;
    }

    @Override
    public int toVersion() {
        return 8;
    }
}