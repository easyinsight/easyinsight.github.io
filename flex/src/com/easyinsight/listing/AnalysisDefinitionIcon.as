package com.easyinsight.listing
{
	import com.easyinsight.analysis.AnalysisDefinition;
	import com.easyinsight.analysis.ChartDefinition;
	
	import mx.controls.Image;

	public class AnalysisDefinitionIcon extends Image
	{	
        [Embed(source="../../../../assets/table.png")]
        public var listIcon:Class;
                
        [Embed(source="../../../../assets/table2_selection_block.png")]
        public var crosstabIcon:Class;
                
        [Embed(source="../../../../assets/chart_bar.png")]
        public var barChartIcon:Class;
                
        [Embed(source="../../../../assets/chart_column.png")]
        public var columnChartIcon:Class;
                
        [Embed(source="../../../../assets/chart_pie.png")]
        public var pieChartIcon:Class;
        
        [Embed(source="../../../../assets/chart_line.png")]
        public var lineChartIcon:Class;
            
		private var _data:AnalysisDefinition;
		
		public function AnalysisDefinitionIcon()
		{
			super();
		}
		
		override public function set data(value:Object):void {						
			_data = value as AnalysisDefinition;
			var iconClass:Class;
			switch (_data.getDataFeedType()) {
				case "Chart":
					var chartDef:ChartDefinition = _data as ChartDefinition;
					switch (chartDef.chartType) {
						case "Column":
							iconClass = columnChartIcon;
							toolTip = "Column Chart";
							break;
						case "Bar":
							iconClass = barChartIcon;
							toolTip = "Bar Chart";
							break;
						case "Pie":
							iconClass = pieChartIcon;
							toolTip = "Pie Chart";
							break;
						case "Moveable":
							iconClass = lineChartIcon;
							toolTip = "Line Chart";
							break;							
					}
					break;
				case "List":
					iconClass = listIcon;
					toolTip = "List";
					break;
				case "Crosstab":
					iconClass = crosstabIcon;
					toolTip = "Crosstab";
					break;
			}
			this.source = iconClass;
		}
		
		override public function get data():Object {
			return _data;
		}
	}
}