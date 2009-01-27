package com.easyinsight.datafeeds;

import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Sep 14, 2008
 * Time: 12:30:48 PM
 */
public class FeedHierarchyNode {
    private String name;
    private long feedID;
    private List<FeedHierarchyNode> children = new ArrayList<FeedHierarchyNode>();

    public List<FeedHierarchyNode> getChildren() {
        return children;
    }

    public void setChildren(List<FeedHierarchyNode> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }
}
