package com.easyinsight.datafeeds;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jul 3, 2008
 * Time: 5:33:37 PM
 */
public class CredentialsDefinition implements Serializable {

    public static final int NO_CREDENTIALS = 0;
    public static final int STANDARD_USERNAME_PW = 1;
    public static final int SALESFORCE = 2;
    public static final int OAUTH = 3;

    private String credentialsName;    

    public CredentialsDefinition() {
    }

    public CredentialsDefinition(String credentialsName) {
        this.credentialsName = credentialsName;
    }

    public String getCredentialsName() {
        return credentialsName;
    }

    public void setCredentialsName(String credentialsName) {
        this.credentialsName = credentialsName;
    }
}
