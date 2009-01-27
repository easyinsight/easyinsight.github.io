package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisDimensionResultMetadata;
	import com.easyinsight.analysis.AnalysisItem;
	
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.Button;
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
		
		override protected function createChildren():void {
			super.createChildren();
			if (labelText == null) {
				labelText = new Label();
				labelText.text = _filterDefinition.field.display;							
			}
			addChild(labelText);
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
		
		override protected function commitProperties():void {
			super.commitProperties();						
			dataService = new RemoteObject();
			dataService.destination = "data";
			dataService.getAnalysisItemMetadata.addEventListener(ResultEvent.RESULT, gotMetadata);
			dataService.getAnalysisItemMetadata.send(_feedID, _analysisItem);
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
			deleteButton.enabled = true;
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
		
		public function set filterDefinition(filterDefinition:FilterDefinition):void {
			_filterDefinition = filterDefinition as FilterValueDefinition; 	
		}
	}
}