package com.easyinsight.customupload.api {
import com.easyinsight.analysis.AnalysisItemWrapper;
import mx.events.DropdownEvent;
import mx.collections.ArrayCollection;
import mx.controls.ComboBox;
import com.easyinsight.analysis.AnalysisItem;
import mx.containers.HBox;
public class ParameterChoiceBox extends HBox {
    private var analysisItemWrapper:AnalysisItemWrapper;

    private var comboBox:ComboBox;

    [Bindable]
    private var availableItems:ArrayCollection;

    [Bindable]
    override public function set data(object:Object):void {
        this.analysisItemWrapper = object as AnalysisItemWrapper;
        for each (var item:AnalysisItem in availableItems) {
            if (item.analysisItemID == analysisItemWrapper.analysisItem.analysisItemID) {
                comboBox.selectedItem = item;
                break;
            }
        }
    }

    override public function get data():Object {
        return this.analysisItemWrapper;
    }

    public function ParameterChoiceBox(availableItems:ArrayCollection) {
        comboBox = new ComboBox();
        comboBox.labelField = "display";
        comboBox.dataProvider = availableItems;
        comboBox.addEventListener(DropdownEvent.CLOSE, onChange);
        this.availableItems = availableItems;
        addChild(comboBox);
    }

    private function onChange(event:DropdownEvent):void {
        analysisItemWrapper.analysisItem = comboBox.selectedItem as AnalysisItem;
    }
}
}