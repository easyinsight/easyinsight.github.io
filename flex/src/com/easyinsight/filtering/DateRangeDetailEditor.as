package com.easyinsight.filtering
{
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.CheckBox;
import mx.controls.ComboBox;
import mx.controls.DateField;

public class DateRangeDetailEditor extends HBox implements IFilterDetailEditor
{
    private var choiceBox:ComboBox;
    private var absoluteLowerBoundCheckBox:CheckBox;
    private var absoluteUpperBoundCheckBox:CheckBox;
    private var absoluteLowerBoundField:DateField;
    private var absoluteUpperBoundField:DateField;
    private var lowerDateFieldsBox:ComboBox;
    private var upperDateFieldsBox:ComboBox;
    private var choices:ArrayCollection;

    private var _filterDefinition:FilterDefinition;

    public function DateRangeDetailEditor()
    {
        super();
        choices = new ArrayCollection();
        choices.addItem("Fixed Date Range");
        choices.addItem("Relative Date Range");
    }

    override protected function createChildren():void {
        super.createChildren();
        if (choiceBox == null) {
            choiceBox = new ComboBox();
            choiceBox.dataProvider = choices;
            choiceBox.selectedItem = _filterDefinition is FilterDateRangeDefinition ? "Fixed Date Range" : "Relative Date Range";
        }
        addChild(choiceBox);
        var hbox:HBox = new HBox();
        var lowerBoundBox:VBox = new VBox();
        
        //lowerBoundBox.addChild();
        var upperBoundBox:VBox = new VBox();
    }

    public function makeUpdates():FilterDefinition
    {
        var filterToReturn:FilterDefinition;
        if (choiceBox.selectedItem == "Fixed Date Range") {
            if (_filterDefinition is FilterDateRangeDefinition) {
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
    }

    public function set feedID(feedID:int):void {
    }
}
}