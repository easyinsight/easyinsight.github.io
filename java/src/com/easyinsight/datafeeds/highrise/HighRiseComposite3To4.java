package com.easyinsight.datafeeds.highrise;

import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.sql.SQLException;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Mar 23, 2010
 * Time: 2:10:43 PM
 */
public class HighRiseComposite3To4 extends DataSourceMigration {
    public HighRiseComposite3To4(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws SQLException {
        addChildDataSource(new HighRiseCompanyNotesSource(), conn);
        addChildDataSource(new HighRiseDealNotesSource(), conn);
        addChildDataSource(new HighRiseCaseNotesSource(), conn);
        addChildDataSource(new HighRiseContactNotesSource(), conn);
    }

    @Override
    public int fromVersion() {
        return 3;
    }

    @Override
    public int toVersion() {
        return 4;
    }
}
