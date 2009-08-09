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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CredentialRequirement that = (CredentialRequirement) o;

        if (dataSourceID != that.dataSourceID) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (dataSourceID ^ (dataSourceID >>> 32));
        result = 31 * result + (dataSourceName != null ? dataSourceName.hashCode() : 0);
        result = 31 * result + credentialsDefinition;
        return result;
    }
}
