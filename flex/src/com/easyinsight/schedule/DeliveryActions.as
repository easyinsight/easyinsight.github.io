/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/8/11
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.schedule {
import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;

public class DeliveryActions extends HBox {

    [Embed(source="../../../../assets/navigate_cross.png")]
    private var deleteIcon:Class;

    private var deleteButton:Button;

    private var deliveryInfo:DeliveryInfo;

    public function DeliveryActions() {
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        deleteButton = new Button();
        deleteButton.setStyle("icon", deleteIcon);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
    }

    override public function set data(val:Object):void {
        deliveryInfo = val as DeliveryInfo;
    }

    override public function get data():Object {
        return deliveryInfo;
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new DeliveryInfoEvent(DeliveryInfoEvent.REMOVE, deliveryInfo));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(deleteButton);
    }
}
}
