package com.easyinsight.analysis.charts
{
	import flash.display.DisplayObject;
	import flash.events.EventDispatcher;
	
	import mx.managers.PopUpManager;
	
	
	[Event (name="chartSort", type="com.easyinsight.analysis.ChartSortEvent")]
	[Event (name="chartAdapterUpdate", type="com.easyinsight.analysis.charts.ChartAdapterUpdateEvent")]
	public class ChartAdapterOption extends EventDispatcher
	{
		private var _chartAdapter:ChartAdapter;		
		private var _threeDSelected:Boolean;
		private var _stackedSelected:Boolean;		
		
		public function ChartAdapterOption() {
		}
		
		public function get chartFamily():int {
			return 0;
		}

		public function get label():String {
			return null;
		}			
		
		public function set threeDSelected(threeDSelected:Boolean):void {
			_threeDSelected = threeDSelected;				
		}
		
		public function set stackedSelected(stackedSelected:Boolean):void {
			_stackedSelected = stackedSelected;
		}
		
		public function get threeDSelected():Boolean {
			return _threeDSelected;			
		}
		
		public function get stackedSelected():Boolean {
			return _stackedSelected;
		}
		
		public function get threeDVisible():Boolean {
			return false;
		}
		
		public function get stackedVisible():Boolean {
			return false;
		}
		
		public function get chartTypeFromSelections():ChartAdapter {
			return null;
		}
		
		public function createDetailEditor(displayObject:DisplayObject, chartType:int):void {
			var chartEditWindow:ChartEditWindow = ChartEditWindow(PopUpManager.createPopUp(displayObject, getDetailEditorClass(), true));
			chartEditWindow.chartType = chartType;
			chartEditWindow.addEventListener(ChartAdapterUpdateEvent.CHART_UPDATE, updateAdapter);			
			PopUpManager.centerPopUp(chartEditWindow);			
		}
		
		private function updateAdapter(event:ChartAdapterUpdateEvent):void {
			this._chartAdapter = event.chartAdapter;
			dispatchEvent(event);
		}
		
		protected function getDetailEditorClass():Class {
			return null;
		}
		
		public function get icon():Class {
			return null;
		}
		
		public function createChartAdapter(chartType:int):ChartAdapter {
			return instantiateChartAdapter(chartType);
		}
		
		protected function instantiateChartAdapter(chartType:int):ChartAdapter {
			return null;
		}
		
		public function changeAwayFrom():void {
			if (_chartAdapter != null) {
				cleanup(_chartAdapter);
			}
		}
		
		protected function cleanup(chartAdapter:ChartAdapter):void {
			
		}
		
		public function createLimitLabel(limitResults:int, maxResults:int):String {
			return "Displaying " + maxResults + " of " + limitResults + " results.";
		}
	}
}