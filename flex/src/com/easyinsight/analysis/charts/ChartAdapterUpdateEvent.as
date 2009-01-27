package com.easyinsight.analysis.charts
{
	import flash.events.Event;

	public class ChartAdapterUpdateEvent extends Event
	{
		public static const CHART_UPDATE:String = "chartAdapterUpdate";
		public var chartAdapter:ChartAdapter;
		
		public function ChartAdapterUpdateEvent(chartAdapter:ChartAdapter)
		{
			super(CHART_UPDATE);
			this.chartAdapter = chartAdapter;			
		}
		
		override public function clone():Event {
			return new ChartAdapterUpdateEvent(chartAdapter);
		}
	}
}