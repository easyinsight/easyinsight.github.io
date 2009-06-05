package com.easyinsight.eventing;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 4, 2009
 * Time: 2:48:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class EIEvent {
    public EIEvent() {
    }

    public EIEvent(String eventType) {
        this.eventType = eventType;
    }

    private String eventType;

    public String getEventType() {
        return eventType;
    }

    protected void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
