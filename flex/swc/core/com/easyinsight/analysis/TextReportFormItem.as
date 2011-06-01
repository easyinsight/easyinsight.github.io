package com.easyinsight.analysis {

import mx.controls.TextInput;

public class TextReportFormItem extends ReportFormItem {

    private var textInput:TextInput;

    public function TextReportFormItem(label:String, property:String, value:Object, report:Object,
            enabledProperty:String = null) {
        super(label, property, value, report, enabledProperty);
    }

    protected override function createChildren():void {
        super.createChildren();
        textInput = new TextInput();
        if (this.value != null) textInput.text = String(this.value);
        addChild(textInput);
    }

    override public function validate():Boolean {
        return true;
    }

    override protected function getValue():Object {
        return textInput.text;
    }
}
}