package com.easyinsight.dataengine;

import com.easyinsight.IDataService;
import com.easyinsight.FeedMetadata;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Aug 5, 2008
 * Time: 5:55:33 PM
 */
public class FeedMetadataRequest extends EngineRequest implements Serializable {
    private long feedID;

    public FeedMetadataRequest() {
    }

    public FeedMetadataRequest(long feedID) {
        setInvocationID(generateInvocationID());
        this.feedID = feedID;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public EngineResponse execute(IDataService dataService) {
        FeedMetadata feedMetadata = dataService.getFeedMetadata(feedID, null);
        return new EngineResponse(getInvocationID(), feedMetadata);
    }
}
