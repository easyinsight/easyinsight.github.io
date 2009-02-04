package com.easyinsight.api;

/**
 * User: James Boe
 * Date: Feb 1, 2009
 * Time: 2:09:14 PM
 */
public class DataSourceAPIDescriptor {
    private long feedID;
    private String name;
    private String apiKey;
    private boolean uncheckedEnabled;
    private boolean validatedEnabled;
    private DynamicServiceDescriptor dynamicServiceDescriptor;

    public DataSourceAPIDescriptor() {
    }

    public DataSourceAPIDescriptor(long feedID, String name, boolean uncheckedEnabled, boolean validatedEnabled,
                                   DynamicServiceDescriptor dynamicServiceDescriptor, String apiKey) {
        this.feedID = feedID;
        this.name = name;
        this.uncheckedEnabled = uncheckedEnabled;
        this.validatedEnabled = validatedEnabled;
        this.dynamicServiceDescriptor = dynamicServiceDescriptor;
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUncheckedEnabled() {
        return uncheckedEnabled;
    }

    public void setUncheckedEnabled(boolean uncheckedEnabled) {
        this.uncheckedEnabled = uncheckedEnabled;
    }

    public boolean isValidatedEnabled() {
        return validatedEnabled;
    }

    public void setValidatedEnabled(boolean validatedEnabled) {
        this.validatedEnabled = validatedEnabled;
    }

    public DynamicServiceDescriptor getDynamicServiceDescriptor() {
        return dynamicServiceDescriptor;
    }

    public void setDynamicServiceDescriptor(DynamicServiceDescriptor dynamicServiceDescriptor) {
        this.dynamicServiceDescriptor = dynamicServiceDescriptor;
    }
}
