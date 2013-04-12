package com.easyinsight.benchmark;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 4/11/13
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class ScheduledTaskBenchmarkInfo {
    private Date start, end;
    private String type, server;
    private long feedID, deliveryID;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public long getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(long deliveryID) {
        this.deliveryID = deliveryID;
    }
}
