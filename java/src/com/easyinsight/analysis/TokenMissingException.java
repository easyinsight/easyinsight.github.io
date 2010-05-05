package com.easyinsight.analysis;

import com.easyinsight.datafeeds.CredentialRequirement;

/**
 * User: jamesboe
 * Date: Aug 30, 2009
 * Time: 12:24:57 PM
 */
public class TokenMissingException extends DataAccessException {
    public TokenMissingException(CredentialRequirement credentialRequirement) {
        super(credentialRequirement);
    }
}
