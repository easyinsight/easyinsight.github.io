package com.easyinsight.outboundnotifications;

import com.easyinsight.scheduler.OutboundEvent;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 15, 2009
 * Time: 10:22:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class BroadcastInfo extends OutboundEvent {

    private String message;

    public BroadcastInfo() {
        setBroadcast(true);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
