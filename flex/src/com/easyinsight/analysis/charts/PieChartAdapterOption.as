package com.easyinsight.analysis.charts
{
	public class PieChartAdapterOption extends ChartAdapterOption
	{
		[Bindable]
        [Embed(source="../../../../../assets/chart_pie.png")]
        public var pieChartIcon:Class;
        
		public function PieChartAdapterOption()
		{
			super();
		}
		
		override public function get icon():Class {
			return pieChartIcon;
		}
		
		override public function get label():String {
			return "Pie";
		}
		
		override public function get chartFamily():int {
			return ChartTypes.PIE_FAMILY;
		}
		
		override protected function instantiateChartAdapter(chartType:int):ChartAdapter {
			var pieChartAdapter:PieChartAdapter = new PieChartAdapter();
			return pieChartAdapter;		
		}
		
		override public function get threeDVisible():Boolean {
			return true;
		}
		
		override public function get chartTypeFromSelections():ChartAdapter {
			var chartAdapter:ChartAdapter;
			if (threeDSelected) {
				chartAdapter = new Pie3DChartAdapter();
			} else {
				chartAdapter = new PieChartAdapter();
			}			
			return chartAdapter;
		}
	}
}