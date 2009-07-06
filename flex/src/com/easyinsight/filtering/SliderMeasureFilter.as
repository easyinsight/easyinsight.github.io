package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.AnalysisItemResultMetadata;
	import com.easyinsight.analysis.AnalysisMeasureResultMetadata;

import com.easyinsight.framework.CredentialsCache;

import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.controls.HSlider;
	import mx.controls.Label;
	import mx.controls.Text;
	import mx.controls.TextInput;
	import mx.managers.PopUpManager;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class SliderMeasureFilter extends HBox implements IFilter
	{
		private var dataService:RemoteObject;
		private var hslider:HSlider;
		private var _filterDefinition:FilterRangeDefinition;
		private var analysisItem:AnalysisItem;
		private var lowValue:int;
		private var highValue:int;
		private var lowField:Label;
		private var highField:Label;
		
		private var lowInput:TextInput;
		private var highInput:TextInput;
		
		private var _analysisItems:ArrayCollection;
		
		[Embed(source="../../../../assets/navigate_cross.png")]
        public var deleteIcon:Class;
        
        [Bindable]
        [Embed(source="../../../../assets/pencil.png")]
        public var editIcon:Class;
		
		public function SliderMeasureFilter(feedID:int, analysisItem:AnalysisItem) {
			super();
			this.analysisItem = analysisItem;
			dataService = new RemoteObject();
			dataService.destination = "data";
			dataService.getAnalysisItemMetadata.addEventListener(ResultEvent.RESULT, gotMetadata);
			dataService.getAnalysisItemMetadata.send(feedID, analysisItem, CredentialsCache.getCache().createCredentials());
		}
		
		public function set analysisItems(analysisItems:ArrayCollection):void {
			_analysisItems = analysisItems;
		}		
		
		private function edit(event:MouseEvent):void {
			var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
			window.detailClass = MeasureFilterEditor;
			window.addEventListener(FilterEditEvent.FILTER_EDIT, onFilterEdit);
			window.analysisItems = _analysisItems;
			window.filterDefinition = _filterDefinition;
			PopUpManager.addPopUp(window, this, true);
			var point:Point = new Point();
			point.x = 0;
			point.y = 0;
			point = this.localToGlobal(point);
			window.x = point.x + 25;
			window.y = point.y + 25;
		}		
		
		private function onFilterEdit(event:FilterEditEvent):void {
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, event.filterDefinition, event.previousFilterDefinition, this));
		}
		
		private function gotMetadata(event:ResultEvent):void {
			var metadata:AnalysisItemResultMetadata = dataService.getAnalysisItemMetadata.lastResult as AnalysisItemResultMetadata;
			var measureMetadata:AnalysisMeasureResultMetadata = metadata as AnalysisMeasureResultMetadata;
            if (_showLabel) {
                var label:Label = new Label();
                label.text = analysisItem.display + ":";
                addChild(label);
            } else {
                toolTip = analysisItem.display;
            }
			lowInput = new TextInput();
			lowInput.editable = false;
			addChild(lowInput);
			
			var between:Text = new Text();
			between.text = " < " + analysisItem.display + " < ";
			addChild(between);
			
			highInput = new TextInput();
			highInput.editable = false;
			addChild(highInput);

            if (_filterEditable) {
                var editButton:Button = new Button();
                editButton.addEventListener(MouseEvent.CLICK, edit);
                editButton.setStyle("icon", editIcon);
                editButton.toolTip = "Edit";
                addChild(editButton);
                var deleteButton:Button = new Button();
                deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
                deleteButton.setStyle("icon", deleteIcon);
                deleteButton.toolTip = "Delete";
            }
			
			addChild(deleteButton);
			
			if (_filterDefinition == null) {
				_filterDefinition = new FilterRangeDefinition();
				_filterDefinition.startValueDefined = false;
				_filterDefinition.endValueDefined = false;
				_filterDefinition.field = analysisItem;
					
			} else {
				if (_filterDefinition.startValueDefined) {
					lowInput.text = String(_filterDefinition.startValue);
				}
				if (_filterDefinition.endValueDefined) {
					highInput.text = String(_filterDefinition.endValue);
				}
			}
			
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));
		}
		
		public function set filterDefinition(filterDefinition:FilterDefinition):void {
			this._filterDefinition = filterDefinition as FilterRangeDefinition;
		}

        private var _filterEditable:Boolean = true;

        public function set filterEditable(editable:Boolean):void {
            _filterEditable = editable;
        }
		
		public function get filterDefinition():FilterDefinition {
			return this._filterDefinition;
		}
		
		private function deleteSelf(event:MouseEvent):void {
			dispatchEvent(new FilterDeletionEvent(this));
		}

        private var _showLabel:Boolean;

        public function set showLabel(show:Boolean):void {
            _showLabel = show;
        }
	}
}