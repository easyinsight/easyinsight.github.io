package com.easyinsight.filtering
{
import com.easyinsight.analysis.AnalysisItem;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.ComboBox;
import mx.controls.Label;
import mx.events.DropdownEvent;

public class EmbeddedRollingRangeFilter extends HBox implements IEmbeddedFilter
{
    private var rollingFilter:RollingDateRangeFilterDefinition;
    private var _feedID:int;
    private var _analysisItem:AnalysisItem;

    private var comboBox:ComboBox;
    private var deleteButton:Button;
    private var editButton:Button;
    private var _analysisItems:ArrayCollection;

    private var rangeOptions:ArrayCollection;

    public function EmbeddedRollingRangeFilter(feedID:int, analysisItem:AnalysisItem)
    {
        super();
        this._feedID = feedID;
        this._analysisItem = analysisItem;
        rangeOptions = new ArrayCollection();
        rangeOptions.addItem(new RangeOption("Last Day", RollingDateRangeFilterDefinition.DAY));
        rangeOptions.addItem(new RangeOption("Last 7 Days", RollingDateRangeFilterDefinition.WEEK));
        rangeOptions.addItem(new RangeOption("Last 30 Days", RollingDateRangeFilterDefinition.MONTH));
        rangeOptions.addItem(new RangeOption("Last 90 Days", RollingDateRangeFilterDefinition.QUARTER));
        rangeOptions.addItem(new RangeOption("Last 365 Days", RollingDateRangeFilterDefinition.YEAR));
        rangeOptions.addItem(new RangeOption("Today", RollingDateRangeFilterDefinition.DAY_TO_NOW));
        rangeOptions.addItem(new RangeOption("Week to Date", RollingDateRangeFilterDefinition.WEEK_TO_NOW));
        rangeOptions.addItem(new RangeOption("Month to Date", RollingDateRangeFilterDefinition.MONTH_TO_NOW));
        rangeOptions.addItem(new RangeOption("Quarter to Date", RollingDateRangeFilterDefinition.QUARTER_TO_NOW));
        rangeOptions.addItem(new RangeOption("Year to Date", RollingDateRangeFilterDefinition.YEAR_TO_NOW));
        rangeOptions.addItem(new RangeOption("Last Full Day", RollingDateRangeFilterDefinition.LAST_FULL_DAY));
        rangeOptions.addItem(new RangeOption("Last Full Week", RollingDateRangeFilterDefinition.LAST_FULL_WEEK));
        rangeOptions.addItem(new RangeOption("Last Full Month", RollingDateRangeFilterDefinition.LAST_FULL_MONTH));
        rangeOptions.addItem(new RangeOption("Last Day of Data", RollingDateRangeFilterDefinition.LAST_DAY));
    }

    public function set filterDefinition(filterDefinition:FilterDefinition):void
    {
        this.rollingFilter = filterDefinition as RollingDateRangeFilterDefinition;
    }

    private var _filterEditable:Boolean = true;

    public function set filterEditable(editable:Boolean):void {
        _filterEditable = editable;
    }

    public function get filterDefinition():FilterDefinition
    {
        return this.rollingFilter;
    }

    public function set analysisItems(analysisItems:ArrayCollection):void
    {
        this._analysisItems = analysisItems;
    }

    private function onChange(event:Event):void {
            var checkbox:CheckBox = event.currentTarget as CheckBox;
            rollingFilter.enabled = checkbox.selected;
            dispatchEvent(new EmbeddedFilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, rollingFilter, null, this));
        }

    private function onFilterEdit(event:FilterEditEvent):void {
        dispatchEvent(new EmbeddedFilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this));
    }

    override protected function createChildren():void {
        super.createChildren();
        //if (!_filterEditable) {
            var checkbox:CheckBox = new CheckBox();
            checkbox.selected = rollingFilter == null ? true : rollingFilter.enabled;
            checkbox.toolTip = "Click to disable this filter.";
            checkbox.addEventListener(Event.CHANGE, onChange);
            addChild(checkbox);
        //}
        if (_showLabel) {
            var label:Label = new Label();
            label.text = _analysisItem.display + ":";
            addChild(label);
        } else {
            toolTip = _analysisItem.display;
        }
        if (comboBox == null) {
            comboBox = new ComboBox();
            comboBox.rowCount = 14;
            comboBox.dataProvider = rangeOptions;
            comboBox.addEventListener(DropdownEvent.CLOSE, filterValueChanged);

            if (rollingFilter == null) {
                comboBox.selectedIndex = 0;
            } else {
                for each (var rangeOption:RangeOption in rangeOptions) {
                    if (rangeOption.data == rollingFilter.interval) {
                        comboBox.selectedItem = rangeOption;
                    }
                }
            }
            if (rollingFilter == null) {
                rollingFilter = new RollingDateRangeFilterDefinition();
                rollingFilter.field = _analysisItem;
            }
            dispatchEvent(new EmbeddedFilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
        }
        addChild(comboBox);
    }

    private function filterValueChanged(event:DropdownEvent):void {
        var newValue:int = event.currentTarget.selectedItem.data;
        var currentValue:int = rollingFilter.interval;
        if (newValue != currentValue) {
            rollingFilter.interval = newValue;
            dispatchEvent(new EmbeddedFilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, rollingFilter, null, this));
        }
    }

    override protected function commitProperties():void {
        super.commitProperties();
    }

    private function deleteSelf(event:MouseEvent):void {
        dispatchEvent(new EmbeddedFilterDeletionEvent(this));
    }

    private var _showLabel:Boolean;

    public function set showLabel(show:Boolean):void {
        _showLabel = show;
    }
}


}

class RangeOption {
    public var label:String;
    public var data:int;

    function RangeOption(label:String, data:int) {
        this.label = label;
        this.data = data;
    }
}