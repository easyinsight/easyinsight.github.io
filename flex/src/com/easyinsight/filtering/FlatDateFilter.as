package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisDateDimensionResultMetadata;
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.AnalysisItemResultMetadata;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.skin.ImageConstants;

import flash.events.Event;
import flash.events.MouseEvent;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Alert;
import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.ComboBox;
import mx.controls.Label;
import mx.managers.PopUpManager;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class FlatDateFilter extends HBox implements IFilter
	{
		private var dataService:RemoteObject;
		private var comboBox:ComboBox;
		private var analysisItem:AnalysisItem;
		private var _filterDefinition:FlatDateFilterDefinition;
		private var _analysisItems:ArrayCollection;

        private var _loadingFromReport:Boolean = false;


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

		
		public function FlatDateFilter(feedID:int, analysisItem:AnalysisItem, reportID:int, dashboardID:int) {
			super();
			this.analysisItem = analysisItem;
            _feedID = feedID;
            _reportID = reportID;
            _dashboardID = dashboardID;
		}

        private function onFault(event:FaultEvent):void {
            Alert.show(event.fault.faultDetail);
        }

        private function onChange(event:Event):void {
            var checkbox:CheckBox = event.currentTarget as CheckBox;
            _filterDefinition.enabled = checkbox.selected;
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }

        private function onDataChange(event:Event):void {
            _filterDefinition.value = parseInt(comboBox.selectedItem as String);
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

            comboBox = new ComboBox();
            comboBox.addEventListener(Event.CHANGE, onDataChange);
            addChild(comboBox);
            if (_filterDefinition == null || !_filterDefinition.cachedValues) {
                dataService = new RemoteObject();
                dataService.destination = "data";
                dataService.getAnalysisItemMetadata.addEventListener(ResultEvent.RESULT, gotMetadata);
                dataService.getAnalysisItemMetadata.addEventListener(FaultEvent.FAULT, onFault);
                dataService.getAnalysisItemMetadata.send(_feedID, analysisItem, new Date().getTimezoneOffset(), _reportID, _dashboardID);
            } else {
                processMetadata(_filterDefinition.cachedValues);
            }
        }

        private function gotMetadata(event:ResultEvent):void {
			var metadata:AnalysisItemResultMetadata = dataService.getAnalysisItemMetadata.lastResult as AnalysisItemResultMetadata;
			processMetadata(metadata);
		}

        private function processMetadata(metadata:AnalysisItemResultMetadata):void {
            var dateMetadata:AnalysisDateDimensionResultMetadata = metadata as AnalysisDateDimensionResultMetadata;
			var lowDate:Date = dateMetadata.earliestDate;
            if (lowDate == null) {
                lowDate = new Date(new Date().getTime() - (1000 * 60 * 60 * 24 * 30));
            }
			var highDate:Date = dateMetadata.latestDate;
            if (highDate == null) {
                highDate = new Date();
            }

			if (_filterDefinition == null) {
				_filterDefinition = new FlatDateFilterDefinition();
				_filterDefinition.field = analysisItem;
                if (analysisItem.hasType(AnalysisItemTypes.STEP)) {
                    _filterDefinition.applyBeforeAggregation = false;
                }
			} else {
			}

            var latestYear:String = String(new Date().fullYear);


            var dp:ArrayCollection = new ArrayCollection();
            if (_filterDefinition.dateLevel == AnalysisItemTypes.YEAR_LEVEL) {
                for (var year:int = lowDate.getFullYear(); year <= highDate.getFullYear(); year++) {
                    dp.addItem(String(year));
                }
            }
            if (!dp.contains(latestYear)) {
                dp.addItem(latestYear);
            }
            comboBox.dataProvider = dp;
            if (dp.getItemIndex(String(_filterDefinition.value)) != -1) {
                comboBox.selectedItem = String(_filterDefinition.value);
            } else {
                _filterDefinition.value = int(dp.getItemAt(0));
            }

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
			this._filterDefinition = filterDefinition as FlatDateFilterDefinition;
		}
		
		public function get filterDefinition():FilterDefinition {
			return _filterDefinition;
		}

        public function updateState():Boolean {
            var existingYear:String = String(comboBox.selectedItem);
            var newYear:String = String(_filterDefinition.value);
            comboBox.selectedItem = newYear;
            return existingYear != newYear;
        }
    }
}