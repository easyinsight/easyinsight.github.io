package com.easyinsight.analysis {
import com.easyinsight.util.SmartComboBox;

import flash.events.Event;

import mx.collections.ArrayCollection;
import mx.controls.ComboBox;

public class ComboBoxReportFormItem extends ReportFormItem {

    private var choices:ArrayCollection;
    private var comboBox:SmartComboBox;
    private var labelField:String;
    private var dataField:String;

    public function ComboBoxReportFormItem(label:String, property:String, value:Object, report:Object,
            choices:Array, enabledProperty:String = null, section:String = null, explicitPosition:int = -1, labelField:String = null, dataField:String = null) {
        super(label, property, value, report, enabledProperty, false, section, explicitPosition);
        this.choices = new ArrayCollection(choices);
        this.labelField = labelField;
        this.dataField = dataField;
    }

    protected override function createChildren():void {
        super.createChildren();
        comboBox = new SmartComboBox();
        comboBox.addEventListener(Event.CHANGE, onChange);
        comboBox.dataProvider = choices;
        if (labelField != null) {
            comboBox.labelField = labelField;
        }
        if (dataField != null) {
            comboBox.selectedProperty = dataField;
            comboBox.selectedValue = this.value;
        } else {
            if (this.value != null) comboBox.selectedItem = this.value;
        }

        addChild(comboBox);
    }

    private function onChange(event:Event):void {
        if (dataField == null) {
            this.value = comboBox.selectedItem;
        } else {
            this.value = comboBox.selectedItem[dataField];
        }
        dispatchEvent(new Event('schemeChange'));
    }

    override protected function getValue():Object {
        if (dataField == null) {
            return comboBox.selectedItem;
        } else {
            return comboBox.selectedItem[dataField];
        }
    }
}
}