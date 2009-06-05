package com.easyinsight.eventing;

import com.easyinsight.datafeeds.FeedDefinition;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 4, 2009
 * Time: 4:02:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class TodoCompletedEvent extends EIEvent {
    public static final String TODO_COMPLETED = "todoCompleted";
    private FeedDefinition feedDefinition;

    public TodoCompletedEvent() {
        super(TODO_COMPLETED);
    }

    public TodoCompletedEvent(FeedDefinition feedDefinition) {
        super(TODO_COMPLETED);
        this.feedDefinition = feedDefinition;
    }

    public FeedDefinition getFeedDefinition() {
        return feedDefinition;
    }

    public void setFeedDefinition(FeedDefinition feedDefinition) {
        this.feedDefinition = feedDefinition;
    }
}
