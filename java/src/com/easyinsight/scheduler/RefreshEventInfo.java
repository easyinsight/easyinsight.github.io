package com.easyinsight.scheduler;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: May 22, 2009
 * Time: 5:12:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class RefreshEventInfo extends OutboundEvent {

    public static final int ADD = 1;
    public static final int COMPLETE = 2;
    public static final int ERROR = 3;

    private long taskId;
    private long feedId;
    private String feedName;
    private String message;
    private int action;

    public long getFeedId() {
        return feedId;
    }

    public void setFeedId(long feedId) {
        this.feedId = feedId;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
