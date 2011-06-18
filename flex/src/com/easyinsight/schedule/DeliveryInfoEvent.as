/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/8/11
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.schedule {
import flash.events.Event;

public class DeliveryInfoEvent extends Event {

    public static const ADD_REPORT:String = "addReport";
    public static const ADD_SCORECARD:String = "addScorecard";
    public static const REMOVE:String = "removeDeliveryInfo";

    public var deliveryInfo:DeliveryInfo;

    public function DeliveryInfoEvent(type:String, deliveryInfo:DeliveryInfo) {
        super(type,  true);
        this.deliveryInfo = deliveryInfo;
    }

    override public function clone():Event {
        return new DeliveryInfoEvent(type, deliveryInfo);
    }
}
}
