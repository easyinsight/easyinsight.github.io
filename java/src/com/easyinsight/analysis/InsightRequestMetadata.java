package com.easyinsight.analysis;

import com.easyinsight.datafeeds.CredentialFulfillment;
import com.easyinsight.users.Credentials;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

import org.jetbrains.annotations.Nullable;

/**
 * User: James Boe
 * Date: Oct 28, 2008
 * Time: 11:22:35 AM
 */
public class InsightRequestMetadata implements Serializable {
    private Date now = new Date();
    private int utcOffset;
    private int version;
    private List<CredentialFulfillment> credentialFulfillmentList = new ArrayList<CredentialFulfillment>();
    private boolean refreshAllSources;

    @Nullable
    public Credentials getCredentialForDataSource(long dataSourceID) {
        Credentials credentials = null;
        for (CredentialFulfillment credentialFulfillment : credentialFulfillmentList) {
            if (credentialFulfillment.getDataSourceID() == dataSourceID) {
                credentials = credentialFulfillment.getCredentials();
            }
        }
        return credentials;
    }

    public boolean isRefreshAllSources() {
        return refreshAllSources;
    }

    public void setRefreshAllSources(boolean refreshAllSources) {
        this.refreshAllSources = refreshAllSources;
    }

    public int getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(int utcOffset) {
        this.utcOffset = utcOffset;
    }

    public List<CredentialFulfillment> getCredentialFulfillmentList() {
        return credentialFulfillmentList;
    }

    public void setCredentialFulfillmentList(List<CredentialFulfillment> credentialFulfillmentList) {
        this.credentialFulfillmentList = credentialFulfillmentList;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }
}
