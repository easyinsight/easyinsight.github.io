package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisZipCode;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.sql.SQLException;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Jun 24, 2010
 * Time: 10:08:53 AM
 */
public class HighRiseContact2To3 extends DataSourceMigration {
    public HighRiseContact2To3(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws SQLException {
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseContactSource.CONTACT_WORK_EMAIL), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseContactSource.CONTACT_HOME_EMAIL), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseContactSource.CONTACT_OFFICE_PHONE), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseContactSource.CONTACT_HOME_PHONE), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseContactSource.CONTACT_MOBILE_PHONE), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseContactSource.CONTACT_FAX_PHONE), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseContactSource.CONTACT_CITY), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseContactSource.CONTACT_STATE), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseContactSource.CONTACT_COUNTRY), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(HighRiseContactSource.CONTACT_STREET), true));
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