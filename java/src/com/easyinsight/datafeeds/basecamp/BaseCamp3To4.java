package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.analysis.AnalysisDimension;
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
 * Date: Mar 1, 2010
 * Time: 10:00:25 AM
 */
public class BaseCamp3To4 extends DataSourceMigration {
    public BaseCamp3To4(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws SQLException {
        BaseCampCommentsSource commentsSource = new BaseCampCommentsSource();
        addChildDataSource(commentsSource, conn);
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