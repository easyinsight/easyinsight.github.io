package com.easyinsight.filtering
{
import com.easyinsight.analysis.AnalysisDateDimension;
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

public class FlatDateDetailEditor extends VBox implements IFilterDetailEditor
{
    private var choiceBox:SmartComboBox;
    private var _type:int;

    [Bindable(event="typeChanged")]
    public function get type():int {
        return _type;
    }

    public function set type(value:int):void {
        if (_type == value) return;
        _type = value;
        dispatchEvent(new Event("typeChanged"));
    }

    private var _filterDefinition:FilterDefinition;

    public function FlatDateDetailEditor()
    {
        super();
    }

    public function set fields(value:ArrayCollection):void {

    }

    override protected function createChildren():void {
        super.createChildren();
        if (choiceBox == null) {
            choiceBox = new SmartComboBox();
            choiceBox.dataProvider = new ArrayCollection([ {label:"Month", data:AnalysisItemTypes.MONTH_LEVEL}, {label:"Year", data: AnalysisItemTypes.YEAR_LEVEL}]);

        }
        addChild(choiceBox);
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