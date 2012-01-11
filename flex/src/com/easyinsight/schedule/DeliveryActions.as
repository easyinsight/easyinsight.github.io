/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/8/11
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.schedule {
import com.easyinsight.skin.ImageConstants;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;

public class DeliveryActions extends HBox {


    private var editButton:Button;
    private var deleteButton:Button;

    private var deliveryInfo:DeliveryInfo;

    public function DeliveryActions() {
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        editButton = new Button();
        editButton.setStyle("icon", ImageConstants.EDIT_ICON);
        editButton.addEventListener(MouseEvent.CLICK, onEdit);
        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
    }

    private function onEdit(event:MouseEvent):void {
        dispatchEvent(new DeliveryInfoEvent(DeliveryInfoEvent.EDIT, deliveryInfo));
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
        addChild(editButton);
        addChild(deleteButton);
    }
}
}
