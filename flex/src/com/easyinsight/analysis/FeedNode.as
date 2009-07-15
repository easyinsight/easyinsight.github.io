package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.FeedNode")]
public class FeedNode {

    public var children:ArrayCollection;

    public function FeedNode() {
    }

    public function get display():String {
        return null;
    }

    private function blah():void {
        var folderNode:FolderNode;
        var feedFolder:FeedFolder;
    }
}
}