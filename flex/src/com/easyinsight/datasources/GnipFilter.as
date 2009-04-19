package com.easyinsight.datasources {

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.gnip.GnipFilter")]
public class GnipFilter {
    public var scope:String;
    public var publisherName:String;
    public var filterName:String;

    public function GnipFilter() {

    }
}
}