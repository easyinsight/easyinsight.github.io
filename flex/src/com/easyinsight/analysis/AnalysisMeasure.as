package com.easyinsight.analysis
{
	import com.easyinsight.analysis.conditions.ConditionRenderer;
	import com.easyinsight.analysis.conditions.MeasureConditionRange;
	import com.easyinsight.analysis.conditions.StatefulConditionRenderer;
	
	import mx.formatters.Formatter;
	import mx.formatters.NumberFormatter;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.AnalysisMeasure")]
	public class AnalysisMeasure extends AnalysisItem
	{
		public var aggregation:int;
		public var measureConditionRange:MeasureConditionRange;
		public var analysisMeasureID:int;
		
		public function AnalysisMeasure(aggregation:int = 1)
		{
			super();
			this.aggregation = aggregation;
		}
		
		override protected function getQualifiedSuffix():String {
			return getType() + ":" + aggregation + ":" + display;
		}
		
		override public function getType():int {
			return AnalysisItemTypes.MEASURE;	
		}
		
		override public function createClientRenderer():ConditionRenderer {
			if (measureConditionRange == null) {
				return super.createClientRenderer();
			} else {
				return new StatefulConditionRenderer(measureConditionRange);
			}
		}
		
		/*override public function getFormatter():Formatter {
			var numberFormatter:NumberFormatter = new NumberFormatter();
			numberFormatter.precision = 2;			
			return numberFormatter;			
		}*/
	}
}