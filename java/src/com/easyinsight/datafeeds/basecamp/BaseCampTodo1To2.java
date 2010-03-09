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
 * Date: Mar 8, 2010
 * Time: 9:00:37 PM
 */
public class BaseCampTodo1To2 extends DataSourceMigration {
    public BaseCampTodo1To2(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws SQLException {
        addAnalysisItem(new AnalysisDimension(new NamedKey(BaseCampTodoSource.MILESTONE_LAST_COMMENT), true));
    }
}
