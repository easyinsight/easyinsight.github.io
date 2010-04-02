package com.easyinsight.report {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.MultiReportInfo")]
public class MultiReportInfo {

    public var dataSourceKey:String;
    public var reports:ArrayCollection;

    public function MultiReportInfo() {
    }
}
}