package com.easyinsight.schedule {
[Bindable]
[RemoteClass(alias="com.easyinsight.export.ReportDelivery")]
public class ReportDelivery extends ScheduledDelivery {

    public var reportFormat:int;
    public var reportID:int;
    public var subject:String;
    public var body:String;

    public function ReportDelivery() {
        super();
    }
}
}