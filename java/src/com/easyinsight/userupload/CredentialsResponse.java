package com.easyinsight.userupload;

import com.easyinsight.users.Credentials;

/**
 * User: James Boe
 * Date: Jul 14, 2008
 * Time: 4:22:07 PM
 */
public class CredentialsResponse {
    private boolean successful;
    private String failureMessage;
    private Credentials encryptedResponse;

    public CredentialsResponse() {
    }

    public CredentialsResponse(boolean successful) {
        this.successful = successful;
    }

    public CredentialsResponse(boolean successful, String failureMessage) {
        this.successful = successful;
        this.failureMessage = failureMessage;
    }

    public Credentials getEncryptedResponse() {
        return encryptedResponse;
    }

    public void setEncryptedResponse(Credentials encryptedResponse) {
        this.encryptedResponse = encryptedResponse;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }
}
