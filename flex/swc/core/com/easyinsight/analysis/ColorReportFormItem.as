package com.easyinsight.analysis {
import flash.events.Event;

import mx.controls.ColorPicker;

public class ColorReportFormItem extends ReportFormItem {

    public var colorPicker:ColorPicker;

    public function ColorReportFormItem(label:String = null, property:String = null, value:Object = null, report:Object = null, enabledProperty:String = null) {
        super(label, property, value, report, enabledProperty);
    }

    protected override function createChildren():void {
        super.createChildren();
        colorPicker = new ColorPicker();
        colorPicker.addEventListener(Event.CHANGE, onChange);
        if (this.value != null) colorPicker.selectedColor = uint(this.value);
        addChild(colorPicker);
    }

    private function onChange(event:Event):void {
        this.value = colorPicker.selectedColor;
    }

    override protected function getValue():Object {
        return colorPicker.selectedColor;
    }
}
}