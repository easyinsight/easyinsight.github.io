package com.easyinsight.solutions;

import com.easyinsight.core.EIDescriptor;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.FeedDefinition;

import java.util.List;
import java.util.Map;

/**
 * User: James Boe
 * Date: Jan 11, 2009
 * Time: 11:44:05 PM
 */
public class SolutionInstallInfo {

    private long previousID;
    private EIDescriptor descriptor;
    private FeedDefinition newDataSource;
    private boolean requiresConfiguration;
    private String feedName;
    private Map<Key, Key> keyReplacementMap;
    private List<Long> reports;
    private List<Long> dashboards;
    private boolean makeVisible;

    /*public SolutionInstallInfo(long previousID, EIDescriptor descriptor, boolean requiresConfiguration) {
        this.previousID = previousID;
        this.descriptor = descriptor;
        this.requiresConfiguration = requiresConfiguration;
    }*/

    public SolutionInstallInfo(long previousID, EIDescriptor descriptor, String feedName, boolean requiresConfiguration, Map<Key, Key> keyReplacementMap, FeedDefinition newDataSource) {
        this.previousID = previousID;
        this.descriptor = descriptor;
        this.requiresConfiguration = requiresConfiguration;
        this.feedName = feedName;
        this.keyReplacementMap = keyReplacementMap;
        this.newDataSource = newDataSource;
    }

    public boolean isMakeVisible() {
        return makeVisible;
    }

    public void setMakeVisible(boolean makeVisible) {
        this.makeVisible = makeVisible;
    }

    public List<Long> getReports() {
        return reports;
    }

    public void setReports(List<Long> reports) {
        this.reports = reports;
    }

    public List<Long> getDashboards() {
        return dashboards;
    }

    public void setDashboards(List<Long> dashboards) {
        this.dashboards = dashboards;
    }

    public FeedDefinition getNewDataSource() {
        return newDataSource;
    }

    public void setNewDataSource(FeedDefinition newDataSource) {
        this.newDataSource = newDataSource;
    }

    public Map<Key, Key> getKeyReplacementMap() {
        return keyReplacementMap;
    }

    public void setKeyReplacementMap(Map<Key, Key> keyReplacementMap) {
        this.keyReplacementMap = keyReplacementMap;
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
