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
 * Date: Jun 24, 2010
 * Time: 10:08:53 AM
 */
public class HighRiseCompany4To5 extends DataSourceMigration {
    public HighRiseCompany4To5(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseCompanySource.COMPANY_WORK_EMAIL), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseCompanySource.COMPANY_HOME_EMAIL), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseCompanySource.COMPANY_OTHER_EMAIL), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseCompanySource.COMPANY_FAX_PHONE), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseCompanySource.COMPANY_MOBILE_PHONE), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseCompanySource.COMPANY_HOME_PHONE), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseCompanySource.COMPANY_OFFICE_PHONE), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseCompanySource.COMPANY_STREET), true));
    }

    @Override
    public int fromVersion() {
        return 4;
    }

    @Override
    public int toVersion() {
        return 5;
    }
}
