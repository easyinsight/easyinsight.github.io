package com.easyinsight.analysis.range {
import com.easyinsight.skin.ImageConstants;

import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;

public class RangeControls extends HBox {



    private var deleteButton:Button;

    private var rangeOption:RangeOption;

    public function RangeControls() {
        super();
        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.addEventListener(RangeEvent.DELETE_RANGE, onDelete);
    }

    private function onDelete(event:MouseEvent):void {
        dispatchEvent(new RangeEvent(RangeEvent.DELETE_RANGE, rangeOption));
    }

    protected override function createChildren():void {
        super.createChildren();
        addChild(deleteButton);
    }

    override public function set data(val:Object):void {
        rangeOption = val as RangeOption;
    }

    override public function get data():Object {
        return rangeOption;
    }
}
}