package com.easyinsight.eventing;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.scheduler.ScheduledTask;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 12, 2009
 * Time: 11:29:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class AsyncRunningEvent extends EIEvent {
    public static final String ASYNC_RUNNING = "asyncRunning";

    public String getFeedName() {
        return feedName;
    }

    public long getFeedID() {
        return feedID;
    }

    private FeedDefinition feedDefinition;
    private ScheduledTask task;
    private String feedName;
    private long feedID;

    public void setFeedDefinition(FeedDefinition feedDefinition) {
        this.feedDefinition = feedDefinition;
    }

    public void setTask(ScheduledTask task) {
        this.task = task;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    private long userID;

    public AsyncRunningEvent() {
        super(ASYNC_RUNNING);
    }

    public FeedDefinition getFeedDefinition() {
        return feedDefinition;
    }

    public ScheduledTask getTask() {
        return task;
    }

    public long getUserID() {
        return userID;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }
}
