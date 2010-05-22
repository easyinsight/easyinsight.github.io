package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.sql.SQLException;
import java.util.Map;

/**
 * User: jamesboe
 * Date: May 21, 2010
 * Time: 10:10:53 AM
 */
public class HighRiseDeal2To3 extends DataSourceMigration {
    public HighRiseDeal2To3(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws SQLException {
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseDealSource.DEAL_OWNER), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseDealSource.RESPONSIBLE_PARTY), true));
    }

    @Override
    public int fromVersion() {
        return 2;
    }

    @Override
    public int toVersion() {
        return 3;
    }
}
