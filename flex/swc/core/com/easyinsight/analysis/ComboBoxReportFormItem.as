package com.easyinsight.analysis {
import mx.collections.ArrayCollection;
import mx.controls.ComboBox;

public class ComboBoxReportFormItem extends ReportFormItem {

    private var choices:ArrayCollection;
    private var comboBox:ComboBox;

    public function ComboBoxReportFormItem(label:String, property:String, value:Object, report:Object,
            choices:Array, enabledProperty:String = null) {
        super(label, property, value, report, enabledProperty);
        this.choices = new ArrayCollection(choices);
    }

    protected override function createChildren():void {
        super.createChildren();
        comboBox = new ComboBox();
        comboBox.dataProvider = choices;
        if (this.value != null) comboBox.selectedItem = this.value;
        addChild(comboBox);
    }

    override protected function getValue():Object {
        return comboBox.selectedItem;
    }
}
}