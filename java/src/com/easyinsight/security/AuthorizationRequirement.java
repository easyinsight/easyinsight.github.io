package com.easyinsight.security;

/**
 * User: jamesboe
 * Date: Aug 25, 2009
 * Time: 4:56:53 PM
 */
public class AuthorizationRequirement {
    private int authorizationType;

    public AuthorizationRequirement() {
    }

    public AuthorizationRequirement(int authorizationType) {
        this.authorizationType = authorizationType;
    }

    public int getAuthorizationType() {
        return authorizationType;
    }

    public void setAuthorizationType(int authorizationType) {
        this.authorizationType = authorizationType;
    }
}
