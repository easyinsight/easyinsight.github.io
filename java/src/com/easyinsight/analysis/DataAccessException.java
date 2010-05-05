package com.easyinsight.analysis;

import com.easyinsight.datafeeds.CredentialRequirement;

/**
 * User: jamesboe
 * Date: Aug 30, 2009
 * Time: 12:24:16 PM
 */
public class DataAccessException extends RuntimeException {
    private CredentialRequirement credentialRequirement;

    public DataAccessException(CredentialRequirement credentialRequirement) {
        this.credentialRequirement = credentialRequirement;
    }

    public CredentialRequirement getCredentialRequirement() {
        return credentialRequirement;
    }
}
