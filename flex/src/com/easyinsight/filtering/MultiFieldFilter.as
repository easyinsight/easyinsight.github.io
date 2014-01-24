package com.easyinsight.filtering {

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItemHandle;
import com.easyinsight.analysis.IRetrievalState;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.util.PopUpUtil;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.Label;
import mx.controls.LinkButton;
import mx.core.UIComponent;
import mx.managers.PopUpManager;

public class MultiFieldFilter extends HBox implements IFilter {
    private var _filterDefinition:MultiFieldFilterDefinition;
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

    public function MultiFieldFilter(feedID:int, reportID:int, dashboardID:int, report:AnalysisDefinition,
                                        dashboard:Dashboard, retrievalState:IRetrievalState, filterMetadata:FilterMetadata) {
        super();
        _feedID = feedID;
        _reportID = reportID;
        _dashboardID = dashboardID;
        _report = report;
        _dashboard = dashboard;
        _retrievalState = retrievalState;
        this.filterMetadata = filterMetadata;
        filterValues = new Button();
        filterValues.styleName = "multiFilterButton";
        setStyle("verticalAlign", "middle");

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
            window.detailClass = MultiFieldFilterEditor;
            window.analysisItems = _analysisItems;
            window.filterDefinition = _filterDefinition;
            PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUpWithY(window, 40);
        } else {
            var window2:NewEmbeddedMultiFieldFilterWindow = new NewEmbeddedMultiFieldFilterWindow();
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
        updateFilterLabel();
        dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this, event.bubbles, event.rebuild));
    }

    private function onChange(event:Event):void {
        var checkbox:CheckBox = event.currentTarget as CheckBox;
        _filterDefinition.enabled = checkbox.selected;
        dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
    }

    override protected function createChildren():void {
        super.createChildren();
        this.setStyle("horizontalGap", 2);

        if (_filterDefinition == null) {
            _filterDefinition = new MultiFieldFilterDefinition();
            _filterDefinition.toggleEnabled = true;
        }

        if (_filterDefinition.filterName == null) {
            _filterDefinition.filterName = "Fields";
        }

        if (!_filterDefinition.toggleEnabled) {
            var checkbox:CheckBox = new CheckBox();
            checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
            checkbox.toolTip = "Click to disable this filter.";
            checkbox.addEventListener(Event.CHANGE, onChange);
            addChild(checkbox);
        }


        var labelText:UIComponent;
        if (_filterEditable) {
            labelText = new LinkButton();
            labelText.addEventListener(MouseEvent.CLICK, edit);
            LinkButton(labelText).label = FilterDefinition.getLabel(_filterDefinition, null);
        } else {
            labelText = new Label();
            Label(labelText).text = FilterDefinition.getLabel(_filterDefinition, null);
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
        var window2:NewEmbeddedMultiFieldFilterWindow = new NewEmbeddedMultiFieldFilterWindow();
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

    private function updateFilterLabel():void {
        if (_filterDefinition && _filterDefinition.all) {
            filterValues.label = "All";
        } else if (_filterDefinition && _filterDefinition.selectedItems) {
            /*if (_filterDefinition.selectedItems.length == 1) {
                filterValues.label = _filterDefinition.selectedItems.getItemAt(0).display;
            } else {*/
            var length:int = 0;
            for each (var v:AnalysisItemHandle in _filterDefinition.selectedItems) {
                if (v.selected) {
                    length++;
                }
            }
                filterValues.label = length + " Item" + ((length == 1) ? "" : "s");
            //}
        } else {
            filterValues.label = ""
        }
        /*filterValues.toolTip = !_filterDefinition || !_filterDefinition.selectedItems ? "" : _filterDefinition.selectedItems.toArray().map(function (a:Object, b:int, c:int):String {
            return a.display;
        }).sort().join(", ");*/
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
        _filterDefinition = filterDefinition as MultiFieldFilterDefinition;
    }

    private var _showLabel:Boolean;

    public function set showLabel(show:Boolean):void {
        _showLabel = show;
    }

    public function regenerate():void {

    }
}
}