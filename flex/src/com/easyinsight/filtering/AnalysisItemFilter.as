package com.easyinsight.filtering {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRetrievalState;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.util.PopUpUtil;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.collections.Sort;
import mx.collections.SortField;
import mx.containers.HBox;

import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.Label;
import mx.controls.LinkButton;
import mx.managers.PopUpManager;

public class AnalysisItemFilter extends HBox implements IFilter {
    private var _filterDefinition:AnalysisItemFilterDefinition;
    private var _feedID:int;
    private var _analysisItem:AnalysisItem;

    private var linkButton:Button;
    private var deleteButton:Button;
    private var editButton:Button;
    private var _analysisItems:ArrayCollection;

    private var _filterEnabled:Boolean;


    private var _report:AnalysisDefinition;
    private var _dashboard:Dashboard;

    [Bindable(event="filterEnabledChanged")]
    public function get filterEnabled():Boolean {
        return _filterEnabled;
    }

    public function set filterEnabled(value:Boolean):void {
        if (_filterEnabled == value) return;
        _filterEnabled = value;
        dispatchEvent(new Event("filterEnabledChanged"));
    }

    private var _retrievalState:IRetrievalState;

    private var filterMetadata:FilterMetadata;

    public function AnalysisItemFilter(feedID:int, analysisItem:AnalysisItem, retrievalState:IRetrievalState, filterMetadata:FilterMetadata, report:AnalysisDefinition,
            dashboard:Dashboard) {
        super();
        this._feedID = feedID;
        this._analysisItem = analysisItem;
        this._retrievalState = retrievalState;
        this.filterMetadata = filterMetadata;
        this._report = report;
        this._dashboard = dashboard;
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
        window.filterMetadata = filterMetadata;
        window.detailClass = AnalysisItemFilterEditor;
        window.feedID = _feedID;
        window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
        var fields:ArrayCollection = new ArrayCollection(_analysisItems.toArray());
        var sort:Sort = new Sort();
        sort.fields = [new SortField("displayName")];
        fields.sort = sort;
        fields.refresh();
        window.analysisItems = fields;
        window.filterDefinition = _filterDefinition;
        PopUpManager.addPopUp(window, this, true);
        window.x = 50;
        window.y = 50;
    }

    private function onFilterEdit(event:FilterEditEvent):void {
        if (filterLabel != null) {
            filterLabel.text =  FilterDefinition.getLabel(event.filterDefinition, _analysisItem);
        }
        _analysisItem = event.filterDefinition.field;
        /*linkButton.dataProvider = _filterDefinition.availableItems;
        linkButton.rowCount = Math.min(_filterDefinition.availableItems.length, 15);*/
    }

    private function onUpdated(event:Event):void {
        if (_retrievalState != null) {
            _retrievalState.updateFilter(_filterDefinition, filterMetadata);
        }
        updateFilterLabel();
        dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
    }

    private function onChange(event:Event):void {
        var checkbox:CheckBox = event.currentTarget as CheckBox;
        _filterDefinition.enabled = checkbox.selected;
        linkButton.enabled = checkbox.selected;
        dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
    }

    private var filterLabel:Label;

    override protected function createChildren():void {
        super.createChildren();

        if (_filterDefinition == null || !_filterDefinition.toggleEnabled) {
            var checkbox:CheckBox = new CheckBox();
            checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
            checkbox.toolTip = "Click to disable this filter.";
            checkbox.addEventListener(Event.CHANGE, onChange);
            addChild(checkbox);
        }

        filterLabel = new Label();
        filterLabel.styleName = "filterLabel";

        filterLabel.text = FilterDefinition.getLabel(_filterDefinition, _analysisItem);
        addChild(filterLabel);
        if (linkButton == null) {
            linkButton = new Button();
            linkButton.maxWidth = 200;
            linkButton.addEventListener(MouseEvent.CLICK, onClick);
            linkButton.styleName = "multiFilterButton";
        }
        addChild(linkButton);

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

        updateFilterLabel();
        if (_loadingFromReport) {
            _loadingFromReport = false;

        } else {
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
        }
    }

    private function onClick(event:MouseEvent):void {
        var window:FieldChoiceFilterWindow = new FieldChoiceFilterWindow();
        window.embeddedFilter = _filterDefinition;
        window.dataSourceID = _feedID;
        window.report = _report;
        window.dashboard = _dashboard;
        window.addEventListener("updated", onUpdated, false, 0, true);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUpWithY(window, 40);
    }

    private function updateFilterLabel():void {
        linkButton.label = _filterDefinition.targetItem.unqualifiedDisplay;
    }

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