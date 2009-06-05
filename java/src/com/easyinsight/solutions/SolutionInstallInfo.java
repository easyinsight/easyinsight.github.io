package com.easyinsight.solutions;

import com.easyinsight.notifications.ConfigureDataFeedTodo;

/**
 * User: James Boe
 * Date: Jan 11, 2009
 * Time: 11:44:05 PM
 */
public class SolutionInstallInfo {

    public static final int DATA_SOURCE = 1;
    public static final int INSIGHT = 2;

    private long previousID;
    private long newID;
    private int type;
    private ConfigureDataFeedTodo todoItem;
    private String feedName;

    public SolutionInstallInfo(long previousID, long newID, int type, ConfigureDataFeedTodo todoItem) {
        this.previousID = previousID;
        this.newID = newID;
        this.type = type;
        this.todoItem = todoItem;
    }

    public SolutionInstallInfo(long previousID, long newID, int type, ConfigureDataFeedTodo todoItem, String feedName) {
        this.previousID = previousID;
        this.newID = newID;
        this.type = type;
        this.todoItem = todoItem;
        this.feedName = feedName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getPreviousID() {
        return previousID;
    }

    public void setPreviousID(long previousID) {
        this.previousID = previousID;
    }

    public long getNewID() {
        return newID;
    }

    public void setNewID(long newID) {
        this.newID = newID;
    }

    public ConfigureDataFeedTodo getTodoItem() {
        return todoItem;
    }

    public void setTodoItem(ConfigureDataFeedTodo todoItem) {
        this.todoItem = todoItem;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }
}
