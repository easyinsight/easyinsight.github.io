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
        var type:String;
        switch (reportFormat) {
            case 1:
                type = " as Excel";
                break;
            case 2:
                type = " as PNG";
                break;
            case 3:
                type = " as PDF";
                break;
            case 4:
                type = " as Inline HTML Table";
                break;
        }
        return "Email " + reportName + type;
    }
}
}