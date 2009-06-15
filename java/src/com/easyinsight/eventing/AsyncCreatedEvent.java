package com.easyinsight.eventing;

import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.datafeeds.FeedDefinition;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 12, 2009
 * Time: 11:06:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class AsyncCreatedEvent extends EIEvent {
    public static final String ASYNC_CREATED = "asyncCreated";
    private long userID;
    private ScheduledTask task;
    private String feedName;
    private long feedID;

    public long getFeedID() {
        return feedID;
    }

    public AsyncCreatedEvent() {
        super(ASYNC_CREATED);
    }

    public long getUserID() {
        return userID;
    }

    public ScheduledTask getTask() {
        return task;
    }

    public void setTask(ScheduledTask task) {
        this.task = task;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }
}
