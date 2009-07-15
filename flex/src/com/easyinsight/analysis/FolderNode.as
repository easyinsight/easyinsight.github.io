package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.FolderNode")]
public class FolderNode extends FeedNode{

    public var folder:FeedFolder;

    public function FolderNode() {
        super();
    }

    override public function get display():String {
        return folder.name;
    }
}
}