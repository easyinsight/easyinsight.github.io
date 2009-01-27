package com.easyinsight.dataengine;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Aug 5, 2008
 * Time: 10:08:38 PM
 */
public class BidRequest implements Serializable {
    private EngineRequest engineRequest;
    private static final long serialVersionUID = 25494553373635690L;

    public BidRequest() { }

    public BidRequest(EngineRequest engineRequest) {
        this.engineRequest = engineRequest;
    }

    public EngineRequest getEngineRequest() {
        return engineRequest;
    }

    public void setEngineRequest(EngineRequest engineRequest) {
        this.engineRequest = engineRequest;
    }
}
