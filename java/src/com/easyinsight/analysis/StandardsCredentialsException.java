package com.easyinsight.analysis;

import com.easyinsight.datafeeds.CredentialRequirement;

/**
 * User: jamesboe
 * Date: Aug 30, 2009
 * Time: 12:24:41 PM
 */
public class StandardsCredentialsException extends DataAccessException {
    public StandardsCredentialsException(CredentialRequirement credentialRequirement) {
        super(credentialRequirement);
    }
}
