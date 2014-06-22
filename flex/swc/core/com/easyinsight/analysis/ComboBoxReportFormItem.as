package com.easyinsight.analysis {
import flash.events.Event;

import mx.collections.ArrayCollection;
import mx.controls.ComboBox;

public class ComboBoxReportFormItem extends ReportFormItem {

    private var choices:ArrayCollection;
    private var comboBox:ComboBox;

    public function ComboBoxReportFormItem(label:String, property:String, value:Object, report:Object,
            choices:Array, enabledProperty:String = null, section:String = null, explicitPosition:int = -1) {
        super(label, property, value, report, enabledProperty, false, section, explicitPosition);
        this.choices = new ArrayCollection(choices);
    }

    protected override function createChildren():void {
        super.createChildren();
        comboBox = new ComboBox();
        comboBox.addEventListener(Event.CHANGE, onChange);
        comboBox.dataProvider = choices;
        if (this.value != null) comboBox.selectedItem = this.value;
        addChild(comboBox);
    }

    private function onChange(event:Event):void {
        this.value = comboBox.selectedItem;
        dispatchEvent(new Event('schemeChange'));
    }

    override protected function getValue():Object {
        return comboBox.selectedItem;
    }
}
}