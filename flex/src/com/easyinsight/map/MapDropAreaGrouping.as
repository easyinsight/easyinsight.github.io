package com.easyinsight.map
{
	import com.easyinsight.analysis.AggregationTypes;
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.AnalysisItemTypes;
	import com.easyinsight.analysis.AnalysisMeasure;
	import com.easyinsight.analysis.ListDropAreaGrouping;
	import com.easyinsight.analysis.conditions.MeasureConditionRange;

	public class MapDropAreaGrouping extends ListDropAreaGrouping
	{
		public function MapDropAreaGrouping()
		{
			super();
		}
		
		override protected function onItem(analysisItem:AnalysisItem, previousItem:AnalysisItem):void {
	    	var analysisMeasure:AnalysisMeasure;
			if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
				analysisMeasure = new AnalysisMeasure();
				analysisMeasure.aggregation = AggregationTypes.SUM;
				analysisMeasure.key = analysisItem.key; 	
			} else {
				analysisMeasure = analysisItem as AnalysisMeasure;
			}
			if (analysisMeasure.measureConditionRange == null) {
				if (previousItem != null && previousItem.hasType(AnalysisItemTypes.MEASURE)) {
					var previousMeasure:AnalysisMeasure = previousItem as AnalysisMeasure;
					var previousRange:MeasureConditionRange = previousMeasure.measureConditionRange;
					var clonedRange:MeasureConditionRange = previousRange.clone();
					analysisMeasure.measureConditionRange = clonedRange;					
				} else {
					var measureConditionRange:MeasureConditionRange = new MeasureConditionRange();
					measureConditionRange.valueRangeType = 2;
					measureConditionRange.highCondition.highColor = 0x33cc33;
					measureConditionRange.highCondition.highValue = 100;
					measureConditionRange.highCondition.lowColor = 0x000000;
					measureConditionRange.highCondition.lowValue = 50;
					measureConditionRange.lowCondition.highColor = 0x000000;
					measureConditionRange.lowCondition.highValue = 50; 
					measureConditionRange.lowCondition.lowColor = 0xff0000;
					measureConditionRange.lowCondition.lowValue = 0;
					analysisMeasure.measureConditionRange = measureConditionRange;
				} 
			}
	    }
	}
}