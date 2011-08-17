/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/16/11
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import flash.events.Event;

import mx.containers.HBox;

import mx.controls.CheckBox;

public class FilterCheckbox extends HBox {

    private var _multiFilterOption:MultiFilterOption;

    private var checkbox:CheckBox;

    public function FilterCheckbox() {
        checkbox = new CheckBox();
        checkbox.addEventListener(Event.CHANGE, onChange);
        this.width = 140;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(checkbox);
    }

    override public function set data(val:Object):void {
        _multiFilterOption = val as MultiFilterOption;
        checkbox.label = _multiFilterOption.label;
        checkbox.selected = _multiFilterOption.selected;
    }

    private function onChange(event:Event):void {
        if (_multiFilterOption != null) {
            _multiFilterOption.selected = checkbox.selected;
        }
    }

    override public function get data():Object {
        return _multiFilterOption;
    }
}
}
