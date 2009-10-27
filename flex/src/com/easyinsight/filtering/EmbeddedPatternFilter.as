package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisItem;



import flash.events.Event;
import flash.events.MouseEvent;	
	

	import mx.containers.HBox;
	import mx.controls.Button;
import mx.controls.CheckBox;
import mx.controls.Label;

	import mx.rpc.remoting.RemoteObject;

	public class EmbeddedPatternFilter extends HBox implements IEmbeddedFilter
	{
		private var _filterDefinition:FilterPatternDefinition;
		private var _analysisItem:AnalysisItem;
		private var _feedID:int;		
		private var deleteButton:Button;
		private var editButton:Button;
		private var labelText:Label;
		private var dataService:RemoteObject;
		
		public function EmbeddedPatternFilter(feedID:int, analysisItem:AnalysisItem) {
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
            if (_filterDefinition == null) {
                _filterDefinition = new FilterPatternDefinition();
                _filterDefinition.field = _analysisItem;
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
            dispatchEvent(new EmbeddedFilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
		}
		
		private function deleteSelf(event:MouseEvent):void {
			dispatchEvent(new EmbeddedFilterDeletionEvent(this));
		}
		
		public function get filterDefinition():FilterDefinition {
			return _filterDefinition;
		}

        private var _filterEditable:Boolean = true;

        public function set filterEditable(editable:Boolean):void {
            _filterEditable = editable;
        }
		
		public function set filterDefinition(filterDefinition:FilterDefinition):void {
			_filterDefinition = filterDefinition as FilterPatternDefinition; 	
		}

        private var _showLabel:Boolean;

        public function set showLabel(show:Boolean):void {
            _showLabel = show;
        }
	}
}