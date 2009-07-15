package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * User: James Boe
 * Date: Apr 27, 2008
 * Time: 9:34:20 PM
 */
public abstract class FeedNode {

    private List<FeedNode> children = new ArrayList<FeedNode>();

    public FeedNode() {
    }

    public List<FeedNode> getChildren() {
        return children;
    }

    public void setChildren(List<FeedNode> children) {
        this.children = children;
    }

    public abstract String toDisplay();

    public void sort() {
        Collections.sort(children, new Comparator<FeedNode>() {

                public int compare(FeedNode o1, FeedNode o2) {
                    if (o1 instanceof FolderNode && !(o2 instanceof FolderNode)) {
                        return -1;
                    } else if (!(o1 instanceof FolderNode) && o2 instanceof FolderNode) {
                        return 1;
                    }
                    return o1.toDisplay().compareTo(o2.toDisplay());
                }
            });
    }
}
