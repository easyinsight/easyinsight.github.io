/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/8/11
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.schedule {
import com.easyinsight.util.SmartComboBox;

import flash.events.Event;

import mx.collections.ArrayCollection;

import mx.containers.HBox;

public class DeliveryFormatSelection extends HBox {



    private var comboBox:SmartComboBox;

    private var deliveryInfo:DeliveryInfo;

    public function DeliveryFormatSelection() {
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        comboBox = new SmartComboBox();
        comboBox.selectedProperty = "data";
        comboBox.addEventListener(Event.CHANGE, onChange);
    }

    private function onChange(event:Event):void {
        deliveryInfo.format = event.currentTarget.selectedItem.data;
    }

    override public function set data(val:Object):void {
        deliveryInfo = val as DeliveryInfo;
        if (deliveryInfo.type == DeliveryInfo.REPORT) {
            comboBox.dataProvider = new ArrayCollection([ {label: "Excel", data: 1},
                {label: "HTML Table", data: 4}]);
        } else if (deliveryInfo.type == DeliveryInfo.SCORECARD) {
            comboBox.dataProvider = new ArrayCollection([ {label: "HTML Table", data: 4}]);
        }
        if (deliveryInfo.format == 0) {
            deliveryInfo.format = comboBox.dataProvider.getItemAt(0).data;
        }
        comboBox.selectedValue = deliveryInfo.format;
    }

    override public function get data():Object {
        return deliveryInfo;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(comboBox);
    }
}
}
