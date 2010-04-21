package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.analysis.AnalysisDateDimension;
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
 * Date: Apr 7, 2010
 * Time: 8:59:27 AM
 */
public class BaseCampTodo3To4 extends DataSourceMigration {
    public BaseCampTodo3To4(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws SQLException {
        addAnalysisItem(new AnalysisDateDimension(new NamedKey(BaseCampTodoSource.MILESTONE_COMPLETED_ON), true, AnalysisDateDimension.DAY_LEVEL));
        addAnalysisItem(new AnalysisDimension(new NamedKey(BaseCampTodoSource.MILESTONE_OWNER), true));
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