package com.easyinsight.outboundnotifications {
import flash.events.MouseEvent;
import flash.net.URLRequest;

[Bindable]
[RemoteClass(alias="com.easyinsight.outboundnotifications.BuyOurStuffInfo")]
public class BuyOurStuffInfo extends TodoEventInfo{
    public function BuyOurStuffInfo() {
        super();
    }

    override public function getTitle():String {
        return "Input your billing information to sign up!";
    }

    override public function onNavigateClick(event:MouseEvent):void {
        super.onNavigateClick(event);
        flash.net.navigateToURL(new URLRequest("billing/billing.jsp"), "_self");
    }
}
}