package com.easyinsight.util;

import java.io.Serializable;

/**
* User: jamesboe
* Date: 1/3/11
* Time: 9:10 AM
*/
public class CallData implements Serializable {
    private String statusMessage;
    private int status = ServiceUtil.RUNNING;
    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
