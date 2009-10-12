package com.easyinsight.outboundnotifications;

import com.easyinsight.scheduler.OutboundEvent;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 1, 2009
 * Time: 6:10:36 PM
 */
public class TodoEventInfo extends OutboundEvent {
    public static final int ADD = 1;
    public static final int COMPLETE = 2;
    public static final int ERROR = 3;

    private long todoID;
    private int action;

    public long getTodoID() {
        return todoID;
    }

    public void setTodoID(long todoID) {
        this.todoID = todoID;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

}