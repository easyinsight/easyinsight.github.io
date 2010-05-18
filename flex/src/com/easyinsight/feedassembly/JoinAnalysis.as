package com.easyinsight.feedassembly {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.JoinAnalysis")]
public class JoinAnalysis {

    public var unjoinedSourceValues:ArrayCollection;
    public var sourceDataSourceName:String;
    public var unjoinedTargetValues:ArrayCollection;
    public var targetDataSourceName:String;
    public var joinedValues:ArrayCollection;

    public function JoinAnalysis() {
    }
}
}