package com.easyinsight.map
{
	import com.easyinsight.analysis.AggregationTypes;
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.AnalysisItemTypes;
	import com.easyinsight.analysis.AnalysisMeasure;
	import com.easyinsight.analysis.ListDropAreaGrouping;	

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
	    }
	}
}