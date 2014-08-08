package com.easyinsight.filtering {

import com.easyinsight.WindowManagement;
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRetrievalState;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.util.PopUpUtil;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.Label;
import mx.controls.LinkButton;
import mx.core.UIComponent;
import mx.managers.PopUpManager;

public class NewMultiValueFilter extends UIComponent implements IFilter {
    private var _filterDefinition:FilterValueDefinition;
    private var _analysisItem:AnalysisItem;
    private var _feedID:int;
    private var deleteButton:Button;
    private var _analysisItems:ArrayCollection;

    private var _report:AnalysisDefinition;
    private var _otherFilters:ArrayCollection;
    private var _dashboard:Dashboard;

    private var _loadingFromReport:Boolean = false;

    private var _retrievalState:IRetrievalState;

    private var filterValues:Button;


    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }

    private var filterMetadata:FilterMetadata;

    public function NewMultiValueFilter(feedID:int, analysisItem:AnalysisItem, reportID:int, dashboardID:int, report:AnalysisDefinition,
                                        otherFilters:ArrayCollection, dashboard:Dashboard, retrievalState:IRetrievalState, filterMetadata:FilterMetadata) {
        super();
        _analysisItem = analysisItem;
        _feedID = feedID;
        _reportID = reportID;
        _dashboardID = dashboardID;
        _report = report;
        _dashboard = dashboard;
        _otherFilters = otherFilters;
        _retrievalState = retrievalState;
        this.filterMetadata = filterMetadata;
        filterValues = new Button();
        filterValues.styleName = "multiFilterButton";
        this.height = 23;
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
        _analysisItems = analysisItems;
    }

    private var _reportID:int;

    private var _dashboardID:int;

    public function set reportID(value:int):void {
        _reportID = value;
    }

    public function set dashboardID(value:int):void {
        _dashboardID = value;
    }

    public function edit(event:MouseEvent):void {
        if (_filterEditable) {
            var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
            window.filterMetadata = filterMetadata;
            window.feedID = _feedID;
            window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
            window.detailClass = NewMultiValueFilterEditWindow;
            window.analysisItems = _analysisItems;
            window.filterDefinition = _filterDefinition;
            PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUpWithY(window, 40);
        } else {
            var window2:NewEmbeddedMultiValueFilterWindow = new NewEmbeddedMultiValueFilterWindow();
            window2.reportID = _reportID;
            window2.dashboardID = _dashboardID;
            window2.embeddedFilter = _filterDefinition;
            window2.dataSourceID = _feedID;
            window2.report = _report;
            window2.otherFilters = _otherFilters;
            window2.dashboard = _dashboard;
            window2.addEventListener("updated", onUpdated, false, 0, true);
            PopUpManager.addPopUp(window2, this, true);
            PopUpUtil.centerPopUpWithY(window2, 40);
        }
    }

    private function onUpdated(event:Event):void {
        updateFilterLabel();
        if (_retrievalState != null) {
            _retrievalState.updateFilter(_filterDefinition, filterMetadata);
        }
        dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
    }

    private function onFilterEdit(event:FilterEditEvent):void {
        if (labelText != null && labelText is LinkButton) {
            LinkButton(labelText).label = FilterDefinition.getLabel(event.filterDefinition, _analysisItem);
        }
        updateFilterLabel();
        dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this, event.bubbles, event.rebuild));
    }

    private function onChange(event:Event):void {
        var checkbox:CheckBox = event.currentTarget as CheckBox;
        _filterDefinition.enabled = checkbox.selected;
        dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        var xPos:int = 0;
        if (checkbox) {
            xPos += checkbox.measuredWidth + 8;
            checkbox.y = (this.height - checkbox.height) / 2;
            checkbox.setActualSize(checkbox.measuredWidth, checkbox.measuredHeight);
        }
        if (labelText) {
            labelText.x = xPos;
            labelText.y = (this.height - labelText.height) / 2;
            labelText.setActualSize(labelText.measuredWidth, labelText.measuredHeight);
            xPos += labelText.measuredWidth + 3;
        }
        if (filterValues) {
            filterValues.x = xPos;
            filterValues.y = (this.height - filterValues.height) / 2;
            filterValues.setActualSize(filterValues.measuredWidth, filterValues.measuredHeight);
            xPos += filterValues.measuredWidth;
        }
        if (deleteButton) {
            xPos += 8;
            deleteButton.x = xPos;
            deleteButton.y = (this.height - deleteButton.height) / 2;
            deleteButton.setActualSize(deleteButton.measuredWidth, deleteButton.measuredHeight);
            xPos += deleteButton.measuredWidth;
        }
        this.width = xPos;
    }

    private var checkbox:CheckBox;
    private var labelText:UIComponent;

    override protected function createChildren():void {
        super.createChildren();
        this.setStyle("horizontalGap", 2);

        if (_filterDefinition == null) {
            _filterDefinition = new FilterValueDefinition();
            _filterDefinition.inclusive = true;
            _filterDefinition.newType = true;
            _filterDefinition.toggleEnabled = true;
            _filterDefinition.filteredValues = new ArrayCollection(["All"]);
            _filterDefinition.field = _analysisItem;
        }

        if (_filterDefinition == null || !_filterDefinition.toggleEnabled) {
            checkbox = new CheckBox();
            checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
            checkbox.toolTip = "Click to disable this filter.";
            checkbox.addEventListener(Event.CHANGE, onChange);
            addChild(checkbox);
        }



        if (_filterEditable) {
            labelText = new LinkButton();
            labelText.addEventListener(MouseEvent.CLICK, edit);
            LinkButton(labelText).label = FilterDefinition.getLabel(_filterDefinition, _analysisItem);
        } else {
            labelText = new Label();
            Label(labelText).text = FilterDefinition.getLabel(_filterDefinition, _analysisItem);
        }
        labelText.styleName = "filterLabel";
        addChild(labelText);

        filterValues.maxWidth = 150;
        filterValues.addEventListener(MouseEvent.CLICK, showFilter);
        updateFilterLabel();
        addChild(filterValues);

        if (_filterEditable) {

            if (deleteButton == null) {
                deleteButton = new Button();
                deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
                deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
                deleteButton.toolTip = "Delete";
                deleteButton.enabled = false;
            }
            addChild(deleteButton);
        }

        if (deleteButton != null) {
            deleteButton.enabled = true;
        }
        updateFilterLabel();
        if (_loadingFromReport) {
            _loadingFromReport = false;

        } else {
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
        }
    }

    public function showFilter(event:MouseEvent):void {
        var window2:NewEmbeddedMultiValueFilterWindow = new NewEmbeddedMultiValueFilterWindow();
        window2.reportID = _reportID;
        window2.dashboardID = _dashboardID;
        window2.embeddedFilter = _filterDefinition;
        window2.dataSourceID = _feedID;
        window2.report = _report;
        window2.otherFilters = _otherFilters;
        window2.dashboard = _dashboard;
        window2.addEventListener("updated", onUpdated, false, 0, true);
        WindowManagement.manager.addWindow(window2);
        PopUpManager.addPopUp(window2, this, true);
        PopUpUtil.centerPopUpWithY(window2, 40);
    }

    private function updateFilterLabel():void {
        if (_filterDefinition && _filterDefinition.filteredValues) {
            if (_filterDefinition.notCondition) {
                filterValues.label = "[ Excluding Values ]";
            } else {
                if (_filterDefinition.filteredValues.length == 1) {
                    filterValues.label = _filterDefinition.filteredValues.getItemAt(0).toString();
                    if (filterValues.label == "")
                        filterValues.label = "[ No Value ]";

                } else {
                    filterValues.label = _filterDefinition.filteredValues.length + " Item" + ((_filterDefinition.filteredValues.length == 1) ? "" : "s");
                }
            }
        } else {
            filterValues.label = ""
        }
        tt = (!_filterDefinition || !_filterDefinition.filteredValues ? "" : _filterDefinition.filteredValues.toArray().map(function (a:Object, b:int, c:int):String {
            if (a == "") return "[ No Value ]";

            return a.toString();
        }).sort().join(", "));

        filterValues.toolTip = tt;
    }

    private var tt:String;

    public function toInclusive(filterValues:ArrayCollection):void {
        _filterDefinition.inclusive = true;
        _filterDefinition.filteredValues = filterValues;
        //updateState();
    }

    public function removeValues(filterValues:ArrayCollection):void {
        for each (var string:String in filterValues) {
            var index:int = _filterDefinition.filteredValues.getItemIndex(string);
            if (index != -1) _filterDefinition.filteredValues.removeItemAt(index);
        }
    }

    public function addValues(filterValues:ArrayCollection):void {
        if (_filterDefinition.inclusive) {
            _filterDefinition.filteredValues = filterValues;
        } else {
            _filterDefinition.filteredValues = new ArrayCollection(_filterDefinition.filteredValues.toArray().concat(filterValues.toArray()));
        }
        //updateState();
    }

    public function addValue(value:String):void {
        if (_filterDefinition.filteredValues == null) {
            _filterDefinition.filteredValues = new ArrayCollection();
        }
        _filterDefinition.filteredValues.addItem(value);
    }

    public function updateState():Boolean {
        /*var selectedValue:String;

        var filterObj:Object = _filterDefinition.filteredValues.getItemAt(0);
        if (filterObj is Value) {
            selectedValue = String(filterObj.getValue());
        } else {
            selectedValue = filterObj as String;
        }
        var existingState:String = comboBox.selectedItem as String;*/
        var str:String = (!_filterDefinition || !_filterDefinition.filteredValues ? "" : _filterDefinition.filteredValues.toArray().map(function (a:Object, b:int, c:int):String {
            if (a == "") return "[ No Value ]";

            return a.toString();
        }).sort().join(", "));
        var changed:Boolean = false;
        if (tt != str) {
            changed = true;
            updateFilterLabel();
        }

        //return existingState != selectedValue;

        return changed;
    }

    public function get inclusive():Boolean {
        return _filterDefinition.inclusive;
    }

    private function deleteSelf(event:MouseEvent):void {
        dispatchEvent(new FilterDeletionEvent(this));
    }

    public function get filterDefinition():FilterDefinition {
        return _filterDefinition;
    }

    private var _filterEditable:Boolean = true;

    public function set filterEditable(editable:Boolean):void {
        _filterEditable = editable;
    }

    public function set filterDefinition(filterDefinition:FilterDefinition):void {
        _filterDefinition = filterDefinition as FilterValueDefinition;
    }

    private var _showLabel:Boolean;

    public function set showLabel(show:Boolean):void {
        _showLabel = show;
    }

    public function regenerate():void {

    }
}
}