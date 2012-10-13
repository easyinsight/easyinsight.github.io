package com.easyinsight.schedule {
[Bindable]
[RemoteClass(alias="com.easyinsight.export.ScorecardDelivery")]
public class ScorecardDelivery extends ScheduledDelivery {

    public var deliveryFormat:int;
    public var scorecardID:int;
    public var scorecardName:String;
    public var subject:String;
    public var body:String;
    public var htmlEmail:Boolean;
    public var timezoneOffset:int;
    public var senderID:int;

    public function ScorecardDelivery() {
        super();
    }

    override public function get activityDisplay():String {
        return describe;
    }

    override public function get describe():String {
        var type:String;
        switch (deliveryFormat) {
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
        return "Email " + scorecardName + type;
    }
}
}