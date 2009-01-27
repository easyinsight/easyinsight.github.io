package com.easyinsight.analysis
{
	import com.easyinsight.analysis.charts.ChartTypes;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.WSChartDefinition")]
	public class ChartDefinition extends GraphicDefinition
	{
		public var chartType:int;
		public var chartFamily:int;
				
		public var chartDefinitionID:Number;
		
		public var limitsMetadata:LimitsMetadata;
	
		public function ChartDefinition()
		{
		}
		
		override public function getDataFeedType():String {
			return "Chart";
		}
		
		override public function getLabel():String {
			var label:String;
			switch (chartFamily) {
				case ChartTypes.BAR_FAMILY:
					label = "Bar";
					break;
				case ChartTypes.COLUMN_FAMILY:
					label = "Column";
					break;
				case ChartTypes.LINE_FAMILY:
					label = "Line";
					break;
				case ChartTypes.PLOT_FAMILY:
					label = "Plot";
					break;
				case ChartTypes.PIE_FAMILY:
					label = "Pie";
					break;
			}
			return label;
		}
		
		public function get threeDSelected():Boolean {
			var selected:Boolean = false;
			switch (chartFamily) {
				case ChartTypes.COLUMN_FAMILY:
					selected = chartType == ChartTypes.COLUMN_3D || chartType == ChartTypes.COLUMN_3D_STACKED;
					break;
				case ChartTypes.BAR_FAMILY:
					selected = chartType == ChartTypes.BAR_3D || chartType == ChartTypes.BAR_3D_STACKED;
					break;
				case ChartTypes.PIE_FAMILY:
					selected = chartType == ChartTypes.PIE_3D;
					break;
				case ChartTypes.AREA_FAMILY:
					selected = chartType == ChartTypes.AREA_3D;
					break;
				case ChartTypes.LINE_FAMILY:
					selected = chartType == ChartTypes.LINE_3D;
					break;
			}
			return selected;
		}
		
		public function get stackedSelected():Boolean {
			var selected:Boolean = false;
			switch (chartFamily) {
				case ChartTypes.COLUMN_FAMILY:
					selected = chartType == ChartTypes.COLUMN_2D_STACKED || chartType == ChartTypes.COLUMN_3D_STACKED;
					break;
				case ChartTypes.BAR_FAMILY:
					selected = chartType == ChartTypes.BAR_2D_STACKED || chartType == ChartTypes.BAR_3D_STACKED;
					break;
			}
			return selected;
		}
	}
}