package com.easyinsight.analysis {
import com.easyinsight.solutions.InsightDescriptor;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.FeedNode")]
public class FeedNode {

    public var children:ArrayCollection;
    public var addonReportID:int;
    public var addonReportDescriptor:InsightDescriptor;

    public function FeedNode() {
    }

    public function get display():String {
        return null;
    }

    public function get unqualifiedDisplay():String {
        return null;
    }
}
}