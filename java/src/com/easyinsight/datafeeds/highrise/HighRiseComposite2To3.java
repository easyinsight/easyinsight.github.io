package com.easyinsight.datafeeds.highrise;

import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;

import java.sql.SQLException;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Mar 23, 2010
 * Time: 2:10:43 PM
 */
public class HighRiseComposite2To3 extends DataSourceMigration {
    public HighRiseComposite2To3(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws SQLException {
        HighRiseTaskSource taskSource = new HighRiseTaskSource();
        HighRiseCaseSource caseSource = new HighRiseCaseSource();
        HighRiseEmailSource emailSource = new HighRiseEmailSource();
        addChildDataSource(taskSource, conn);
        addChildDataSource(caseSource, conn);
        addChildDataSource(emailSource, conn);
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
