package com.easyinsight.datafeeds.cleardb;

import com.easyinsight.core.EIDescriptor;

/**
 * User: jamesboe
 * Date: 1/18/11
 * Time: 10:54 AM
 */
public class ClearDBResponse {
    private boolean successful;
    private EIDescriptor eiDescriptor;
    private String errorMessage;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public EIDescriptor getEiDescriptor() {
        return eiDescriptor;
    }

    public void setEiDescriptor(EIDescriptor eiDescriptor) {
        this.eiDescriptor = eiDescriptor;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
