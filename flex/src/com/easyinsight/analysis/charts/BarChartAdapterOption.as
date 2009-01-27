package com.easyinsight.analysis.charts
{
	import com.easyinsight.analysis.ChartSortEvent;
	
	public class BarChartAdapterOption extends ChartAdapterOption
	{
		[Bindable]
        [Embed(source="../../../../../assets/chart_bar.png")]
        public var barChartIcon:Class;
		
		public function BarChartAdapterOption()
		{
			super();
		}
		
		override public function get icon():Class {
			return barChartIcon;
		}
		
		override public function get label():String {
			return "Bar";
		}
		
		override public function get chartFamily():int {
			return ChartTypes.BAR_FAMILY;
		}
		
		override protected function instantiateChartAdapter(chartType:int):ChartAdapter {
			var barChartAdaptor:BarChartAdapter = new BarChartAdapter();
			barChartAdaptor.addEventListener(ChartSortEvent.CHART_SORT, onChartSort);
			return barChartAdaptor;		
		}
		
		private function onChartSort(event:ChartSortEvent):void {
			dispatchEvent(event);
		}
		
		override protected function cleanup(chartAdapter:ChartAdapter):void {
			chartAdapter.removeEventListener(ChartSortEvent.CHART_SORT, onChartSort);			
		}
		
		override public function get threeDVisible():Boolean {
			return true;
		}
		
		override public function get chartTypeFromSelections():ChartAdapter {
			var chartAdapter:ChartAdapter;
			if (threeDSelected) {
				chartAdapter = new Bar3DChartAdapter();
			} else {
				chartAdapter = new BarChartAdapter();
			}
			chartAdapter.addEventListener(ChartSortEvent.CHART_SORT, onChartSort);
			return chartAdapter;
		}
	}
}