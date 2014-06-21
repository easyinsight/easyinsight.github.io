package com.easyinsight.analysis {
import flash.events.Event;

import mx.controls.ColorPicker;

public class ColorReportFormItem extends ReportFormItem {

    public var colorPicker:ColorPicker;

    private var dependsOn:ComboBoxReportFormItem;

    public function ColorReportFormItem(label:String = null, property:String = null, value:Object = null, report:Object = null, enabledProperty:String = null,
            dependsOn:ComboBoxReportFormItem = null, section:String = null, explicitPosition:int = -1) {
        super(label, property, value, report, enabledProperty, false, section, explicitPosition);
        this.dependsOn = dependsOn;
    }

    protected override function createChildren():void {
        super.createChildren();
        colorPicker = new ColorPicker();
        colorPicker.addEventListener(Event.CHANGE, onChange);
        if (dependsOn != null) {
            dependsOn.addEventListener('schemeChange', onDependChange);
        }
        if (dependsOn != null && (dependsOn.value == "Primary" || dependsOn.value == "Secondary")) {
            this.enabled = false;
        }
        if (this.value != null) colorPicker.selectedColor = uint(this.value);
        addChild(colorPicker);
    }

    private function onDependChange(event:Event):void {
        if (dependsOn.value == "Primary" || dependsOn.value == "Secondary") {
            this.enabled = false;
        } else {
            this.enabled = true;
        }
    }

    private function onChange(event:Event):void {
        this.value = colorPicker.selectedColor;
    }

    override protected function getValue():Object {
        return colorPicker.selectedColor;
    }
}
}