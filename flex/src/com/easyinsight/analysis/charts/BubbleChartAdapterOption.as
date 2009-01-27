package com.easyinsight.analysis.charts
{
	public class BubbleChartAdapterOption extends ChartAdapterOption
	{
		public function BubbleChartAdapterOption()
		{
		}

		override public function get label():String {
			return "Bubble";
		}
		
		override public function get chartFamily():int {
			return ChartTypes.BUBBLE_FAMILY;
		}
		
		override protected function instantiateChartAdapter(chartType:int):ChartAdapter {
			var barChartAdaptor:BubbleChartAdapter = new BubbleChartAdapter();			
			return barChartAdaptor;		
		}
		
		override public function get chartTypeFromSelections():ChartAdapter {
			var chartAdapter:ChartAdapter = new BubbleChartAdapter();			
			return chartAdapter;
		}
	}
}