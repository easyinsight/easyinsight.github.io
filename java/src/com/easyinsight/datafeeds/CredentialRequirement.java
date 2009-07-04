package com.easyinsight.datafeeds;

/**
 * User: James Boe
 * Date: Jun 15, 2009
 * Time: 11:22:15 AM
 */
public class CredentialRequirement {
    private long dataSourceID;
    private String dataSourceName;
    private int credentialsDefinition;

    public CredentialRequirement() {
    }

    public CredentialRequirement(long dataSourceID, String dataSourceName, int credentialsDefinition) {

        this.dataSourceID = dataSourceID;
        this.dataSourceName = dataSourceName;
        this.credentialsDefinition = credentialsDefinition;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public int getCredentialsDefinition() {
        return credentialsDefinition;
    }

    public void setCredentialsDefinition(int credentialsDefinition) {
        this.credentialsDefinition = credentialsDefinition;
    }
}
