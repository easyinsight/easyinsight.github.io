package com.easyinsight.dataengine;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Aug 5, 2008
 * Time: 6:42:25 PM
 */
public class EngineResponse implements Serializable {
    private Long invocationID;
    private Object output;

    public EngineResponse() {
    }

    public EngineResponse(Long invocationID, Object output) {
        this.invocationID = invocationID;
        this.output = output;
    }

    public Long getInvocationID() {
        return invocationID;
    }

    public void setInvocationID(Long invocationID) {
        this.invocationID = invocationID;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }
}
