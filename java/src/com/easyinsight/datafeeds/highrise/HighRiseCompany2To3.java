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
 * Date: Jun 24, 2010
 * Time: 10:08:53 AM
 */
public class HighRiseCompany2To3 extends DataSourceMigration {
    public HighRiseCompany2To3(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        addAnalysisItem(new AnalysisDateDimension(new NamedKey(HighRiseCompanySource.UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
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
