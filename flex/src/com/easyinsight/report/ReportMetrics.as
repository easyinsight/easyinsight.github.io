package com.easyinsight.report {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ReportMetrics")]
public class ReportMetrics {
    
    public var count:int;
    public var average:Number;
    public var myRating:int;

    public function ReportMetrics() {
    }
}
}