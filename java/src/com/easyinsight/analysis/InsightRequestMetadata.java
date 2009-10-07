package com.easyinsight.analysis;

import com.easyinsight.datafeeds.CredentialFulfillment;
import com.easyinsight.users.Credentials;
import com.easyinsight.PasswordStorage;
import com.easyinsight.security.SecurityUtil;

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
    private boolean noCache;
    private List<HierarchyOverride> hierarchyOverrides = new ArrayList<HierarchyOverride>();

    public List<HierarchyOverride> getHierarchyOverrides() {
        return hierarchyOverrides;
    }

    public void setHierarchyOverrides(List<HierarchyOverride> hierarchyOverrides) {
        this.hierarchyOverrides = hierarchyOverrides;
    }

    public boolean isNoCache() {
        return noCache;
    }

    public void setNoCache(boolean noCache) {
        this.noCache = noCache;
    }

    @Nullable
    public Credentials getCredentialForDataSource(long dataSourceID) {
        Credentials credentials = null;
        for (CredentialFulfillment credentialFulfillment : credentialFulfillmentList) {
            if (credentialFulfillment.getDataSourceID() == dataSourceID) {
                credentials = credentialFulfillment.getCredentials();
                if (credentials.isEncrypted()) {
                    credentials = decryptCredentials(credentials);
                }

            }
        }
        return credentials;
    }

    private Credentials decryptCredentials(Credentials creds) {
        Credentials c = new Credentials();
        String s = PasswordStorage.decryptString(creds.getUserName());
        int i = s.lastIndexOf(":" + SecurityUtil.getUserName());
        if(i == -1) {
            throw new RuntimeException();
        }
        c.setUserName(s.substring(0, i));
        s = PasswordStorage.decryptString(creds.getPassword());
        i = s.lastIndexOf(":" + SecurityUtil.getUserName());
        if(i == -1)
            throw new RuntimeException();
        c.setPassword(s.substring(0, i));
        return c;
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
