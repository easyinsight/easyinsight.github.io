package com.easyinsight.analysis {
import mx.controls.TextInput;

public class NumericReportFormItem extends ReportFormItem {

    private var minValue:int;
    private var maxValue:int;
    private var textInput:TextInput;

    public function NumericReportFormItem(label:String, property:String, value:Object, report:Object,
            minValue:int, maxValue:int) {
        super(label, property, value, report);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    protected override function createChildren():void {
        super.createChildren();
        textInput = new TextInput();
        if (this.value != null) textInput.text = String(this.value);
        addChild(textInput);
    }

    override protected function getValue():Object {
        return Number(textInput.text);
    }
}
}