package com.easyinsight.analysis {
import com.easyinsight.util.SmartComboBox;

import mx.collections.ArrayCollection;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.Operation;

public class ServerLoadComboBoxReportFormItem extends ReportFormItem {

    private var comboBox:SmartComboBox;
    private var operation:Operation;

    public function ServerLoadComboBoxReportFormItem(label:String, property:String, value:Object, report:Object,
                                                     operation:Operation, enabledProperty:String = null) {
        super(label, property, value, report, enabledProperty);
        this.operation = operation;
    }

    protected override function createChildren():void {
        super.createChildren();
        operation.addEventListener(ResultEvent.RESULT, gotData);
        operation.send();
    }

    private function gotData(event:ResultEvent):void {
        var choices:ArrayCollection = event.result as ArrayCollection;
        var none:Object = { name: "[ No Selection ]"};
        choices.addItemAt(none, 0);
        comboBox = new SmartComboBox();
        comboBox.selectedProperty = "id";
        if (this.value != null) {
            comboBox.selectedValue = this.value;
        }
        comboBox.labelField = "name";
        comboBox.dataProvider = choices;
        addChild(comboBox);
    }

    override protected function getValue():Object {
        return comboBox.selectedItem.id;
    }
}
}