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
 * Date: 1/4/11
 * Time: 10:14 AM
 */
public class BaseCampTodo7To8 extends DataSourceMigration {
    public BaseCampTodo7To8(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        /*addAnalysisItem(new AnalysisDimension(new NamedKey(BaseCampTodoSource.CALENDAR_EVENT_START)));*/
        addAnalysisItem(new AnalysisDimension(new NamedKey(BaseCampTodoSource.CALENDAR_EVENT_TYPE)));
    }

    @Override
    public int fromVersion() {
        return 7;
    }

    @Override
    public int toVersion() {
        return 8;
    }
}
