package com.easyinsight.datafeeds;

/**
 * Created by IntelliJ IDEA.
 * User: jboe
 * Date: Jul 14, 2009
 * Time: 1:59:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class FolderNode extends FeedNode {
    private FeedFolder folder;

    public FeedFolder getFolder() {
        return folder;
    }

    public void setFolder(FeedFolder folder) {
        this.folder = folder;
    }

    @Override
    public String toDisplay() {
        return folder.getName();
    }
}
