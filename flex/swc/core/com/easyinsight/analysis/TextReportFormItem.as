package com.easyinsight.analysis {

import mx.controls.TextInput;

public class TextReportFormItem extends ReportFormItem {

    private var textInput:TextInput;

    public function TextReportFormItem(label:String, property:String, value:Object, report:Object,
            enabledProperty:String = null, section:String = null, explicitPosition:int = -1) {
        super(label, property, value, report, enabledProperty, null, section, explicitPosition);
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