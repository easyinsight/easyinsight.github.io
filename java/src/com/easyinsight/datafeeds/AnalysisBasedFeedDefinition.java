package com.easyinsight.datafeeds;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        return new AnalysisBasedFeed();
    }
}
