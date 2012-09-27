/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 1/8/11
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.google {
import flash.events.Event;

import mx.containers.HBox;
import mx.controls.CheckBox;
import mx.controls.RadioButton;
import mx.controls.RadioButtonGroup;

public class QuickbaseApplicationSelector extends HBox {

    private var _checkbox:RadioButton;
    private var app:QuickbaseApplication;

    public function QuickbaseApplicationSelector() {
        _checkbox = new RadioButton();
        _checkbox.addEventListener(Event.CHANGE, onChange);
    }

    public function get group():RadioButtonGroup {
        return _checkbox.group;
    }

    public function set group(value:RadioButtonGroup):void {
        _checkbox.group = value;
    }

    public function get checkbox():RadioButton {
        return _checkbox;
    }

    public function set checkbox(value:RadioButton):void {
        _checkbox = value;
    }

    private function onChange(event:Event):void {
        app.selected = _checkbox.selected;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(_checkbox);
    }

    override public function set data(val:Object):void {
        app = val as QuickbaseApplication;
        if (app != null) {
            _checkbox.label = app.name;
        }
    }

    override public function get data():Object {
        return app;
    }
}
}
