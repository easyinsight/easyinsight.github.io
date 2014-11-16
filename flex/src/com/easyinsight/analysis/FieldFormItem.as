/**
 * Created by jamesboe on 5/8/14.
 */
package com.easyinsight.analysis {
import com.easyinsight.util.SmartComboBox;

import mx.collections.ArrayCollection;

public class FieldFormItem extends ReportFormItem {

    private var choices:ArrayCollection;
    private var comboBox:SmartComboBox;

    public function FieldFormItem(label:String, property:String, value:Object, report:Object,
                                           fields:ArrayCollection, qualifier:int = 0, enabledProperty:String = null) {
        super(label, property, value, report, enabledProperty);
        var copy:Array = [];
        for each (var item:AnalysisItemWrapper in fields) {
            if (qualifier == 0) {
                copy.push(item);
            } else {
                if (item.analysisItem.hasType(qualifier)) {
                    copy.push(item);
                }
            }
        }

        copy.sortOn("displayName", Array.CASEINSENSITIVE | Array.DESCENDING);

        var noOption:Object = { displayName: "[ No Selection ]"};
        copy.push(noOption);
        copy.reverse();
        this.choices = new ArrayCollection(copy);
    }

    protected override function createChildren():void {
        super.createChildren();
        comboBox = new SmartComboBox();
        comboBox.selectedProperty = "displayName";
        comboBox.labelField = "displayName";
        comboBox.dataProvider = choices;
        if (this.value != null) {
            comboBox.selectedValue = this.value.display;
        }
        addChild(comboBox);
    }

    override protected function getValue():Object {
        if (comboBox.selectedItem is AnalysisItemWrapper) {
            return AnalysisItemWrapper(comboBox.selectedItem).analysisItem;
        }
        return null;
    }
}
}
