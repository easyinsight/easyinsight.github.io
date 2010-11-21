package com.easyinsight.analysis {
import mx.controls.ColorPicker;

public class ColorReportFormItem extends ReportFormItem {

    private var colorPicker:ColorPicker;

    public function ColorReportFormItem(label:String, property:String, value:Object, report:Object) {
        super(label, property, value, report);
    }

    protected override function createChildren():void {
        super.createChildren();
        colorPicker = new ColorPicker();
        if (this.value != null) colorPicker.selectedColor = uint(this.value);
        addChild(colorPicker);
    }

    override protected function getValue():Object {
        return colorPicker.selectedColor;
    }
}
}