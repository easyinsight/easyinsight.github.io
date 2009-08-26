package com.easyinsight.security;

/**
 * User: jamesboe
 * Date: Aug 25, 2009
 * Time: 4:56:53 PM
 */
public class AuthorizationRequirement {
    private int authorizationType;
    private String url;

    public AuthorizationRequirement() {
    }

    public AuthorizationRequirement(int authorizationType, String url) {
        this.authorizationType = authorizationType;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getAuthorizationType() {
        return authorizationType;
    }

    public void setAuthorizationType(int authorizationType) {
        this.authorizationType = authorizationType;
    }
}
