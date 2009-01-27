package com.easyinsight.analysis.options {
import analysis.AggregationTypes;
import analysis.AnalysisItem;
import analysis.AnalysisMeasure;
	
    public class SumOption extends AnalysisItemOption {
    	
    	public function SumOption() {
			super();
		}
		
		override protected function getLabelValue():String {
			return OptionTypeNames.SUM;
		}
		
		override public function createAnalysisItem(key:String):AnalysisItem {
			var analysisMeasure:AnalysisMeasure = new AnalysisMeasure();
			analysisMeasure.key = key;
			analysisMeasure.aggregation = AggregationTypes.SUM;
			return analysisMeasure; 
		}	
    }
}
