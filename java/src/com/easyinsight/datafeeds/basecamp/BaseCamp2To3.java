package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.analysis.AnalysisDimension;
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
        BaseCampCompanySource companySource = new BaseCampCompanySource();
        BaseCampCompanyProjectJoinSource joinSource = new BaseCampCompanyProjectJoinSource();
        addChildDataSource(companySource, conn);
        addChildDataSource(joinSource, conn);
        addConnection(new ChildConnection(FeedType.BASECAMP, FeedType.BASECAMP_COMPANY_PROJECT_JOIN, BaseCampTodoSource.PROJECTID,
                BaseCampCompanyProjectJoinSource.PROJECT_ID), conn);
        addConnection(new ChildConnection(FeedType.BASECAMP_COMPANY, FeedType.BASECAMP_COMPANY_PROJECT_JOIN, BaseCampCompanySource.COMPANY_ID,
                BaseCampCompanyProjectJoinSource.COMPANY_ID), conn);
        AnalysisDimension contentDim = (AnalysisDimension) findAnalysisItem(BaseCampTodoSource.CONTENT);
        if (contentDim != null) {
            contentDim.setDisplayName(BaseCampTodoSource.TODO_ITEM_NAME);
        }
    }
}
