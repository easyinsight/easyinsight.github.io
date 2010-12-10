package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Apr 27, 2008
 * Time: 9:33:57 PM
 */
public class FeedFolder implements Cloneable, Serializable {

    private long folderID;
    private String name;
    private List<FeedFolder> childFolders = new ArrayList<FeedFolder>();
    private List<AnalysisItem> childItems = new ArrayList<AnalysisItem>();

    public FeedFolder() {
    }

    public FeedFolder clone() throws CloneNotSupportedException {
        FeedFolder clone = (FeedFolder) super.clone();
        clone.setFolderID(0);
        List<FeedFolder> clonedChildFolders = new ArrayList<FeedFolder>();
        for (FeedFolder childFolder : childFolders) {
            clonedChildFolders.add(childFolder.clone());
        }
        setChildFolders(clonedChildFolders);
        return clone;
    }

    public void updateIDs(Map<Long, AnalysisItem> replacementMap) {
        List<AnalysisItem> clonedChildren = new ArrayList<AnalysisItem>();
        for (AnalysisItem item : getChildItems()) {
            AnalysisItem clony = replacementMap.remove(item.getAnalysisItemID());
            clonedChildren.add(clony);
        }
        for (FeedFolder childFolder : childFolders) {
            childFolder.updateIDs(replacementMap);
        }
        setChildItems(clonedChildren);
    }

    public void addAnalysisItem(AnalysisItem analysisItem) {
        childItems.add(analysisItem);
    }

    public long getFolderID() {
        return folderID;
    }

    public void setFolderID(long folderID) {
        this.folderID = folderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FeedFolder> getChildFolders() {
        return childFolders;
    }

    public void setChildFolders(List<FeedFolder> childFolders) {
        this.childFolders = childFolders;
    }

    public List<AnalysisItem> getChildItems() {
        return childItems;
    }

    public void setChildItems(List<AnalysisItem> childItems) {
        this.childItems = childItems;
    }

    public FeedNode toFeedNode() {
        FolderNode folderNode = new FolderNode();
        folderNode.setFolder(this);
        for (FeedFolder childFolder : childFolders) {
            folderNode.getChildren().add(childFolder.toFeedNode());
        }
        for (AnalysisItem analysisItem : getChildItems()) {
            if (!analysisItem.isHidden()) {
                folderNode.getChildren().add(analysisItem.toFeedNode());
            }
        }
        folderNode.sort();
        return folderNode;
    }

    @Nullable
    public FeedFolder getFolderByName(String name) {
        for (FeedFolder child : getChildFolders()) {
            if (name.equals(child.getName())) {
                return child;
            }
        }
        return null;
    }

    public AnalysisItem getAnalysisItem(String name) {
        for (AnalysisItem node : getChildItems()) {
            if (node.getKey().toKeyString().equals(name)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
