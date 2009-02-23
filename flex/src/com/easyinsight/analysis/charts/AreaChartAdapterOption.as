package com.easyinsight.analysis.charts
{
	public class AreaChartAdapterOption extends ChartAdapterOption
	{
		public function AreaChartAdapterOption()
		{
			super();
		}
		
		override public function get label():String {
			return "Area";
		}
		
		override public function get chartFamily():int {
			return ChartTypes.AREA_FAMILY;
		}
		
		override public function get threeDVisible():Boolean {
			return true;
		}
		
		override public function get chartTypeFromSelections():ChartAdapter {
			var chartAdapter:ChartAdapter;
			if (threeDSelected) {
				chartAdapter = new Area3DChartAdapter();
			} else {
				chartAdapter = new AreaChartAdapter();
			}			
			return chartAdapter;
		}
	}
}