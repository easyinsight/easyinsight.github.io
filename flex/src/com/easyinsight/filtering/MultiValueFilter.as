package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisDimensionResultMetadata;
	import com.easyinsight.analysis.AnalysisItem;

import com.easyinsight.framework.CredentialsCache;

import flash.events.Event;
import flash.events.MouseEvent;
	import flash.geom.Point;
	
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
		
		[Bindable]
        [Embed(source="../../../../assets/navigate_cross.png")]
        public var deleteIcon:Class;
        
        [Bindable]
        [Embed(source="../../../../assets/pencil.png")]
        public var editIcon:Class;
		
		public function MultiValueFilter(feedID:int, analysisItem:AnalysisItem) {
			super();
			_analysisItem = analysisItem;
			_feedID = feedID;
		}
		
		public function set analysisItems(analysisItems:ArrayCollection):void {
			_analysisItems = analysisItems;
		}		
		
		private function edit(event:MouseEvent):void {
			var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
            window.feedID = _feedID;
			window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit);
            window.detailClass = MultiValueFilterWindow;
			window.analysisItems = _analysisItems;
			window.filterDefinition = _filterDefinition;			
			PopUpManager.addPopUp(window, this, true);
			window.x = 50;
			window.y = 50;
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
            if (!_filterEditable) {
                var checkbox:CheckBox = new CheckBox();
                checkbox.selected = true;
                checkbox.toolTip = "Click to disable this filter.";
                checkbox.addEventListener(Event.CHANGE, onChange);
                addChild(checkbox);
            }
            if (_showLabel) {
                if (labelText == null) {
                    labelText = new Label();
                    labelText.text = _filterDefinition.field.display + ":";
                }
                addChild(labelText);
            } else {
                this.toolTip = _filterDefinition.field.display + ":";
            }
            if (_filterEditable) {
                if (editButton == null) {
                    editButton = new Button();
                    editButton.addEventListener(MouseEvent.CLICK, edit);
                    editButton.setStyle("icon", editIcon);
                    editButton.toolTip = "Edit";
                }
                addChild(editButton);
                if (deleteButton == null) {
                    deleteButton = new Button();
                    deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
                    deleteButton.setStyle("icon", deleteIcon);
                    deleteButton.toolTip = "Delete";
                    deleteButton.enabled = false;
                }
                addChild(deleteButton);
            }
		}
		
		override protected function commitProperties():void {
			super.commitProperties();						
			dataService = new RemoteObject();
			dataService.destination = "data";
			dataService.getAnalysisItemMetadata.addEventListener(ResultEvent.RESULT, gotMetadata);
			dataService.getAnalysisItemMetadata.send(_feedID, _analysisItem, CredentialsCache.getCache().createCredentials(), new Date().getTimezoneOffset());
		}
		
		private function gotMetadata(event:ResultEvent):void {
			var analysisDimensionResultMetadata:AnalysisDimensionResultMetadata = dataService.getAnalysisItemMetadata.lastResult as 
				AnalysisDimensionResultMetadata;
			/*var strings:ArrayCollection = new ArrayCollection();
			for each (var value:Value in analysisDimensionResultMetadata.values) {
				var string:String = String(value.getValue());
				if (!strings.contains(string)) {
					strings.addItem(string);
				}
			}
			var sort:Sort = new Sort();
			strings.sort = sort;
			strings.refresh();			
			comboBox.dataProvider = strings;
			comboBox.rowCount = Math.min(strings.length, 15);
			var selectedValue:String;
			if (_filterDefinition.filteredValues.length == 0) {
				_filterDefinition.filteredValues.addItem(strings.getItemAt(0));
			}
			selectedValue = _filterDefinition.filteredValues.getItemAt(0) as String;							
			comboBox.selectedItem = selectedValue;
			comboBox.enabled = true;
			deleteButton.enabled = true;*/
            if (deleteButton != null) {
			    deleteButton.enabled = true;
            }
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
		}
		
		public function toInclusive(filterValues:ArrayCollection):void {
			_filterDefinition.inclusive = true;
			_filterDefinition.filteredValues = filterValues;
			//updateState();	
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