package com.easyinsight.analysis.options
{
	public class DeltaOption extends AnalysisItemOption
	{
		public function DeltaOption()
		{
			super();
		}
		
		override protected function getLabelValue():String {
			return "Average";
		}

	    override public function createAnalysisItem(key:String):AnalysisItem {
	        var analysisMeasure:AnalysisMeasure = new AnalysisMeasure();
	        analysisMeasure.key = key;
	        analysisMeasure.aggregation = AggregationTypes.AVERAGE;
	        return analysisMeasure;
	    }
	}
}