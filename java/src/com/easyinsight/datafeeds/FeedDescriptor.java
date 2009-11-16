package com.easyinsight.datafeeds;

import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.core.InsightDescriptor;

import java.util.List;
import java.util.Date;

/**
 * User: jboe
 * Date: Jan 5, 2008
 * Time: 11:54:00 AM
 */
public class FeedDescriptor {
    private String name;
    private long dataFeedID;
    private int role;
    private long groupSourceID;
    //private UploadPolicy policy;
    private long size;
    private int feedType;
    private List<InsightDescriptor> children;
    private String tagString;
    private String ownerName;
    private String description;
    private String attribution;
    private Date lastDataTime;
    private boolean solutionTemplate;

    private boolean hasSavedCredentials;

    public FeedDescriptor() {
    }

    public FeedDescriptor(String name, long dataFeedID, long size, int feedType, Integer role, String ownerName, String description, String attribution,
                          Date lastDataTime) {
        this.name = name;
        this.dataFeedID = dataFeedID;
        this.size = size;
        this.feedType = feedType;
        this.role = role;
        this.ownerName = ownerName;
        this.description = description;
        this.attribution = attribution;
        this.lastDataTime = lastDataTime;
    }

    public long getGroupSourceID() {
        return groupSourceID;
    }

    public void setGroupSourceID(long groupSourceID) {
        this.groupSourceID = groupSourceID;
    }

    public boolean isSolutionTemplate() {
        return solutionTemplate;
    }

    public void setSolutionTemplate(boolean solutionTemplate) {
        this.solutionTemplate = solutionTemplate;
    }

    public Date getLastDataTime() {
        return lastDataTime;
    }

    public void setLastDataTime(Date lastDataTime) {
        this.lastDataTime = lastDataTime;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public List<InsightDescriptor> getChildren() {
        return children;
    }

    public void setChildren(List<InsightDescriptor> children) {
        this.children = children;
    }

    public long getSize() {
        return size;
    }

    public int getFeedType() {
        return feedType;
    }

    public void setFeedType(int feedType) {
        this.feedType = feedType;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }

    /*public boolean isSubscribable() {
        return subscribable;
    }

    public void setSubscribable(boolean subscribable) {
        this.subscribable = subscribable;
    }

    public boolean isAnalyzeable() {
        return analyzeable;
    }

    public void setAnalyzeable(boolean analyzeable) {
        this.analyzeable = analyzeable;
    }*/

    public boolean isHasSavedCredentials() {
        return hasSavedCredentials;
    }

    public void setHasSavedCredentials(boolean hasSavedCredentials) {
        this.hasSavedCredentials = hasSavedCredentials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedDescriptor that = (FeedDescriptor) o;

        if (dataFeedID != that.dataFeedID) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (dataFeedID ^ (dataFeedID >>> 32));
    }
}
