package com.easyinsight.solutions;

import com.easyinsight.core.EIDescriptor;

/**
 * User: James Boe
 * Date: Jan 11, 2009
 * Time: 11:44:05 PM
 */
public class SolutionInstallInfo {

    private long previousID;
    private EIDescriptor descriptor;
    private boolean requiresConfiguration;
    private String feedName;

    public SolutionInstallInfo(long previousID, EIDescriptor descriptor, boolean requiresConfiguration) {
        this.previousID = previousID;
        this.descriptor = descriptor;
        this.requiresConfiguration = requiresConfiguration;
    }

    public SolutionInstallInfo(long previousID, EIDescriptor descriptor, String feedName, boolean requiresConfiguration) {
        this(previousID, descriptor, requiresConfiguration);
        this.feedName = feedName;
    }

    public boolean isRequiresConfiguration() {
        return requiresConfiguration;
    }

    public void setRequiresConfiguration(boolean requiresConfiguration) {
        this.requiresConfiguration = requiresConfiguration;
    }

    public EIDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(EIDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public long getPreviousID() {
        return previousID;
    }

    public void setPreviousID(long previousID) {
        this.previousID = previousID;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }
}
