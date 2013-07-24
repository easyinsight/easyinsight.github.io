/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/1/13
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import flash.events.Event;

import mx.containers.HBox;
import mx.controls.CheckBox;

public class DashboardFilterOverrideCheckbox extends HBox {

    private var checkbox:CheckBox;
    private var filterOverride:DashboardFilterOverrideState;

    public function DashboardFilterOverrideCheckbox() {
        checkbox = new CheckBox();
        checkbox.addEventListener(Event.CHANGE, onChange);
        setStyle("horizontalAlign", "center");
        percentWidth = 100;
    }

    private function onChange(event:Event):void {
        filterOverride.selected = event.currentTarget.selected;
    }

    override public function set data(val:Object):void {
        filterOverride = val as DashboardFilterOverrideState;
        checkbox.selected = filterOverride.selected;
    }

    override public function get data():Object {
        return filterOverride;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(checkbox);
    }
}
}
