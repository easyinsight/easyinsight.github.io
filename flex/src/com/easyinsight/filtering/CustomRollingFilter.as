package com.easyinsight.filtering {
import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.ComboBox;
import mx.controls.TextInput;

public class CustomRollingFilter extends HBox {

    private var _filter:RollingDateRangeFilterDefinition;

    

    public function CustomRollingFilter() {
        super();
    }

    public function set filter(value:RollingDateRangeFilterDefinition):void {
        _filter = value;
    }

    protected override function createChildren():void {
        super.createChildren();
        var beforeComboBox:ComboBox = new ComboBox();
        beforeComboBox.dataProvider = new ArrayCollection([ "Last", "Next" ]);
        BindingUtils.bindProperty(beforeComboBox, "selectedItem", this, "beforeItem");
        beforeComboBox.addEventListener(Event.CHANGE, onLastChange);
        addChild(beforeComboBox);
        var amountInput:TextInput = new TextInput();
        amountInput.width = 50;
        BindingUtils.bindProperty(amountInput, "text", this, "intervalAmountString");
        amountInput.addEventListener(Event.CHANGE, onAmountChange);
        addChild(amountInput);
        var amountTypeComboBox:ComboBox = new ComboBox();
        amountTypeComboBox.rowCount = 6;
        amountTypeComboBox.dataProvider = new ArrayCollection(["Minutes",
            "Hours", "Days", "Weeks", "Months", "Years"]);
        amountTypeComboBox.addEventListener(Event.CHANGE, onAmountTypeChange);
        BindingUtils.bindProperty(amountTypeComboBox, "selectedItem", this, "intervalType");
        addChild(amountTypeComboBox);
    }

    protected override function commitProperties():void {
        super.commitProperties();
        if (_filter.customBeforeOrAfter) {
            beforeItem = "Next";
        } else {
            beforeItem = "Last";
        }
        intervalAmountString = String(_filter.customIntervalAmount);
        if (_filter.customIntervalType == 0) {
            intervalType = "Minutes";
        } else if (_filter.customIntervalType == 1) {
            intervalType = "Hours";
        } else if (_filter.customIntervalType == 2) {
            intervalType = "Days";
        } else if (_filter.customIntervalType == 3) {
            intervalType = "Weeks";
        } else if (_filter.customIntervalType == 4) {
            intervalType = "Months";
        } else if (_filter.customIntervalType == 5) {
            intervalType = "Years";
        }
    }

    private var _beforeItem:String;

    private var _intervalAmountString:String;

    private var _intervalType:String;


    [Bindable(event="intervalAmountStringChanged")]
    public function get intervalAmountString():String {
        return _intervalAmountString;
    }

    public function set intervalAmountString(value:String):void {
        if (_intervalAmountString == value) return;
        _intervalAmountString = value;
        dispatchEvent(new Event("intervalAmountStringChanged"));
    }

    [Bindable(event="intervalTypeChanged")]
    public function get intervalType():String {
        return _intervalType;
    }

    public function set intervalType(value:String):void {
        if (_intervalType == value) return;
        _intervalType = value;
        dispatchEvent(new Event("intervalTypeChanged"));
    }

    [Bindable(event="beforeItemChanged")]
    public function get beforeItem():String {
        return _beforeItem;
    }

    public function set beforeItem(value:String):void {
        if (_beforeItem == value) return;
        _beforeItem = value;
        dispatchEvent(new Event("beforeItemChanged"));
    }

    private function changed():void {
        dispatchEvent(new Event("customRollingFilterEvent"));
    }

    private function onLastChange(event:Event):void {
        var comboBox:ComboBox = event.currentTarget as ComboBox;
        _filter.customBeforeOrAfter = comboBox.selectedItem != "Last";
        changed();
    }

    private function onAmountChange(event:Event):void {
        var input:TextInput = event.currentTarget as TextInput;
        if (input.text != "") {
            _filter.customIntervalAmount = int(input.text);
            changed();
        }
    }

    private function onAmountTypeChange(event:Event):void {
        var comboBox:ComboBox = event.currentTarget as ComboBox;
        _filter.customIntervalType = comboBox.selectedIndex;
        changed();
    }
}
}