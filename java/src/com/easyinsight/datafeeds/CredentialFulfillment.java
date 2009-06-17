package com.easyinsight.datafeeds;

import com.easyinsight.users.Credentials;

/**
 * User: James Boe
 * Date: Jun 15, 2009
 * Time: 11:22:59 AM
 */
public class CredentialFulfillment {
    private long dataSourceID;
    private Credentials credentials;

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
