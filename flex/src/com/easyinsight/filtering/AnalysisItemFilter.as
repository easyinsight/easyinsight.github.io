package com.easyinsight.filtering {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.skin.ImageConstants;

import com.easyinsight.util.SmartComboBox;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;

import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.Label;
import mx.events.DropdownEvent;
import mx.managers.PopUpManager;

public class AnalysisItemFilter extends HBox implements IFilter {
    private var _filterDefinition:AnalysisItemFilterDefinition;
    private var _feedID:int;
    private var _analysisItem:AnalysisItem;

    private var comboBox:SmartComboBox;
    private var deleteButton:Button;
    private var editButton:Button;
    private var _analysisItems:ArrayCollection;

    private var _filterEnabled:Boolean;

    [Bindable(event="filterEnabledChanged")]
    public function get filterEnabled():Boolean {
        return _filterEnabled;
    }

    public function set filterEnabled(value:Boolean):void {
        if (_filterEnabled == value) return;
        _filterEnabled = value;
        dispatchEvent(new Event("filterEnabledChanged"));
    }



    public function AnalysisItemFilter(feedID:int, analysisItem:AnalysisItem) {
        super();
        this._feedID = feedID;
        this._analysisItem = analysisItem;
        setStyle("verticalAlign", "middle");
    }

    private var _filterEditable:Boolean = true;

    public function set filterEditable(editable:Boolean):void {
        _filterEditable = editable;
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
        _analysisItems = analysisItems;
    }

    private function edit(event:MouseEvent):void {
        var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
        window.detailClass = AnalysisItemFilterEditor;
        window.feedID = _feedID;
        window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
        window.analysisItems = _analysisItems;
        window.filterDefinition = _filterDefinition;
        PopUpManager.addPopUp(window, this, true);
        window.x = 50;
        window.y = 50;
    }

    private function onFilterEdit(event:FilterEditEvent):void {
        _analysisItem = event.filterDefinition.field;
        comboBox.dataProvider = _filterDefinition.availableItems;
        comboBox.rowCount = Math.min(_filterDefinition.availableItems.length, 15);
    }

    private function onChange(event:Event):void {
        var checkbox:CheckBox = event.currentTarget as CheckBox;
        _filterDefinition.enabled = checkbox.selected;
        comboBox.enabled = checkbox.selected;
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
        label.text = FilterDefinition.getLabel(_filterDefinition, _analysisItem);
        addChild(label);
        if (comboBox == null) {
            comboBox = new SmartComboBox();
            comboBox.labelField = "display";
            comboBox.maxWidth = 300;
            comboBox.addEventListener(DropdownEvent.CLOSE, filterValueChanged);
        }
        addChild(comboBox);

        if (_filterEditable) {
            if (editButton == null) {
                editButton = new Button();
                editButton.addEventListener(MouseEvent.CLICK, edit);
                editButton.setStyle("icon", ImageConstants.EDIT_ICON);
                editButton.toolTip = "Edit";
            }
            addChild(editButton);
            if (deleteButton == null) {
                deleteButton = new Button();
                deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
                deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
                deleteButton.toolTip = "Delete";
            }
            addChild(deleteButton);
        }

        if (_filterDefinition == null) {
            _filterDefinition = new AnalysisItemFilterDefinition();
            _filterDefinition.availableItems = new ArrayCollection();
            _filterDefinition.availableItems.addItem(_analysisItem);
            _filterDefinition.targetItem = _analysisItem;
        }

        comboBox.dataProvider = _filterDefinition.availableItems;
        comboBox.rowCount = Math.min(_filterDefinition.availableItems.length, 15);
        comboBox.selectedProperty = "display";
        comboBox.selectedValue = _filterDefinition.targetItem.display;

        if (!_loadingFromReport) {
            if (newFilter) {
                dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
                newFilter = false;
            } else {
                dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, filterDefinition, filterDefinition, this));
            }
        } else {
            loadingFromReport = false;
            newFilter = false;
        }
    }

    private var newFilter:Boolean = true;

    private var _loadingFromReport:Boolean = false;


    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }

    private var _valuesSet:Boolean = true;


    [Bindable(event="valuesSetChanged")]
    public function get valuesSet():Boolean {
        return _valuesSet;
    }

    public function set valuesSet(value:Boolean):void {
        if (_valuesSet == value) return;
        _valuesSet = value;
        dispatchEvent(new Event("valuesSetChanged"));
    }

    private function filterValueChanged(event:DropdownEvent):void {
        var newValue:AnalysisItem = event.currentTarget.selectedItem as AnalysisItem;

        if (newValue != _filterDefinition.targetItem) {
            _filterDefinition.targetItem = newValue;
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }
    }

    private function deleteSelf(event:MouseEvent):void {
        dispatchEvent(new FilterDeletionEvent(this));
    }

    public function get filterDefinition():FilterDefinition {
        return _filterDefinition;
    }

    public function set filterDefinition(filterDefinition:FilterDefinition):void {
        _filterDefinition = filterDefinition as AnalysisItemFilterDefinition;
    }
}
}