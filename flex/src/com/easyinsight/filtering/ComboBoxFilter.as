package com.easyinsight.filtering {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisDimensionResultMetadata;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemResultMetadata;
import com.easyinsight.analysis.ReportFault;

import com.easyinsight.analysis.Value;
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.util.PopUpUtil;

import flash.events.Event;
import flash.events.MouseEvent;
import flash.utils.Dictionary;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.ComboBox;
import mx.controls.Label;
import mx.controls.LinkButton;
import mx.controls.ProgressBar;
import mx.core.UIComponent;
import mx.events.DropdownEvent;
import mx.managers.PopUpManager;
import mx.managers.PopUpManager;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class ComboBoxFilter extends UIComponent implements IFilter {
    private var _filterDefinition:FilterValueDefinition;
    private var _feedID:int;
    private var dataService:RemoteObject;
    private var _analysisItem:AnalysisItem;

    private var comboBox:ComboBox;
    private var deleteButton:Button;
    private var editButton:Button;
    private var _analysisItems:ArrayCollection;

    private var _filterEnabled:Boolean;

    private var _reportID:int;

    private var _dashboardID:int;

    private var _otherFilters:ArrayCollection;

    public function set otherFilters(value:ArrayCollection):void {
        _otherFilters = value;
    }

    public function set reportID(value:int):void {
        _reportID = value;
    }

    public function set dashboardID(value:int):void {
        _dashboardID = value;
    }

    [Bindable(event="filterEnabledChanged")]
    public function get filterEnabled():Boolean {
        return _filterEnabled;
    }

    public function set filterEnabled(value:Boolean):void {
        if (_filterEnabled == value) return;
        _filterEnabled = value;
        dispatchEvent(new Event("filterEnabledChanged"));
    }

    private var _report:AnalysisDefinition;

    public function ComboBoxFilter(feedID:int, analysisItem:AnalysisItem, reportID:int, dashboardID:int, report:AnalysisDefinition, otherFilters:ArrayCollection) {
        super();
        this._report = report;
        this._feedID = feedID;
        this._analysisItem = analysisItem;
        _otherFilters = otherFilters;
        this.reportID = reportID;
        this.dashboardID = dashboardID;
        this.height = 23;
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
        window.feedID = _feedID;
        window.detailClass = ComboBoxFilterWindow;
        window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
        window.analysisItems = _analysisItems;
        window.filterDefinition = _filterDefinition;
        PopUpManager.addPopUp(window, this, true);
        window.x = 50;
        window.y = 50;
    }

    private function onFilterEdit(event:FilterEditEvent):void {
        _analysisItem = event.filterDefinition.field;
        if (event.filterDefinition != this.filterDefinition || !FilterValueDefinition(event.filterDefinition).singleValue || FilterValueDefinition(event.filterDefinition).autoComplete) {
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this, event.bubbles, event.rebuild));
        } else {
            //viewStack.selectedIndex = 0;
            dataService = new RemoteObject();
            dataService.destination = "data";
            dataService.getAnalysisItemMetadata.addEventListener(ResultEvent.RESULT, gotMetadata);
            dataService.getAnalysisItemMetadata.send(_feedID, event.filterDefinition.field, new Date().getTimezoneOffset(), _reportID, _dashboardID, _report, _otherFilters, _filterDefinition);
        }
    }

    public function regenerate():void {
        _loadingFromReport = true;
        _filterDefinition.filteredValues = new ArrayCollection(["All"]);
        dataService = new RemoteObject();
        dataService.destination = "data";
        dataService.getAnalysisItemMetadata.addEventListener(ResultEvent.RESULT, gotMetadata);
        dataService.getAnalysisItemMetadata.send(_feedID, filterDefinition.field, new Date().getTimezoneOffset(), _reportID, _dashboardID, _report, _otherFilters, _filterDefinition);
    }

    private function onChange(event:Event):void {
        var checkbox:CheckBox = event.currentTarget as CheckBox;
        _filterDefinition.enabled = checkbox.selected;
        comboBox.enabled = checkbox.selected;
        dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
    }
    
    private var checkbox:CheckBox;
    
    private var filterLabel:UIComponent;

    override protected function createChildren():void {
        super.createChildren();

        if (_filterDefinition == null) {
            _filterDefinition = new FilterValueDefinition();
            _filterDefinition.field = _analysisItem;
            _filterDefinition.filteredValues = new ArrayCollection();
            _filterDefinition.inclusive = true;
            _filterDefinition.enabled = true;
            _filterDefinition.singleValue = true;
            _filterDefinition.allOption = true;
            _filterDefinition.toggleEnabled = true;
        }

        /*var hbox:HBox = new HBox();
        hbox.percentHeight = 100;
        hbox.setStyle("verticalAlign", "middle");*/

        if (_filterDefinition == null || !_filterDefinition.toggleEnabled) {
            checkbox = new CheckBox();
            checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
            checkbox.toolTip = "Click to disable this filter.";
            checkbox.addEventListener(Event.CHANGE, onChange);
            //addChild(checkbox);
        }

        if (_filterEditable) {
            filterLabel = new LinkButton();
            filterLabel.setStyle("fontSize", 12);
            filterLabel.setStyle("textDecoration", "underline");
            LinkButton(filterLabel).label = FilterDefinition.getLabel(_filterDefinition, _analysisItem);
        } else {
            filterLabel = new Label();
            Label(filterLabel).text = FilterDefinition.getLabel(_filterDefinition, _analysisItem);
        }
        
        
        //addChild(label);

        if (comboBox == null) {
            comboBox = new ComboBox();
            comboBox.maxWidth = 300;
            comboBox.addEventListener(DropdownEvent.CLOSE, filterValueChanged);
            comboBox.enabled = false;
        }
        //hbox.addChild(comboBox);

        //addChild(viewStack);

        if (_filterEditable) {
            filterLabel.addEventListener(MouseEvent.CLICK, edit);
            /*if (editButton == null) {
                editButton = new Button();
                editButton.addEventListener(MouseEvent.CLICK, edit);
                editButton.setStyle("icon", ImageConstants.EDIT_ICON);
                editButton.toolTip = "Edit";
            }*/
            //hbox.addChild(editButton);
            if (deleteButton == null) {
                deleteButton = new Button();
                deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
                deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
                deleteButton.toolTip = "Delete";
                deleteButton.enabled = false;
            }
            //hbox.addChild(deleteButton);
        }


        /*var loadingBox:HBox = new HBox();
        loadingBox.height = 23;
        loadingBox.setStyle("verticalAlign", "middle");*/
        if (_filterDefinition.cachedValues) {
            valuesSet = false;
            invalidateDisplayList();
        } else {
            loadingBar = new ProgressBar();
            loadingBar.height = 23;
            loadingBar.width = 300;
            loadingBar.label = "";
            loadingBar.labelPlacement = "right";
            BindingUtils.bindProperty(loadingBar, "indeterminate", this, "valuesSet");
            loadingBar.indeterminate = true;
            addChild(loadingBar);
        }
        if (_filterDefinition == null || _filterDefinition.cachedValues == null) {
            dataService = new RemoteObject();
            dataService.destination = "data";
            dataService.getAnalysisItemMetadata.addEventListener(ResultEvent.RESULT, gotMetadata);
            dataService.getAnalysisItemMetadata.addEventListener(FaultEvent.FAULT, onFault);
            dataService.getAnalysisItemMetadata.send(_feedID, _analysisItem, new Date().getTimezoneOffset(), _reportID, _dashboardID, _report, _otherFilters, _filterDefinition);
        } else {
            processMetadata(_filterDefinition.cachedValues as AnalysisDimensionResultMetadata);
        }
    }

    private function onFault(event:FaultEvent):void {
        Alert.show(event.fault.faultString);
    }
    
    private var loadingBar:ProgressBar;

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (valuesSet) {
            
        } else {
            if (loadingBar) {
                removeChild(loadingBar);
                loadingBar = null;
            }
            if (!filterLabel.parent) {
                if (checkbox) {
                    addChild(checkbox);
                }
                addChild(filterLabel);
                addChild(comboBox);
                if (editButton) {
                    addChild(editButton);
                }
                if (deleteButton) {
                    addChild(deleteButton);
                }
            }
            var xPos:int = 0;
            if (checkbox) {
                xPos += checkbox.measuredWidth + 8;
                checkbox.y = (this.height - checkbox.height) / 2;
                checkbox.setActualSize(checkbox.measuredWidth, checkbox.measuredHeight);
            }
            filterLabel.x = xPos;
            filterLabel.y = (this.height - filterLabel.height) / 2;
            filterLabel.setActualSize(filterLabel.measuredWidth, filterLabel.measuredHeight);            
            xPos += filterLabel.measuredWidth;
            comboBox.x = xPos;
            comboBox.y = (this.height - comboBox.height) / 2;
            comboBox.setActualSize(comboBox.measuredWidth, comboBox.measuredHeight);
            xPos += comboBox.measuredWidth;
            if (editButton) {
                xPos += 8;
                editButton.x = xPos;
                editButton.y = (this.height - editButton.height) / 2;
                editButton.setActualSize(editButton.measuredWidth, editButton.measuredHeight);
                xPos += editButton.measuredWidth + 8;
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
        var newValue:String = event.currentTarget.selectedLabel;

        var filterObj:Object = _filterDefinition.filteredValues.getItemAt(0);
        var selectedValue:String;
        if (filterObj is Value) {
            selectedValue = String(filterObj.getValue());
        } else {
            selectedValue = filterObj as String;
        }
        if (newValue != selectedValue) {
            var newFilteredValues:ArrayCollection = new ArrayCollection();
            newFilteredValues.addItem(newValue);
            _filterDefinition.filteredValues = newFilteredValues;
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }
    }

    public function updateState():Boolean {
        var selectedValue:String;
        var filterObj:Object = _filterDefinition.filteredValues.getItemAt(0);
        if (filterObj is Value) {
            selectedValue = String(filterObj.getValue());
        } else {
            selectedValue = filterObj as String;
        }
        var existingState:String = comboBox.selectedItem as String;
        comboBox.selectedItem = selectedValue;
        return existingState != selectedValue;

    }

    private function processMetadata(metadata:AnalysisItemResultMetadata):void {
        if (metadata.reportFault != null) {
            var window:UIComponent = ReportFault(metadata.reportFault).createFaultWindow();
            PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUp(window);
        }
        try {
            if (!(metadata is AnalysisDimensionResultMetadata)) {
                if (_filterEditable) {
                    deleteButton.enabled = true;
                }
                valuesSet = false;
                invalidateDisplayList();
                return;
            }
            var analysisDimensionResultMetadata:AnalysisDimensionResultMetadata = metadata as AnalysisDimensionResultMetadata;
            var valueObj:Dictionary = new Dictionary();
            if (analysisDimensionResultMetadata != null && analysisDimensionResultMetadata.values != null) {
                for each (var value:Value in analysisDimensionResultMetadata.values) {
                    var string:String = String(value.getValue());
                    valueObj[string] = true;
                }
            }
            if (_filterDefinition != null && _filterDefinition.excludeEmpty) {
                delete valueObj[""];
            }
            var strings:Array = [];
            for (var str:String in valueObj) {
                strings.push(str);
            }
            strings = strings.sort(Array.CASEINSENSITIVE | Array.DESCENDING);
            if (_filterDefinition != null && _filterDefinition.allOption) {
                strings.push("All");
            }
            strings = strings.reverse();
            comboBox.dataProvider = new ArrayCollection(strings);
            comboBox.rowCount = Math.min(strings.length, 15);
            var selectedValue:String;
            if (_filterDefinition.filteredValues.length == 0 && strings.length > 0) {
                _filterDefinition.filteredValues.addItem(strings[0]);
            }
            if (_filterDefinition.filteredValues.length > 0) {
                var filterObj:Object = _filterDefinition.filteredValues.getItemAt(0);
                if (filterObj is Value) {
                    selectedValue = String(filterObj.getValue());
                } else {
                    selectedValue = filterObj as String;
                }
                var selectedIndex:int = strings.indexOf(selectedValue);
                if (selectedIndex == -1) {
                    selectedValue = strings[0] as String;
                    var newFilteredValues:ArrayCollection = new ArrayCollection();
                    newFilteredValues.addItem(selectedValue);
                    _filterDefinition.filteredValues = newFilteredValues;
                }
                comboBox.selectedItem = selectedValue;

            }

            comboBox.enabled = _filterDefinition.enabled;
            if (deleteButton != null) {
                deleteButton.enabled = true;
            }
            valuesSet = false;
            invalidateDisplayList();
            //viewStack.selectedIndex = 1;
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
        } catch (e:Error) {
            Alert.show(e.message);
        }
    }

    private function gotMetadata(event:ResultEvent):void {
        var analysisDimensionResultMetadata:AnalysisItemResultMetadata = dataService.getAnalysisItemMetadata.lastResult as
                AnalysisItemResultMetadata;
        processMetadata(analysisDimensionResultMetadata);
    }

    private function deleteSelf(event:MouseEvent):void {
        dispatchEvent(new FilterDeletionEvent(this));
    }

    public function get filterDefinition():FilterDefinition {
        return _filterDefinition;
    }

    public function set filterDefinition(filterDefinition:FilterDefinition):void {
        _filterDefinition = filterDefinition as FilterValueDefinition;
    }
}
}
