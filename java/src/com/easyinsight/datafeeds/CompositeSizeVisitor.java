package com.easyinsight.datafeeds;

import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Jul 15, 2008
 * Time: 8:44:35 PM
 */
public class CompositeSizeVisitor extends CompositeFeedNodeVisitor {
    private int size;
    private FeedStorage feedStorage = new FeedStorage();

    protected void accept(CompositeFeedNode compositeFeedNode) throws SQLException {
        size += feedStorage.getFeedDefinitionData(compositeFeedNode.getDataFeedID()).getSize();
    }

    public int getSize() {
        return size;
    }
}
