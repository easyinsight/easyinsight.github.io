package com.easyinsight.datafeeds;

import java.util.ArrayList;
import java.util.List;

/**
 * User: James Boe
 * Date: Apr 27, 2008
 * Time: 9:33:57 PM
 */
public class FeedFolder extends FeedNode {

    private List<FeedNode> children = new ArrayList<FeedNode>();

    public FeedFolder() {
    }

    public FeedFolder(String name) {
        super(name);
    }

    public void addChild(FeedNode feedNode) {
        children.add(feedNode);
    }

    public List<FeedNode> getChildren() {
        return children;
    }

    public void setChildren(List<FeedNode> children) {
        this.children = children;
    }
}
