package com.easyinsight.analysis.charts
{
	import com.easyinsight.analysis.ChartSortEvent;
	
	
	public class ColumnChartAdapterOption extends ChartAdapterOption
	{
		private var chartSubType:int;
		
		[Bindable]
        [Embed(source="../../../../../assets/chart_column.png")]
        public var columnChartIcon:Class;
		
		public function ColumnChartAdapterOption()
		{
			super();
		}
		
		override public function get icon():Class {
			return columnChartIcon;
		}
		
		override public function get label():String {
			return "Column";
		}
		
		override public function get chartFamily():int {
			return ChartTypes.COLUMN_FAMILY;
		}
		
		override protected function getDetailEditorClass():Class {
			return ColumnChartEditWindow;
		}
		
		override protected function instantiateChartAdapter(chartType:int):ChartAdapter {
			var chartAdapter:ChartAdapter;
			switch (chartType) {
				case 0:
				case ChartTypes.COLUMN_2D:
					var columnChartAdaptor:ColumnChartAdapter = new ColumnChartAdapter();
					columnChartAdaptor.addEventListener(ChartSortEvent.CHART_SORT, onChartSort);
					chartAdapter = columnChartAdaptor;
					break;		
			}
			
			return chartAdapter;		
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
		
		override public function get stackedVisible():Boolean {
			return true;
		}
		
		override public function get chartTypeFromSelections():ChartAdapter {
			var chartAdapter:ChartAdapter;
			if (stackedSelected) {
				chartAdapter = new StackedColumnChartAdapter();				
			} else {
				if (threeDSelected) {
					chartAdapter = new Column3DChartAdapter();
				} else {
					chartAdapter = new ColumnChartAdapter();
				}
			}
			chartAdapter.addEventListener(ChartSortEvent.CHART_SORT, onChartSort);
			return chartAdapter;
		}
	}
}