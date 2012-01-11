package com.easyinsight.schedule {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.export.GeneralDelivery")]
public class GeneralDelivery extends ScheduledDelivery {

    public var subject:String;
    public var body:String;
    public var htmlEmail:Boolean;
    public var timezoneOffset:int;
    public var senderID:int;
    public var deliveryInfos:ArrayCollection;
    public var deliveryLabel:String;

    public function GeneralDelivery() {
        super();
    }


    override public function get activityDisplay():String {
        if (deliveryLabel != null && deliveryLabel != "") {
            return deliveryLabel;
        }
        return "Email multiple scorecards and reports";
    }
}
}