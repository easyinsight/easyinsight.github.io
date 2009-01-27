package com.easyinsight.analysis
{
	import flash.events.Event;

	public class ChartSortEvent extends Event
	{
		public static const CHART_SORT:String = "chartSort";
		
		public var analysisItem:AnalysisItem;
		public var sortState:int;
		
		public function ChartSortEvent(analysisItem:AnalysisItem, sortState:int) {
			super(CHART_SORT, true);
			this.analysisItem = analysisItem;
			this.sortState = sortState;
		}
		
		override public function clone():Event {
			return new ChartSortEvent(analysisItem, sortState);
		}
	}
}