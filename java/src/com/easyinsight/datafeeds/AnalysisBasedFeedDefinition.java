package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisStorage;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: May 5, 2008
 * Time: 2:01:56 PM
 */

public class AnalysisBasedFeedDefinition extends FeedDefinition {

    public FeedType getFeedType() {
        return FeedType.ANALYSIS_BASED;
    }

    public Feed createFeedObject() {
        AnalysisBasedFeed feed = new AnalysisBasedFeed();
        feed.setAnalysisDefinition(new AnalysisStorage().getAnalysisDefinition(getAnalysisDefinitionID()));
        return feed;
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        setFields(new FeedStorage().retrieveFields(new AnalysisStorage().getAnalysisDefinition(getAnalysisDefinitionID(), conn).getDataFeedID(), conn));
    }
}
