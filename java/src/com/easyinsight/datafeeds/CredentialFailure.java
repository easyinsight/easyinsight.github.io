package com.easyinsight.datafeeds;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jul 4, 2008
 * Time: 12:03:56 PM
 */
public class CredentialFailure implements Serializable {
    private CredentialsDefinition credentialsDefinition;

    public CredentialsDefinition getCredentialsDefinition() {
        return credentialsDefinition;
    }

    public void setCredentialsDefinition(CredentialsDefinition credentialsDefinition) {
        this.credentialsDefinition = credentialsDefinition;
    }
}
