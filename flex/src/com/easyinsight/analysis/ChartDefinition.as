package com.easyinsight.analysis
{
	import com.easyinsight.analysis.charts.ChartTypes;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.WSChartDefinition")]
	public class ChartDefinition extends AnalysisDefinition
	{
		public var chartType:int;
		public var chartFamily:int;
				
		public var chartDefinitionID:Number;
		
		public var limitsMetadata:LimitsMetadata;
	
		public function ChartDefinition()
		{
            chartType = getChartType();
            chartFamily = getChartFamily();
		}

        public function getChartType():int {
            return 0;
        }

        public function getChartFamily():int {
            return 0;
        }

        override public function createDefaultLimits():void {
            var limitsMetadata:LimitsMetadata = new LimitsMetadata();
            var limitNumber:int;
            switch (chartFamily) {
                case ChartTypes.COLUMN_FAMILY:
                case ChartTypes.BAR_FAMILY:
                case ChartTypes.PIE_FAMILY:
                    limitNumber = 15;
                    break;
                case ChartTypes.PLOT_FAMILY:
                case ChartTypes.BUBBLE_FAMILY:
                    limitNumber = 100;
                    break;
                case ChartTypes.LINE_FAMILY:
                case ChartTypes.AREA_FAMILY:
                    limitNumber = 1000;
                    break;
            }
            limitsMetadata.number = limitNumber;
            limitsMetadata.top = true;
            this.limitsMetadata = limitsMetadata;
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