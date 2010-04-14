package com.easyinsight.filtering
{
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisItemWrapper;
import com.easyinsight.util.PersonaItemComboBox;
import com.easyinsight.util.SmartComboBox;

import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.containers.ViewStack;

import mx.controls.ComboBox;

import mx.controls.Label;

public class DateRangeDetailEditor extends VBox implements IFilterDetailEditor
{
    private var choiceBox:ComboBox;
    private var leftComboBox:ComboBox;
    private var rightComboBox:ComboBox;
    private var lowerDateFieldsBox:SmartComboBox;
    private var upperDateFieldsBox:SmartComboBox;
    private var choices:ArrayCollection;
    private var _lowerStackIndex:int;
    private var _upperStackIndex:int;
    private var _lowerDateItemDisplay:String;
    private var _upperDateItemDisplay:String;
    private var _settingsStackIndex:int;


    [Bindable(event="settingsStackIndexChanged")]
    public function get settingsStackIndex():int {
        return _settingsStackIndex;
    }

    public function set settingsStackIndex(value:int):void {
        if (_settingsStackIndex == value) return;
        _settingsStackIndex = value;
        dispatchEvent(new Event("settingsStackIndexChanged"));
    }

    [Bindable(event="lowerStackIndexChanged")]
    public function get lowerStackIndex():int {
        return _lowerStackIndex;
    }

    public function set lowerStackIndex(value:int):void {
        if (_lowerStackIndex == value) return;
        _lowerStackIndex = value;
        dispatchEvent(new Event("lowerStackIndexChanged"));
    }

    [Bindable(event="upperStackIndexChanged")]
    public function get upperStackIndex():int {
        return _upperStackIndex;
    }

    public function set upperStackIndex(value:int):void {
        if (_upperStackIndex == value) return;
        _upperStackIndex = value;
        dispatchEvent(new Event("upperStackIndexChanged"));
    }

    [Bindable(event="lowerDateItemDisplayChanged")]
    public function get lowerDateItemDisplay():String {
        return _lowerDateItemDisplay;
    }

    public function set lowerDateItemDisplay(value:String):void {
        if (_lowerDateItemDisplay == value) return;
        _lowerDateItemDisplay = value;
        dispatchEvent(new Event("lowerDateItemDisplayChanged"));
    }

    [Bindable(event="upperDateItemDisplayChanged")]
    public function get upperDateItemDisplay():String {
        return _upperDateItemDisplay;
    }

    public function set upperDateItemDisplay(value:String):void {
        if (_upperDateItemDisplay == value) return;
        _upperDateItemDisplay = value;
        dispatchEvent(new Event("upperDateItemDisplayChanged"));
    }

    private var _filterDefinition:FilterDefinition;

    public function DateRangeDetailEditor()
    {
        super();
        choices = new ArrayCollection();
        choices.addItem("Fixed Date Range");
        choices.addItem("Relative Date Range");
    }

    private var _fields:ArrayCollection;

    public function set fields(value:ArrayCollection):void {
        if (value != null) {
            var fields:ArrayCollection = new ArrayCollection();
            for each (var item:AnalysisItemWrapper in value) {
                if (item.analysisItem.hasType(AnalysisItemTypes.DATE)) {
                    fields.addItem(item);
                }
            }
            _fields = fields;
        }
    }

    override protected function createChildren():void {
        super.createChildren();
        if (choiceBox == null) {
            choiceBox = new ComboBox();
            choiceBox.dataProvider = choices;
            choiceBox.selectedItem = _filterDefinition is FilterDateRangeDefinition ? "Fixed Date Range" : "Relative Date Range";
            settingsStackIndex = _filterDefinition is FilterDateRangeDefinition ? 1 : 0;
        }
        addChild(choiceBox);
        var settingsStack:ViewStack = new ViewStack();
        settingsStack.resizeToContent = true;
        BindingUtils.bindProperty(settingsStack, "selectedIndex", this, "settingsStackIndex");
        var noBox:Box = new Box();
        settingsStack.addChild(noBox);
        var hbox:HBox = new HBox();
        var lowerBoundBox:VBox = new VBox();
        var lowerBoundLabel:Label = new Label();
        lowerBoundLabel.text = "Lower Bound Settings";
        lowerBoundBox.addChild(lowerBoundLabel);
        // options are, what
        var leftStack:ViewStack = new ViewStack();
        leftStack.resizeToContent = true;
        leftComboBox = new ComboBox();
        leftComboBox.dataProvider = new ArrayCollection(["( None )", "Based on Field"]);
        BindingUtils.bindProperty(leftComboBox, "selectedIndex", this, "lowerStackIndex");
        BindingUtils.bindProperty(leftStack, "selectedIndex", leftComboBox, "selectedIndex");
        lowerBoundBox.addChild(leftComboBox);
        var noneBox:Box = new Box();
        leftStack.addChild(noneBox);
        var fieldBox:Box = new Box();
        leftStack.addChild(fieldBox);
        lowerDateFieldsBox = new SmartComboBox();
        lowerDateFieldsBox.dataProvider = _fields;
        lowerDateFieldsBox.selectedProperty = "displayName";
        BindingUtils.bindProperty( lowerDateFieldsBox, "selectedValue", this, "lowerDateItemDisplay");
        lowerDateFieldsBox.labelField = "displayName";
        fieldBox.addChild(lowerDateFieldsBox);
        lowerBoundBox.addChild(leftStack);
        hbox.addChild(lowerBoundBox);
        var rightBoundBox:VBox = new VBox();
        var rightBoundLabel:Label = new Label();
        rightBoundLabel.text = "Upper Bound Settings";
        rightBoundBox.addChild(rightBoundLabel);
        var rightStack:ViewStack = new ViewStack();
        rightStack.resizeToContent = true;
        rightComboBox = new ComboBox();
        rightComboBox.dataProvider = new ArrayCollection(["( None )", "Based on Field"]);
        BindingUtils.bindProperty(rightComboBox, "selectedIndex", this, "upperStackIndex");
        BindingUtils.bindProperty(rightStack, "selectedIndex", rightComboBox, "selectedIndex");
        rightBoundBox.addChild(rightComboBox);
        var rightNoneBox:Box = new Box();
        rightStack.addChild(rightNoneBox);
        var rightFieldBox:Box = new Box();
        rightStack.addChild(rightFieldBox);
        upperDateFieldsBox = new SmartComboBox();
        upperDateFieldsBox.dataProvider = _fields;
        upperDateFieldsBox.selectedProperty = "displayName";
        upperDateFieldsBox.labelField = "displayName";
        BindingUtils.bindProperty(upperDateFieldsBox, "selectedValue", this, "upperDateItemDisplay");
        rightFieldBox.addChild(upperDateFieldsBox);
        rightBoundBox.addChild(rightStack);
        hbox.addChild(rightBoundBox);
        settingsStack.addChild(hbox);
        addChild(settingsStack);
        //lowerBoundBox.addChild();

    }

    public function makeUpdates():FilterDefinition
    {
        var filterToReturn:FilterDefinition;
        if (choiceBox.selectedItem == "Fixed Date Range") {
            if (_filterDefinition is FilterDateRangeDefinition) {
                if (leftComboBox.selectedIndex == 1) {
                    FilterDateRangeDefinition(_filterDefinition).startDateDimension = lowerDateFieldsBox.selectedItem.analysisItem;
                } else {
                    FilterDateRangeDefinition(_filterDefinition).startDateDimension = null;
                }
                if (rightComboBox.selectedIndex == 1) {
                    FilterDateRangeDefinition(_filterDefinition).endDateDimension = upperDateFieldsBox.selectedItem.analysisItem;
                } else {
                    FilterDateRangeDefinition(_filterDefinition).endDateDimension = null;
                }
                filterToReturn = _filterDefinition;
            } else {
                var newFilter:FilterDateRangeDefinition = new FilterDateRangeDefinition();
                newFilter.intrinsic = _filterDefinition.intrinsic;
                newFilter.field = _filterDefinition.field;
                newFilter.applyBeforeAggregation = _filterDefinition.applyBeforeAggregation;
                filterToReturn = newFilter;
            }
        } else if (choiceBox.selectedItem == "Relative Date Range") {
            if (_filterDefinition is RollingDateRangeFilterDefinition) {
                filterToReturn = _filterDefinition;
            } else {
                var newDateFilter:RollingDateRangeFilterDefinition = new RollingDateRangeFilterDefinition();
                newDateFilter.intrinsic = _filterDefinition.intrinsic;
                newDateFilter.field = _filterDefinition.field;
                newDateFilter.applyBeforeAggregation = _filterDefinition.applyBeforeAggregation;
                filterToReturn = newDateFilter;
            }
        }
        return filterToReturn;
    }

    public function set filterDefinition(filterDefinition:FilterDefinition):void
    {
        this._filterDefinition = filterDefinition;
        if (_filterDefinition is FilterDateRangeDefinition) {
            var dateRange:FilterDateRangeDefinition = _filterDefinition as FilterDateRangeDefinition;
            if (dateRange.startDateDimension != null) {
                lowerStackIndex = 1;
                lowerDateItemDisplay = dateRange.startDateDimension.display;
            }
            if (dateRange.endDateDimension != null) {
                upperStackIndex = 1;
                upperDateItemDisplay = dateRange.endDateDimension.display;
            }
        }
    }

    public function set feedID(feedID:int):void {
    }
}
}