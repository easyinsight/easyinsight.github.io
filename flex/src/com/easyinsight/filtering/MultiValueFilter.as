package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisDimensionResultMetadata;
	import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.util.PopUpUtil;

import flash.events.Event;
import flash.events.MouseEvent;
	
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.Label;
	import mx.managers.PopUpManager;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class MultiValueFilter extends HBox implements IFilter
	{
		private var _filterDefinition:FilterValueDefinition;
		private var _analysisItem:AnalysisItem;
		private var _feedID:int;		
		private var deleteButton:Button;
		private var editButton:Button;
		private var labelText:Label;
		private var dataService:RemoteObject;		
		private var _analysisItems:ArrayCollection;

        private var _loadingFromReport:Boolean = false;


    public function set loadingFromReport(value:Boolean):void {
        _loadingFromReport = value;
    }

		public function MultiValueFilter(feedID:int, analysisItem:AnalysisItem) {
			super();
			_analysisItem = analysisItem;
			_feedID = feedID;
		}
		
		public function set analysisItems(analysisItems:ArrayCollection):void {
			_analysisItems = analysisItems;
		}		
		
		public function edit(event:MouseEvent):void {
            if (_filterEditable) {
                var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
                window.feedID = _feedID;
                window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit, false, 0, true);
                window.detailClass = MultiValueFilterWindow;
                window.analysisItems = _analysisItems;
                window.filterDefinition = _filterDefinition;
                PopUpManager.addPopUp(window, this, true);
                PopUpUtil.centerPopUpWithY(window, 40);
            } else {
                var window2:EmbeddedMultiValueFilterWindow = new EmbeddedMultiValueFilterWindow();
                window2.embeddedFilter = _filterDefinition;
                window2.dataSourceID = _feedID;
                window2.addEventListener("updated", onUpdated, false, 0, true);
                PopUpManager.addPopUp(window2, this, true);
                PopUpUtil.centerPopUpWithY(window2, 40);
            }
		}

        private function onUpdated(event:Event):void {
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }
		
		private function onFilterEdit(event:FilterEditEvent):void {
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this, event.bubbles, event.rebuild));
		}

        private function onChange(event:Event):void {
            var checkbox:CheckBox = event.currentTarget as CheckBox;
            _filterDefinition.enabled = checkbox.selected;
            dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
        }
		
		override protected function createChildren():void {
			super.createChildren();
            //if (!_filterEditable) {
                var checkbox:CheckBox = new CheckBox();
                checkbox.selected = _filterDefinition == null ? true : _filterDefinition.enabled;
                checkbox.toolTip = "Click to disable this filter.";
                checkbox.addEventListener(Event.CHANGE, onChange);
                addChild(checkbox);
            //}

            labelText = new Label();
            labelText.text = FilterDefinition.getLabel(_filterDefinition, _analysisItem);
            addChild(labelText);

            if (editButton == null) {
                editButton = new Button();
                editButton.addEventListener(MouseEvent.CLICK, edit);
                editButton.setStyle("icon", ImageConstants.EDIT_ICON);
                editButton.toolTip = "Edit";
            }
            addChild(editButton);
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
            if (_filterDefinition == null) {
                _filterDefinition = new FilterValueDefinition();
                _filterDefinition.inclusive = true;
                _filterDefinition.field = _analysisItem;
            }
            if (deleteButton != null) {
			    deleteButton.enabled = true;
            }
            if (_loadingFromReport) {
                _loadingFromReport = false;

            } else {
                dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
            }
		}

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
	}
}