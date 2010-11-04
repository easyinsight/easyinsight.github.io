package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisZipCode;
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
public class HighRiseCompany1To2 extends DataSourceMigration {
    public HighRiseCompany1To2(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseCompanySource.BACKGROUND), true));
        addAnalysisItem(new AnalysisZipCode(new NamedKey(HighRiseCompanySource.ZIP_CODE), true));
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
