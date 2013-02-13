/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/16/11
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import flash.events.Event;

import mx.binding.utils.BindingUtils;

import mx.containers.HBox;
import mx.controls.Alert;

import mx.controls.CheckBox;

public class FilterStringCheckbox extends HBox {

    private var curData:Object;

    private var checkbox:CheckBox;

    public function FilterStringCheckbox() {
        checkbox = new CheckBox();
        checkbox.addEventListener(Event.CHANGE, onChange);
        this.width = 140;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(checkbox);
    }

    override public function set data(val:Object):void {
        curData = val;
        checkbox.label = val.label;
        BindingUtils.bindProperty(checkbox, "selected", curData, "selected");
        BindingUtils.bindProperty(curData, "selected", checkbox, "selected");
    }

    private function onChange(event:Event):void {
//        if (_multiFilterOption != null) {
//            dispatchEvent(new MultiFilterEvent(_multiFilterOption));
//        }
    }

    override public function get data():Object {
        return curData;
    }
}
}
