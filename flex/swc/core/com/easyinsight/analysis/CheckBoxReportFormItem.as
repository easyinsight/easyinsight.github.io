package com.easyinsight.analysis {
import mx.collections.ArrayCollection;
import mx.controls.CheckBox;


public class CheckBoxReportFormItem extends ReportFormItem {

    private var checkBox:CheckBox;

    public function CheckBoxReportFormItem(label:String, property:String, value:Object, report:Object) {
        super(label, property, value, report);
    }

    protected override function createChildren():void {
        super.createChildren();
        checkBox = new CheckBox();
        if (this.value != null) {
            checkBox.selected = Boolean(value);
        }
        addChild(checkBox);
    }

    override protected function getValue():Object {
        return checkBox.selected;
    }
}
}