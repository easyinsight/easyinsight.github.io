/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/16/11
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import com.easyinsight.util.GridCheckbox;

import flash.events.Event;

public class FieldCheckbox extends GridCheckbox {


    public function FieldCheckbox() {
        super();
        this.width = 290;
        this.cbWidth = 290;
        this.moveY = 4;
    }


    override protected function isSelected():Boolean {
        return data.selected;
    }


    override protected function updateSelectedOnObject(selected:Boolean):void {
        data.selected = selected;
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(checkbox);
    }

    [Bindable("dataChange")]
    override public function set data(val:Object):void {
        if(this.data)
            this.data.removeEventListener("selectedChanged", onSelectedChanged);
        super.data = val;
        this.data.addEventListener("selectedChanged", onSelectedChanged, false, 0, true);
        checkbox.label = val.display;
    }

    private function onSelectedChanged(event:Event):void {
        this.checkbox.selected = isSelected();
    }
}
}
