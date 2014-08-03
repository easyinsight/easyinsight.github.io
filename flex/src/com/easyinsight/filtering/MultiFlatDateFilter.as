package com.easyinsight.filtering
{

	import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemResultMetadata;
import com.easyinsight.analysis.IRetrievalState;
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.util.PopUpUtil;


import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Alert;

import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.Label;

import mx.controls.LinkButton;
import mx.core.UIComponent;
import mx.managers.PopUpManager;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class MultiFlatDateFilter extends HBox implements IFilter
	{
		private var analysisItem:AnalysisItem;
		private var _filterDefinition:MultiFlatDateFilterDefinition;
		private var _analysisItems:ArrayCollection;
        private var dataService:RemoteObject;
    private var _reportID:int;
    private var _dashboardID:int;

        private var _loadingFromReport:Boolean = false;


    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }

        private var _feedID:int;

        private var _retrievalState:IRetrievalState;

        private var filterMetadata:FilterMetadata;
		
		public function MultiFlatDateFilter(feedID:int, analysisItem:AnalysisItem, retrievalState:IRetrievalState, filterMetadata:FilterMetadata) {
			super();
			this.analysisItem = analysisItem;
            _feedID = feedID;
            this.filterMetadata = filterMetadata;
            _retrievalState = retrievalState;
            setStyle("verticalAlign", "middle");
		}

        private function onChange(event:Event):void {
            var checkbox:CheckBox = event.currentTarget as CheckBox;
            _filterDefinition.enabled = checkbox.selected;
            try {
                if (_retrievalState != null) {
                    _retrievalState.updateFilter(_filterDefinition, filterMetadata);
                }
            } catch (e:Error) {
            }
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }

        public function onClick(event:Event):void {
            var window:MultiFWindow = new MultiFWindow();
            window.values = levels;
            window.dateFilter = _filterDefinition;
            window.addEventListener("updated", onWindowDone, false, 0, true);
            PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUp(window);
        }

        private function onWindowDone(event:Event):void {
            populateLabel();
            try {
                if (_retrievalState != null) {
                    _retrievalState.updateFilter(_filterDefinition, filterMetadata);
                }
            } catch (e:Error) {
            }
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }

        private function populateLabel():void {
            if (_filterDefinition.levels.length == 0) {
                labelButton.label = "(Click to Configure)";
            } else {
                labelButton.label = _filterDefinition.createLabel();
            }
        }

        private var filterLabel:UIComponent;

        private var labelButton:LinkButton;

    private var levels:ArrayCollection;

        override protected function createChildren():void {
            super.createChildren();
            if (_filterDefinition == null || !_filterDefinition.toggleEnabled) {
                var checkbox:CheckBox = new CheckBox();
                checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
                checkbox.toolTip = "Click to disable this filter.";
                checkbox.addEventListener(Event.CHANGE, onChange);
                addChild(checkbox);
            }

			if (_filterDefinition == null) {
				_filterDefinition = new MultiFlatDateFilterDefinition();
				_filterDefinition.field = analysisItem;
			}


            if (_filterEditable) {
                filterLabel = new LinkButton();
                filterLabel.styleName = "filterLabel";
                filterLabel.addEventListener(MouseEvent.CLICK, edit);
                LinkButton(filterLabel).label = FilterDefinition.getLabel(_filterDefinition, analysisItem);
            } else {
                filterLabel = new Label();
                filterLabel.styleName = "filterLabel";
                Label(filterLabel).text = FilterDefinition.getLabel(_filterDefinition, analysisItem);
            }

            addChild(filterLabel);

            labelButton = new LinkButton();
            labelButton.setStyle("fontSize", 12);

            labelButton.setStyle("textDecoration", "underline");
            labelButton.addEventListener(MouseEvent.CLICK, onClick);
            addChild(labelButton);

            if (_filterEditable) {

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

            if (_filterDefinition == null || !_filterDefinition.cachedValues) {
                dataService = new RemoteObject();
                dataService.destination = "data";
                dataService.getMultiDateOptions.addEventListener(ResultEvent.RESULT, gotMetadata);
                dataService.getMultiDateOptions.addEventListener(FaultEvent.FAULT, onFault);
                dataService.getMultiDateOptions.send(_filterDefinition);
            } else {
                processMetadata(_filterDefinition.cachedValues);
            }
        }

    private function onFault(event:FaultEvent):void {
        Alert.show(event.fault.faultDetail);
    }

    private function gotMetadata(event:ResultEvent):void {
        var metadata:ArrayCollection = dataService.getMultiDateOptions.lastResult as ArrayCollection;
        processMetadata(metadata);
    }

    private function processMetadata(values:ArrayCollection):void {

        this.levels = values;
        var firstValue:int = 11;
        var lastValue:int = 0;

        for each (var wrapper:DateLevelWrapper in _filterDefinition.levels) {
            firstValue = Math.min(wrapper.dateLevel, firstValue);
            lastValue = Math.max(wrapper.dateLevel, lastValue);
        }

        this.first = firstValue;
        this.last = lastValue;

        populateLabel();


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

        private var first:int = -1;
        private var last:int = -1;



        private var newFilter:Boolean = true;
		
		public function set analysisItems(analysisItems:ArrayCollection):void {
			_analysisItems = analysisItems;
		}		
		
		private function edit(event:MouseEvent):void {
			var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
            window.filterMetadata = filterMetadata;
            window.detailClass = MultiDateFilterEditor;
			window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
			window.analysisItems = _analysisItems;
			window.filterDefinition = _filterDefinition;
			PopUpManager.addPopUp(window, this, true);
			window.x = 50;
			window.y = 50;
		}
		
		private function onFilterEdit(event:FilterEditEvent):void {
            dataService = new RemoteObject();
            dataService.destination = "data";
            dataService.getMultiDateOptions.addEventListener(ResultEvent.RESULT, gotMetadata);
            dataService.getMultiDateOptions.addEventListener(FaultEvent.FAULT, onFault);
            dataService.getMultiDateOptions.send(_filterDefinition);
            if (filterLabel is LinkButton) {
                LinkButton(filterLabel).label = FilterDefinition.getLabel(event.filterDefinition, analysisItem);
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
			this._filterDefinition = filterDefinition as MultiFlatDateFilterDefinition;
		}
		
		public function get filterDefinition():FilterDefinition {
			return _filterDefinition;
		}

        public function updateState():Boolean {
            var firstValue:int = 11;
            var lastValue:int;
            for each (var wrapper:DateLevelWrapper in _filterDefinition.levels) {
                firstValue = Math.min(wrapper.dateLevel, firstValue);
                lastValue = Math.max(wrapper.dateLevel, lastValue);
            }
            populateLabel();
            return (this.first != firstValue || this.last != lastValue);
        }
	}
}