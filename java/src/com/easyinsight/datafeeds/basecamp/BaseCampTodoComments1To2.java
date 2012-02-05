package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisText;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 10:41 AM
 */
public class BaseCampTodoComments1To2 extends DataSourceMigration {
    public BaseCampTodoComments1To2(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        AnalysisDimension projectDimension = new AnalysisDimension(new NamedKey(BaseCampTodoCommentsSource.PROJECT_ID), true);
        projectDimension.setHidden(true);
        addAnalysisItem(projectDimension);
        migrateAnalysisItem(BaseCampTodoCommentsSource.COMMENT_BODY, new AnalysisText(BaseCampTodoCommentsSource.COMMENT_BODY, true));
    }

    @Override
    public int fromVersion() {
        return 1;
    }

    @Override
    public int toVersion() {
        return 2;
    }
}
