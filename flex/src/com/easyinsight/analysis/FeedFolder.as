package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.FeedFolder")]
public class FeedFolder {
    public var folderID:int;
    public var name:String;
    public var childFolders:ArrayCollection;
    public var childItems:ArrayCollection;

    public function FeedFolder() {
    }
}
}