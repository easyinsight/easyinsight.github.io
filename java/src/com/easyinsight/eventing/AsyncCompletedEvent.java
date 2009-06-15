package com.easyinsight.eventing;

import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.datafeeds.FeedDefinition;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 12, 2009
 * Time: 2:01:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class AsyncCompletedEvent extends EIEvent {
    public static final String ASYNC_COMPLETED = "asyncCompleted";
    private ScheduledTask task;
    private long userID;
    private int feedID;

    public AsyncCompletedEvent() {
        super(ASYNC_COMPLETED);
    }

    public int getFeedID() {

        return feedID;
    }

    public String getFeedName() {
        return feedName;
    }

    private String feedName;

    public void setTask(ScheduledTask task) {
        this.task = task;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setFeedDefinition(FeedDefinition feedDefinition) {
        this.feedDefinition = feedDefinition;
    }

    private FeedDefinition feedDefinition;

    public ScheduledTask getTask() {
        return task;
    }

    public long getUserID() {
        return userID;
    }

    public FeedDefinition getFeedDefinition() {
        return feedDefinition;
    }

    public void setFeedID(int feedID) {
        this.feedID = feedID;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }
}
