package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisDimensionResultMetadata;
	import com.easyinsight.analysis.AnalysisItem;

import com.easyinsight.framework.CredentialsCache;

import flash.events.Event;
import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.Label;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class EmbeddedMultiValueFilter extends HBox implements IEmbeddedFilter
	{
		private var _filterDefinition:FilterValueDefinition;
		private var _analysisItem:AnalysisItem;
		private var _feedID:int;		
		private var deleteButton:Button;
		private var editButton:Button;
		private var labelText:Label;
		private var dataService:RemoteObject;		

		
		public function EmbeddedMultiValueFilter(feedID:int, analysisItem:AnalysisItem) {
			super();
			_analysisItem = analysisItem;
			_feedID = feedID;
		}
		

		
		private function onFilterEdit(event:FilterEditEvent):void {
			dispatchEvent(new EmbeddedFilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this, event.bubbles, event.rebuild));
		}

        private function onChange(event:Event):void {
            var checkbox:CheckBox = event.currentTarget as CheckBox;
            _filterDefinition.enabled = checkbox.selected;
            dispatchEvent(new EmbeddedFilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, _filterDefinition, null, this));
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
            if (_showLabel) {
                if (labelText == null) {
                    labelText = new Label();
                    labelText.text = _analysisItem.display + ":";
                }
                addChild(labelText);
            } else {
                this.toolTip = _analysisItem.display + ":";
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
			dispatchEvent(new EmbeddedFilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
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
			dispatchEvent(new EmbeddedFilterDeletionEvent(this));
		}
		
		public function get filterDefinition():FilterDefinition {
			return _filterDefinition;
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