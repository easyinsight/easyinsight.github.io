package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.datafeeds.DynamicFeed;
import com.easyinsight.datafeeds.FeedType;

/**
 * User: James Boe
 * Date: Jul 5, 2008
 * Time: 5:38:50 PM
 */
public class SalesforceSubFeed extends DynamicFeed {
    public FeedType getDataFeedType() {
        return FeedType.SALESFORCE_SUB;
    }
}
