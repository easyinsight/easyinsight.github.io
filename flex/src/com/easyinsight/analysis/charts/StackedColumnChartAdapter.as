package com.easyinsight.analysis.charts
{
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.SortableAxisTitleFactory;
	
	public class StackedColumnChartAdapter extends ChartAdapter
	{
		public function StackedColumnChartAdapter()
		{
			super();
		}
		
		import mx.events.FlexEvent;
		import mx.controls.TextInput;
		import mx.binding.utils.BindingUtils;
		import mx.charts.ColumnChart;
		import mx.charts.AxisRenderer;
		import com.easyinsight.filtering.FilterRawData;
		import mx.graphics.SolidColor;
		import mx.graphics.IFill;
		import com.easyinsight.analysis.conditions.ConditionRenderer;
		import mx.charts.chartClasses.IAxis;
		import mx.charts.LinearAxis;
		import mx.formatters.Formatter;
		import mx.charts.series.items.ColumnSeriesItem;
		import mx.charts.chartClasses.Series;
		import mx.charts.ChartItem;
		import mx.charts.CategoryAxis;
		import mx.charts.series.ColumnSeries;
		import mx.collections.ArrayCollection;
		[Bindable]
		private var graphData:ArrayCollection = new ArrayCollection();
		
		private var dimensionItem:AnalysisItem;
		
		[Bindable]
		private var measureName:String;
		
		[Bindable]
		private var xAxisTitle:String = "";
		
		[Bindable]
		private var yAxisTitle:String = "";
		
		[Bindable]
		private var xAxisItem:AnalysisItem;					
		[Bindable]
		private var yAxisItem:AnalysisItem;
		
		[Bindable]
		private var _xField:String;
		[Bindable]
		private var yField:String;
		
		private var measureFormatter:Formatter;
		
		private var customColors:Boolean = false;
		private var conditionRenderer:ConditionRenderer;
		
		private var columnChart:ColumnChart;
		
		private var xAxis:CategoryAxis;
		private var yAxis:LinearAxis;
		
		private var xAxisTitleRendererFactory:SortableAxisTitleFactory;
		
		private var yAxisTitleRendererFactory:SortableAxisTitleFactory;
		
		private var columnSeries:ColumnSeries;
		
		[Bindable]
		public function get xField():String {
			return _xField;
		}
		
		public function set xField(xField:String):void {
			this._xField = xField;
			dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
		}
		
		private function createChart():void {
			if (columnChart == null) {
				columnChart = new ColumnChart();
				columnChart.percentHeight = 100;
				columnChart.percentWidth = 100;
				columnChart.type = "stacked";
				columnChart.showDataTips = true;
				columnChart.dataProvider = graphData;
				
				xAxis = new CategoryAxis();
				BindingUtils.bindProperty(this, "xField", xAxis, "categoryField");					
				//xAxis.categoryField = xField;
				xAxis.dataProvider = graphData;
				xAxis.title = xAxisTitle;
				columnChart.horizontalAxis = xAxis;
				var axisRenderer:AxisRenderer = new AxisRenderer();
		        axisRenderer.axis = xAxis;
		        xAxisTitleRendererFactory = new SortableAxisTitleFactory(xAxisItem, true);
		        axisRenderer.titleRenderer = xAxisTitleRendererFactory;
		        axisRenderer.setStyle("color", "#FFFFFF");
		        axisRenderer.placement = "bottom";
		        columnChart.horizontalAxisRenderers = [ axisRenderer ];
		        
		        yAxis = new LinearAxis();
		        yAxis.title = yAxisTitle;
		        yAxis.labelFunction = renderAxis;
		        columnChart.verticalAxis = yAxis;
		        var yAxisRenderer:AxisRenderer = new AxisRenderer();
		        yAxisRenderer.axis = yAxis;
		        yAxisRenderer.setStyle("color", "#FFFFFF");
		        yAxisTitleRendererFactory = new SortableAxisTitleFactory(yAxisItem, false); 
		        yAxisRenderer.titleRenderer = yAxisTitleRendererFactory;
		        yAxisRenderer.placement = "left";
		        columnChart.verticalAxisRenderers = [ yAxisRenderer ];
		        
				columnSeries = new ColumnSeries();
				columnSeries.xField = xField;
				columnSeries.yField = yField;					
				columnSeries.labelFunction = renderChartLabel;
				columnSeries.dataProvider = graphData;
				columnSeries.displayName = xAxisTitle;
				//columnSeries.setStyle("showDataEffect", interpolateIn);
				var mySeries:Array = new Array();
				columnChart.series = mySeries;
				mySeries.push(columnSeries);					
				addChild(columnChart);
			}	
		}
		
		override public function get chartType():int {
			return ChartTypes.COLUMN_2D_STACKED;
		}
		
		override public function dataChange(dataSet:ArrayCollection, dimensions:Array, measures:Array):void {
			
			createChart();
			
			if (measures.length == 1) {
				measureFormatter = measures[0].getFormatter();
				var conditionRenderer:ConditionRenderer = measures[0].createClientRenderer();
				if (conditionRenderer.hasCustomColor()) {
					customColors = true;
					this.conditionRenderer = conditionRenderer;
				}
			}
			
			var newXAxisItem:AnalysisItem = dimensions[0];
			if (xAxisItem == null || newXAxisItem.key.createString() != xAxisItem.key.createString()) {
				xAxisItem = newXAxisItem;
				xAxisTitleRendererFactory.analysisItem = xAxisItem;
			}
			
			var newYAxisItem:AnalysisItem = measures[0];
			if (yAxisItem == null || newYAxisItem.key.createString() != yAxisItem.key.createString()) {
				yAxisItem = newYAxisItem;
				yAxisTitleRendererFactory.analysisItem = yAxisItem;
			}

			var newXField:String = dimensions[0].key.createString();
			if (newXField != xField) {
				xField = newXField;				
				xAxis.categoryField = xField;
				columnSeries.xField = xField;
			}
			
			var newXAxisTitle:String = dimensions[0].display;
			if (newXAxisTitle != xAxisTitle) {
				xAxisTitle = newXAxisTitle;
				xAxis.title = xAxisTitle;
				columnSeries.displayName = xAxisTitle;
			}
			
			var newYField:String = measures[0].key.createString();
			if (newYField != yField) {
				yField = newYField;
				columnSeries.yField = yField;
			}
			
			var newYAxisTitle:String = measures[0].display;
			if (newYAxisTitle != yAxisTitle) {
				yAxisTitle = newYAxisTitle;
				yAxis.title = yAxisTitle;					
			}		
			
			
			
			columnChart.dataProvider = dataSet;
			columnSeries.dataProvider = dataSet;
			xAxis.dataProvider = dataSet;
			graphData = dataSet;		
			var mySeries:Array = new Array();
			
			if (dimensions.length == 2) {
				
				var uniques:ArrayCollection = new ArrayCollection();
				var seriesData:Object = new Object();
				for (var i:int = 0; i < dataSet.length; i++) {
					var object:Object = dataSet.getItemAt(i);
					var dimensionValue:String = object[dimensions[1].key.createString()];
					var newSeriesData:ArrayCollection = seriesData[dimensionValue];
					if (newSeriesData == null) {
						newSeriesData = new ArrayCollection();
						seriesData[dimensionValue] = newSeriesData;
					}
					var newObject:Object = new Object();
					newObject[dimensions[0].key.createString()] = object[dimensions[0].key.createString()];
					newObject[dimensionValue] = object[measures[0].key.createString()];
					newSeriesData.addItem(newObject);
					if (!uniques.contains(dimensionValue)) {
						uniques.addItem(dimensionValue);
					}
				}
				for (i = 0; i < uniques.length; i++) {
					var key:String = uniques.getItemAt(i) as String;
					var uniqueColumnSeries:ColumnSeries = new ColumnSeries();
					uniqueColumnSeries.xField = dimensions[0].key.createString();
					uniqueColumnSeries.yField = key;
					uniqueColumnSeries.dataProvider = seriesData[key];
					uniqueColumnSeries.labelFunction = renderChartLabel; 
					mySeries.push(uniqueColumnSeries);	
				}
				var stackedAxis:CategoryAxis = new CategoryAxis();
		        stackedAxis.categoryField = newXField;
		        stackedAxis.dataProvider = seriesData[uniques.getItemAt(0)] as ArrayCollection;
		        columnChart.dataProvider = seriesData[uniques.getItemAt(0)] as ArrayCollection; 			        
		        columnChart.horizontalAxis = stackedAxis;
		        var stackedLinearAxis:LinearAxis = new LinearAxis();
		        stackedLinearAxis.labelFunction = renderAxis;
		        columnChart.verticalAxis = stackedLinearAxis;					
			}
			
			columnChart.series = mySeries;
			removeAllChildren();
			addChild(columnChart);
			
			graphData = dataSet;
		}
		
		private function renderChartLabel(element:ChartItem, series:Series):String {
			var columnSeriesItem:ColumnSeriesItem = element as ColumnSeriesItem;
			return measureFormatter.format(columnSeriesItem.xNumber);
		}
		
		private function customFill(element:ChartItem, index:Number):IFill {
			var item:ColumnSeriesItem = element as ColumnSeriesItem;
			var color:SolidColor = new SolidColor(conditionRenderer.getColor(item.yValue));
			return color;
		}
		
		private function renderAxis(labelValue:Object, previousValue:Object, axis:IAxis):String {
			return measureFormatter.format(labelValue);
		}
		
		override public function getMaxMeasures():int {
			return 1;
		}
		
		override public function getMaxDimensions():int {
			return 2;
		}
		
		override public function createFilterRawData():FilterRawData {
			var filterRawData:FilterRawData = new FilterRawData();
			for (var i:int = 0; i < columnChart.selectedChartItems.length; i++) {
            	var obj:ChartItem = columnChart.selectedChartItems[i];
				filterRawData.addPair(dimensionItem, obj.item[dimensionItem.key.createString()]);                	
            }
			return filterRawData;
		}
	}
}