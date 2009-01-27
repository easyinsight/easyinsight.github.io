package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.users.Credentials;

/**
 * User: James Boe
 * Date: Jul 14, 2008
 * Time: 4:13:51 PM
 */
public class SalesforceCredentials extends Credentials {
    private String securityToken;

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }
}
