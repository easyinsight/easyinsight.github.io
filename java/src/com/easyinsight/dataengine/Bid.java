package com.easyinsight.dataengine;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Aug 5, 2008
 * Time: 6:37:42 PM
 */
public class Bid implements Serializable {
    private int bidValue;
    private String destination;
    private EngineRequest engineRequest;
    private static final long serialVersionUID = -2089446082460548774L;

    public Bid() {
    }

    public Bid(int bidValue, String destination, EngineRequest engineRequest) {
        this.bidValue = bidValue;
        this.destination = destination;
        this.engineRequest = engineRequest;
    }

    public EngineRequest getEngineRequest() {
        return engineRequest;
    }

    public void setEngineRequest(EngineRequest engineRequest) {
        this.engineRequest = engineRequest;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getBidValue() {
        return bidValue;
    }

    public void setBidValue(int bidValue) {
        this.bidValue = bidValue;
    }
}
