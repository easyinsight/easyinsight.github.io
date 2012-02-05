package com.easyinsight.datafeeds.pivotaltracker;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.util.Map;

/**
 * User: jamesboe
 * Date: 2/4/12
 * Time: 5:38 PM
 */
public class PivotalTracker1To2 extends DataSourceMigration {
    public PivotalTracker1To2(FeedDefinition dataSource) {
        super(dataSource);
    }

    @Override
    public void migrate(Map<String, Key> keys, EIConnection conn) throws Exception {
        addAnalysisItem(new AnalysisDimension(new NamedKey(PivotalTrackerBaseSource.STORY_DESCRIPTION), true));
        addAnalysisItem(new AnalysisDimension(new NamedKey(PivotalTrackerBaseSource.STORY_ID), true));
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
