package com.easyinsight.datafeeds;

import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.core.InsightDescriptor;

import java.util.List;

/**
 * User: jboe
 * Date: Jan 5, 2008
 * Time: 11:54:00 AM
 */
public class FeedDescriptor {
    private String name;
    private long dataFeedID;
    private int role;
    private UploadPolicy policy;
    private long size;
    private int feedType;
    private List<InsightDescriptor> children;
    //private WSAnalysisDefinition definition;
    private String tagString;
    private String ownerName;
    private String description;
    private String attribution;

    public FeedDescriptor() {
    }

    public FeedDescriptor(String name, long dataFeedID,
                          UploadPolicy policy, long size, int feedType, Integer role, String ownerName, String description, String attribution) {
        this.name = name;
        this.dataFeedID = dataFeedID;
        this.policy = policy;
        this.size = size;
        this.feedType = feedType;
        this.role = role;
        this.ownerName = ownerName;
        this.description = description;
        this.attribution = attribution;
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

    /*public WSAnalysisDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(WSAnalysisDefinition definition) {
        this.definition = definition;
    }*/

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

    public UploadPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(UploadPolicy policy) {
        this.policy = policy;
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
}
