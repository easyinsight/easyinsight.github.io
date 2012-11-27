package com.easyinsight.scheduler;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 1, 2009
 * Time: 9:52:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class OutboundEvent implements Serializable {
    private long userId;
    private boolean broadcast = false;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }
}
