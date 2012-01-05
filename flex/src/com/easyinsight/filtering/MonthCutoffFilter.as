package com.easyinsight.filtering {

import com.easyinsight.analysis.AnalysisItem;

import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.util.SmartComboBox;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Button;
import mx.controls.CheckBox;

import mx.controls.Label;
import mx.managers.PopUpManager;
import mx.rpc.events.FaultEvent;


public class MonthCutoffFilter extends HBox implements IFilter {
    private var comboBox:SmartComboBox;
    private var analysisItem:AnalysisItem;
    private var _filterDefinition:MonthCutoffFilterDefinition;
    private var _analysisItems:ArrayCollection;

    private var _loadingFromReport:Boolean = false;

    private var _selectedMonth:int;

    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }

    private var _reportID:int;

    private var _dashboardID:int;

    private var _feedID:int;

    public function set reportID(value:int):void {
        _reportID = value;
    }

    public function set dashboardID(value:int):void {
        _dashboardID = value;
    }

    public function MonthCutoffFilter(feedID:int, analysisItem:AnalysisItem, reportID:int, dashboardID:int) {
        super();
        this.analysisItem = analysisItem;
        _feedID = feedID;
        _reportID = reportID;
        _dashboardID = dashboardID;
    }

    private function onChange(event:Event):void {
        var checkbox:CheckBox = event.currentTarget as CheckBox;
        _filterDefinition.enabled = checkbox.selected;
        dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
    }

    private function onDataChange(event:Event):void {
        var month:int = comboBox.selectedItem.data;
        _filterDefinition.dateLevel = month;
        dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
    }


    override protected function createChildren():void {
        super.createChildren();

        if (_filterDefinition == null || !_filterDefinition.toggleEnabled) {
            var checkbox:CheckBox = new CheckBox();
            checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
            checkbox.toolTip = "Click to disable this filter.";
            checkbox.addEventListener(Event.CHANGE, onChange);
            addChild(checkbox);
        }

        var label:Label = new Label();
        label.text = FilterDefinition.getLabel(_filterDefinition, analysisItem);
        addChild(label);

        comboBox = new SmartComboBox();
        comboBox.addEventListener(Event.CHANGE, onDataChange);
        addChild(comboBox);

        if (_filterDefinition == null) {
            _filterDefinition = new MonthCutoffFilterDefinition();
            _filterDefinition.field = analysisItem;
            if (analysisItem.hasType(AnalysisItemTypes.STEP)) {
                _filterDefinition.applyBeforeAggregation = false;
            }
        } else {
            _selectedMonth = _filterDefinition.dateLevel;
        }

        var dp:ArrayCollection = new ArrayCollection();
        dp.addItem({label: "January", data: 0});
        dp.addItem({label: "February", data: 1});
        dp.addItem({label: "March", data: 2});
        dp.addItem({label: "April", data: 3});
        dp.addItem({label: "May", data: 4});
        dp.addItem({label: "June", data: 5});
        dp.addItem({label: "July", data: 6});
        dp.addItem({label: "August", data: 7});
        dp.addItem({label: "September", data: 8});
        dp.addItem({label: "October", data: 9});
        dp.addItem({label: "November", data: 10});
        dp.addItem({label: "December", data: 11});
        comboBox.dataProvider = dp;
        comboBox.selectedProperty = "data";
        comboBox.selectedValue = _selectedMonth;
        /*if (dp.getItemIndex(String(_filterDefinition.value)) != -1) {
         comboBox.selectedItem = String(_filterDefinition.value);
         } else {
         _filterDefinition.value = int(dp.getItemAt(0));
         }*/

        if (_filterEditable) {
            var editButton:Button = new Button();
            editButton.addEventListener(MouseEvent.CLICK, edit);
            editButton.setStyle("icon", ImageConstants.EDIT_ICON);
            editButton.toolTip = "Edit";
            addChild(editButton);


            var deleteButton:Button = new Button();
            deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
            deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
            if (_filterDefinition.intrinsic) {
                deleteButton.enabled = false;
                deleteButton.toolTip = "This filter is an intrinsic part of the data source and cannot be deleted.";
            } else {
                deleteButton.toolTip = "Delete";
            }
            addChild(deleteButton);
        }


        if (_loadingFromReport) {
            _loadingFromReport = false;
            newFilter = false;
        } else {
            if (newFilter) {
                dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
                newFilter = false;
            } else {
                dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, filterDefinition, filterDefinition, this));
            }
        }
    }

    private var newFilter:Boolean = true;

    public function set analysisItems(analysisItems:ArrayCollection):void {
        _analysisItems = analysisItems;
    }

    private function edit(event:MouseEvent):void {
        var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
        window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
        window.analysisItems = _analysisItems;
        window.filterDefinition = _filterDefinition;
        PopUpManager.addPopUp(window, this, true);
        window.x = 50;
        window.y = 50;
    }

    private function onFilterEdit(event:FilterEditEvent):void {
        if (event.filterDefinition is FilterDateRangeDefinition) {

        }
        dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this));
    }

    private function deleteSelf(event:MouseEvent):void {
        dispatchEvent(new FilterDeletionEvent(this));
    }

    private var _filterEditable:Boolean = true;

    public function set filterEditable(editable:Boolean):void {
        _filterEditable = editable;
    }

    public function set filterDefinition(filterDefinition:FilterDefinition):void {
        this._filterDefinition = filterDefinition as MonthCutoffFilterDefinition;
    }

    public function get filterDefinition():FilterDefinition {
        return _filterDefinition;
    }

    private var _showLabel:Boolean;

    public function set showLabel(show:Boolean):void {
        _showLabel = show;
    }
}
}