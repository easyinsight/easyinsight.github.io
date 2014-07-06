/**
 * Created by jamesboe on 7/3/14.
 */
package com.easyinsight.solutions {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.util.TextInputWithArrow;

import flash.events.Event;

import mx.collections.ArrayCollection;

import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.ComboBox;
import mx.events.DropdownEvent;

public class TargetFieldComboBox extends HBox {

    private var comboBox:ComboBox;
    private var _fields:ArrayCollection;

    private var fieldAssignment:FieldAssignment;

    public function TargetFieldComboBox() {
        percentWidth = 100;
        setStyle("horizontalAlign", "center");
        setStyle("horizontalScrollPolicy", "off");
        setStyle("verticalScrollPolicy", "off");
        comboBox = new ComboBox();
        comboBox.width = 320;
        comboBox.labelField = "display";
        comboBox.addEventListener(Event.CHANGE, onChange);
        comboBox.rowCount = 15;
    }


    public function set fields(value:ArrayCollection):void {
        _fields = value;
        comboBox.dataProvider = _fields;
    }

    override public function set data(val:Object):void {
        fieldAssignment = val as FieldAssignment;
        if (fieldAssignment.targetField != null) {
            comboBox.selectedItem = fieldAssignment.targetField;
        }
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(comboBox);
    }

    private function onChange(event:Event):void {
        fieldAssignment.targetField = comboBox.selectedItem as AnalysisItem;
    }
}
}
