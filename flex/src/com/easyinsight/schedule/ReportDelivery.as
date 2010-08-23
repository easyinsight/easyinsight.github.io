package com.easyinsight.schedule {
[Bindable]
[RemoteClass(alias="com.easyinsight.export.ReportDelivery")]
public class ReportDelivery extends ScheduledDelivery {

    public var reportFormat:int;
    public var reportID:int;
    public var reportName:String;
    public var subject:String;
    public var body:String;
    public var htmlEmail:Boolean;
    public var timezoneOffset:int;
    public var senderID:int;

    public function ReportDelivery() {
        super();
    }


    override public function get activityDisplay():String {
        return "Email " + reportName;
    }
}
}