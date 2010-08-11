package com.easyinsight.users;

/**
 * User: jamesboe
 * Date: Aug 11, 2010
 * Time: 11:49:04 AM
 */
public class OAuthResponse {

    public static final int BAD_HOST = 1;
    public static final int OTHER_OAUTH_PROBLEM = 2;

    private boolean successful;
    private int error;
    private String requestToken;

    public OAuthResponse(boolean successful, int error) {
        this.successful = successful;
        this.error = error;
    }

    public OAuthResponse(String requestToken, boolean successful) {
        this.requestToken = requestToken;
        this.successful = successful;
    }

    public OAuthResponse() {
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }
}
