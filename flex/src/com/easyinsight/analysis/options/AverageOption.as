package com.easyinsight.analysis.options {
import analysis.AggregationTypes;
import analysis.AnalysisItem;
import analysis.AnalysisMeasure;

public class AverageOption extends AnalysisItemOption{
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