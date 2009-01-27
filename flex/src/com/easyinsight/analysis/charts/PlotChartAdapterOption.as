package com.easyinsight.analysis.charts
{
	public class PlotChartAdapterOption extends ChartAdapterOption
	{
		[Bindable]
        [Embed(source="../../../../../assets/chart_bubble.png")]
        public var bubbleChartIcon:Class;
        
		public function PlotChartAdapterOption()
		{
			super();
		}
		
		override public function get icon():Class {
			return bubbleChartIcon;
		}
		
		override public function get label():String {
			return "Plot";
		}
		
		override public function get chartFamily():int {
			return ChartTypes.PLOT_FAMILY;
		}
		
		override protected function instantiateChartAdapter(chartType:int):ChartAdapter {
			var plotChartAdapter:PlotChartAdapter = new PlotChartAdapter();
			return plotChartAdapter;		
		}
		
		override public function get chartTypeFromSelections():ChartAdapter {
			var plotChartAdapter:PlotChartAdapter = new PlotChartAdapter();
			return plotChartAdapter;
		}
	}
}