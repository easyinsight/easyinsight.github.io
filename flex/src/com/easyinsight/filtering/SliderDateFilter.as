package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisDateDimensionResultMetadata;
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.AnalysisItemResultMetadata;
	
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.controls.DateField;
	import mx.controls.HSlider;
	import mx.events.CalendarLayoutChangeEvent;
	import mx.events.SliderEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class SliderDateFilter extends HBox implements IFilter
	{
		private var dataService:RemoteObject;
		private var hslider:HSlider;
		private var lowField:DateField;
		private var highField:DateField;
		private var lowDate:Date;
		private var highDate:Date;
		private var delta:Number;
		private var analysisItem:AnalysisItem;
		private var _filterDefinition:FilterDateRangeDefinition;
		private var _analysisItems:ArrayCollection;
		
		[Bindable]
        [Embed(source="../../../../assets/navigate_cross.png")]
        public var deleteIcon:Class;
        
        [Bindable]
        [Embed(source="../../../../assets/pencil.png")]
        public var editIcon:Class;
		
		public function SliderDateFilter(feedID:int, analysisItem:AnalysisItem) {
			super();
			this.analysisItem = analysisItem;
			dataService = new RemoteObject();
			dataService.destination = "data";
			dataService.getAnalysisItemMetadata.addEventListener(ResultEvent.RESULT, gotMetadata);
			dataService.getAnalysisItemMetadata.send(feedID, analysisItem);
		}
		
		private function gotMetadata(event:ResultEvent):void {
			var metadata:AnalysisItemResultMetadata = dataService.getAnalysisItemMetadata.lastResult as AnalysisItemResultMetadata;
			var dateMetadata:AnalysisDateDimensionResultMetadata = metadata as AnalysisDateDimensionResultMetadata;
			this.lowDate = dateMetadata.earliestDate;
			this.highDate = dateMetadata.latestDate;
			this.delta = highDate.valueOf() - lowDate.valueOf();
			hslider = new HSlider();
			hslider.thumbCount = 2;
			hslider.liveDragging = false;
			hslider.minimum = 0;
			hslider.maximum = 100;
			hslider.values = [0, 100];
			hslider.addEventListener(SliderEvent.THUMB_RELEASE, thumbRelease);
			lowField = new DateField();
			lowField.selectedDate = dateMetadata.earliestDate;
			lowField.addEventListener(CalendarLayoutChangeEvent.CHANGE, lowDateChange);
			highField = new DateField();
			highField.selectedDate = dateMetadata.latestDate;
			highField.addEventListener(CalendarLayoutChangeEvent.CHANGE, highDateChange);
			addChild(lowField);
			addChild(hslider);
			addChild(highField);
			if (_filterDefinition == null) {
				_filterDefinition = new FilterDateRangeDefinition();
				_filterDefinition.startDate = dateMetadata.earliestDate;
				_filterDefinition.endDate = dateMetadata.latestDate;
				_filterDefinition.field = analysisItem;	
			} else {
				if (_filterDefinition.startDate == null) {
					_filterDefinition.startDate = dateMetadata.earliestDate;
				}
				lowField.selectedDate = _filterDefinition.startDate;
				if (_filterDefinition.endDate == null) {
					_filterDefinition.endDate = dateMetadata.latestDate;
				}
				highField.selectedDate = _filterDefinition.endDate;
				var newLowVal:int = ((lowField.selectedDate.valueOf() - lowDate.valueOf()) / delta) * 100;
				var newHighVal:int = ((highField.selectedDate.valueOf() - lowDate.valueOf()) / delta) * 100;
				hslider.values = [ newLowVal, newHighVal ] ;
			}
			
			var editButton:Button = new Button();
			editButton.addEventListener(MouseEvent.CLICK, edit);
			editButton.setStyle("icon", editIcon);
			editButton.toolTip = "Edit";
			addChild(editButton);
			
			var deleteButton:Button = new Button();
			deleteButton.addEventListener(MouseEvent.CLICK, deleteSelf);
			deleteButton.setStyle("icon", deleteIcon);
			deleteButton.toolTip = "Delete";
			addChild(deleteButton);
			
			
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_ADDED, filterDefinition, null, this));			
		}
		
		public function set analysisItems(analysisItems:ArrayCollection):void {
			_analysisItems = analysisItems;
		}		
		
		private function edit(event:MouseEvent):void {
			var window:GeneralFilterEditSettings = new GeneralFilterEditSettings();
			window.detailClass = DateRangeDetailEditor;
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
		
		private function deleteSelf(event:MouseEvent):void {
			dispatchEvent(new FilterDeletionEvent(this));
		}
		
		private function thumbRelease(event:SliderEvent):void {
			var lowValue:int = hslider.values[0];
			var highValue:int = hslider.values[1];
			
			var newLowDate:Date = new Date();
			var newHighDate:Date = new Date();		
			
			newLowDate.setTime(lowDate.valueOf() + delta * (lowValue / 100));
			newHighDate.setTime(lowDate.valueOf() + delta * (highValue / 100));
			
			_filterDefinition.startDate = newLowDate;
			_filterDefinition.endDate = newHighDate;
			lowField.selectedDate = newLowDate;
			highField.selectedDate = newHighDate;			
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, filterDefinition, null, this));
		}
		
		private function lowDateChange(event:CalendarLayoutChangeEvent):void {
			var newLowVal:int = ((event.newDate.valueOf() - lowDate.valueOf()) / delta) * 100;			
			hslider.values = [ newLowVal, hslider.values[1] ] ;
			_filterDefinition.startDate = event.newDate;
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, filterDefinition, null, this));
		}
		
		private function highDateChange(event:CalendarLayoutChangeEvent):void {
			var newHighVal:int = ((event.newDate.valueOf() - lowDate.valueOf()) / delta) * 100;
			hslider.values = [ hslider.values[0], newHighVal ] ;
			_filterDefinition.endDate = event.newDate;
			dispatchEvent(new FilterUpdatedEvent(FilterUpdatedEvent.FILTER_UPDATED, filterDefinition, null, this));
		}
		
		public function set filterDefinition(filterDefinition:FilterDefinition):void {
			this._filterDefinition = filterDefinition as FilterDateRangeDefinition;			
		}
		
		public function get filterDefinition():FilterDefinition {
			return _filterDefinition;
		}
	}
}