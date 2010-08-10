package com.easyinsight.analysis.options
{
	import com.easyinsight.analysis.AggregationTypes;
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.AnalysisItemTypes;
	import com.easyinsight.analysis.AnalysisMeasure;
	
	public class OptionFactory
	{
		public function OptionFactory()
		{
		}

		public static function getAnalysisItemOption(analysisItem:AnalysisItem):String {
			var option:String;
			if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
				var analysisMeasure:AnalysisMeasure = analysisItem as AnalysisMeasure;
                if (analysisMeasure.hasType(AnalysisItemTypes.SIX_SIGMA_MEASURE)) {
                    option = OptionTypeNames.SIXSIGMA;
                } else if (analysisMeasure.aggregation == AggregationTypes.SUM) {
					option = OptionTypeNames.SUM;
				} else if (analysisMeasure.aggregation == AggregationTypes.AVERAGE) {
					option = OptionTypeNames.AVERAGE;
				} else if (analysisMeasure.aggregation == AggregationTypes.COUNT) {
					option = OptionTypeNames.COUNT;
				} else if (analysisMeasure.aggregation == AggregationTypes.MAX) {
					option = OptionTypeNames.MAX;
				} else if (analysisMeasure.aggregation == AggregationTypes.MIN) {
					option = OptionTypeNames.MIN;
				} else if (analysisMeasure.aggregation == AggregationTypes.NORMALS) {
					option = OptionTypeNames.NORMALS;
				} else if (analysisMeasure.aggregation == AggregationTypes.DELTA) {
					option = OptionTypeNames.DELTA;
				} else if (analysisMeasure.aggregation == AggregationTypes.LAST_VALUE) {
					option = OptionTypeNames.LASTVALUE;
				} else if (analysisMeasure.aggregation == AggregationTypes.PERCENT_CHANGE) {
                    option = OptionTypeNames.PERCENTCHANGE;
                } else if (analysisMeasure.aggregation == AggregationTypes.MEDIAN) {
                    option = OptionTypeNames.MEDIAN;
                } else if (analysisMeasure.aggregation == AggregationTypes.VARIANCE) {
                    option = OptionTypeNames.VARIANCE;
                }
			} else {
                if (analysisItem.hasType(AnalysisItemTypes.STEP)) {
					option = OptionTypeNames.STEP;
				} else if (analysisItem.hasType(AnalysisItemTypes.DATE)) {
					option = OptionTypeNames.DATE;
				} else if (analysisItem.hasType(AnalysisItemTypes.RANGE)) {
					option = OptionTypeNames.RANGE;
				} else if (analysisItem.hasType(AnalysisItemTypes.LIST)) {
					option = OptionTypeNames.LIST;
                } else if (analysisItem.hasType(AnalysisItemTypes.LATITUDE)) {
                    option = OptionTypeNames.LATITUDE;
                } else if (analysisItem.hasType(AnalysisItemTypes.LONGITUDE)) {
                    option = OptionTypeNames.LONGITUDE;
                } else if (analysisItem.hasType(AnalysisItemTypes.ZIP_CODE)) {
                    option = OptionTypeNames.ZIP_CODE;
                } else if (analysisItem.hasType(AnalysisItemTypes.TEXT)) {
                    option = OptionTypeNames.TEXT;
				} else {
					option = OptionTypeNames.GROUPING;
				}
			}
			return option;
		}
	}
}
