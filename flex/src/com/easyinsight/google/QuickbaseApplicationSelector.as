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

public class QuickbaseApplicationSelector extends HBox {

    private var checkbox:CheckBox;
    private var app:QuickbaseApplication;

    public function QuickbaseApplicationSelector() {
        checkbox = new CheckBox();
        checkbox.addEventListener(Event.CHANGE, onChange);
    }

    private function onChange(event:Event):void {
        app.selected = checkbox.selected;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(checkbox);
    }

    override public function set data(val:Object):void {
        app = val as QuickbaseApplication;
        if (app != null) {
            checkbox.label = app.name;
        }
    }

    override public function get data():Object {
        return app;
    }
}
}
