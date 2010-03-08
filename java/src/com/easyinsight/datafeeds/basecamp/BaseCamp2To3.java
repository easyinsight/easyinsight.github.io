package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Mar 1, 2010
 * Time: 10:00:25 AM
 */
public class BaseCamp2To3 extends DataSourceMigration {
    public BaseCamp2To3(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws SQLException {
        BaseCampCommentSource baseCampCommentSource = new BaseCampCommentSource();
        addChildDataSource(baseCampCommentSource, Arrays.asList(new ChildConnection(FeedType.BASECAMP, FeedType.BASECAMP_COMMENT, BaseCampTodoSource.ITEMID,
                BaseCampCommentSource.TODO_ITEM_ID)), conn);
    }
}
