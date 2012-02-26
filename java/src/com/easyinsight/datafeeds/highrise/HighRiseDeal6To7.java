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
public class HighRiseDeal6To7 extends DataSourceMigration {
    public HighRiseDeal6To7(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        migrateAnalysisItem(HighRiseDealSource.DESCRIPTION, new AnalysisText(HighRiseDealSource.DESCRIPTION, true));
    }

    @Override
    public int fromVersion() {
        return 6;
    }

    @Override
    public int toVersion() {
        return 7;
    }
}