package com.easyinsight.analysis.charts
{
	public class LineChartAdapterOption extends ChartAdapterOption
	{
		[Bindable]
        [Embed(source="../../../../../assets/chart_line.png")]
        public var lineChartIcon:Class;
        
		public function LineChartAdapterOption()
		{
			super();
		}

		override public function get icon():Class {
			return lineChartIcon;
		}
		
		override public function get label():String {
			return "Line";
		}
		
		override public function get chartFamily():int {
			return ChartTypes.LINE_FAMILY;
		}
		
		override protected function instantiateChartAdapter(chartType:int):ChartAdapter {
			var lineChartAdapter:LineChartAdapter = new LineChartAdapter();
			return lineChartAdapter;		
		}	
		
		override public function get threeDVisible():Boolean {
			return true;
		}
		
		override public function get chartTypeFromSelections():ChartAdapter {
			var chartAdapter:ChartAdapter;
			if (threeDSelected) {
				chartAdapter = new Line3DChartAdapter();
			} else {
				chartAdapter = new LineChartAdapter();
			}			
			return chartAdapter;
		}	
	}
}