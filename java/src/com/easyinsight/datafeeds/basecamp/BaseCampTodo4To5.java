package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.util.Map;

/**
 * User: jamesboe
 * Date: Apr 7, 2010
 * Time: 8:59:27 AM
 */
public class BaseCampTodo4To5 extends DataSourceMigration {
    public BaseCampTodo4To5(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        addAnalysisItem(new AnalysisDimension(new NamedKey(BaseCampTodoSource.ANNOUNCEMENT), true));
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