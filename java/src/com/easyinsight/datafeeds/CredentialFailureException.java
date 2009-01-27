package com.easyinsight.datafeeds;

/**
 * User: James Boe
 * Date: Jul 3, 2008
 * Time: 9:35:16 PM
 */
public class CredentialFailureException extends RuntimeException {
    private CredentialsDefinition credentialsDefinition;

    public CredentialFailureException(CredentialsDefinition credentialsDefinition) {
        this.credentialsDefinition = credentialsDefinition;
    }

    public CredentialsDefinition getCredentialsDefinition() {
        return credentialsDefinition;
    }

    public void setCredentialsDefinition(CredentialsDefinition credentialsDefinition) {
        this.credentialsDefinition = credentialsDefinition;
    }
}
