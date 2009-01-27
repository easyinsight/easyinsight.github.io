package com.easyinsight.analysis.charts
{
	import mx.containers.TitleWindow;

	public class ChartEditWindow extends TitleWindow
	{
		private var _chartType:int;
		
		public function ChartEditWindow()
		{
			super();
		}
		
		public function set chartType(chartType:int):void {
			this._chartType = chartType;
		}
		
		public function get chartType():int {
			return this._chartType;
		}
	}
}