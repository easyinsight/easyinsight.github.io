/**
 * Created by jamesboe on 12/8/14.
 */
package com.easyinsight.customupload {
import flash.events.Event;

import mx.controls.CheckBox;

public class NetsuiteTableRenderer extends CheckBox {

    private var netsuiteTable:NetsuiteTable;

    public function NetsuiteTableRenderer() {
        addEventListener(Event.CHANGE, onChange);
    }

    override public function set data(val:Object):void {
        netsuiteTable = val as NetsuiteTable;
        this.selected = netsuiteTable.selected;
        this.label = netsuiteTable.name;
    }

    private function onChange(event:Event):void {
        netsuiteTable.selected = this.selected;
    }
}
}
