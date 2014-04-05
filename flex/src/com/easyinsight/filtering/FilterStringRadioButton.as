/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/16/11
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import com.easyinsight.util.GridRadioButton;

import flash.events.Event;

public class FilterStringRadioButton extends GridRadioButton {


    public function FilterStringRadioButton() {
        super();
        this.width = 490;
        this.cbWidth = 490;
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
        addChild(radioButton);
    }

    [Bindable("dataChange")]
    override public function set data(val:Object):void {
        if(this.data)
            this.data.removeEventListener("selectedChanged", onSelectedChanged);
        super.data = val;
        if (val != null && radioButton != null) {
            this.data.addEventListener("selectedChanged", onSelectedChanged, false, 0, true);
            radioButton.label = val.label;
        }
    }

    private function onSelectedChanged(event:Event):void {
        this.radioButton.selected = isSelected();
    }
}
}
