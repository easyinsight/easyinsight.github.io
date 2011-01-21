package com.easyinsight.datafeeds;

import com.easyinsight.core.DataSourceDescriptor;

/**
 * User: James Boe
 * Date: Sep 13, 2008
 * Time: 4:46:18 PM
 */
public class FeedResponse {

    public static final int SUCCESS = 1;
    public static final int NEED_LOGIN = 2;
    public static final int REJECTED = 3;

    private int status;
    private DataSourceDescriptor feedDescriptor;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataSourceDescriptor getFeedDescriptor() {
        return feedDescriptor;
    }

    public void setFeedDescriptor(DataSourceDescriptor feedDescriptor) {
        this.feedDescriptor = feedDescriptor;
    }

    public FeedResponse() {
    }

    public FeedResponse(int status, DataSourceDescriptor feedDescriptor) {
        this.status = status;
        this.feedDescriptor = feedDescriptor;
    }
}
