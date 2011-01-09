package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/4/11
 * Time: 10:14 AM
 */
public class BaseCampTodo6To7 extends DataSourceMigration {
    public BaseCampTodo6To7(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        changeDisplayName(BaseCampTodoSource.CONTENT, BaseCampTodoSource.TODO_ITEM_NAME);
    }

    @Override
    public int fromVersion() {
        return 6;
    }

    @Override
    public int toVersion() {
        return 7;
    }
}
