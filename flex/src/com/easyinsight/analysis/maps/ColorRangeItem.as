package com.easyinsight.analysis.maps {


import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.containers.Box;
import mx.containers.HBox;
import mx.controls.Label;

public class ColorRangeItem extends HBox{

    private var colorBox:Box;
    private var text:Label;

    private var _valueString:String;
    private var _valueColor:uint;

    public function ColorRangeItem() {
        super();
    }

    [Bindable(event="valueColorChanged")]
    public function get valueColor():uint {
        return _valueColor;
    }

    public function set valueColor(value:uint):void {
        if (_valueColor == value) return;
        _valueColor = value;
        dispatchEvent(new Event("valueColorChanged"));
    }

    [Bindable(event="valueStringChanged")]
    public function get valueString():String {
        return _valueString;
    }

    public function set valueString(value:String):void {
        if (_valueString == value) return;
        _valueString = value;
        dispatchEvent(new Event("valueStringChanged"));
    }

    override protected function createChildren():void {
        super.createChildren();
        if (colorBox == null) {
            colorBox = new Box();
            colorBox.width = 32;
            colorBox.height = 16;
            colorBox.setStyle("backgroundAlpha", 1);
            colorBox.setStyle("backgroundColor", _valueColor);
        }
        addChild(colorBox);
        if (text == null) {
            text = new Label();
            BindingUtils.bindProperty(text, "text", this, "valueString");
        }
        addChild(text);
    }
}
}