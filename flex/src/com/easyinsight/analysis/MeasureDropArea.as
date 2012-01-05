package com.easyinsight.analysis
{
	
	public class MeasureDropArea extends DropArea
	{
		
		public function MeasureDropArea()
		{
			super();		
		}
		
		override public function getDropAreaType():String {
			return "Measure";
		}
		
		override protected function getNoDataLabel():String {
			return "Drag Metric Here";
		}

        override public function accept(analysisItem:AnalysisItem):Boolean {
            if (analysisItem.hasType(AnalysisItemTypes.DERIVED_GROUPING) || analysisItem.hasType(AnalysisItemTypes.DATE)) {
                return false;
            }
            return true;
        }

        override public function recommend(analysisItem:AnalysisItem):Boolean {
            return (analysisItem.hasType(AnalysisItemTypes.MEASURE));
        }

        override public function set analysisItem(analysisItem:AnalysisItem):void {

            if (analysisItem != null) {
                if (!analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                    var analysisMeasure:AnalysisMeasure = new AnalysisMeasure();
                    analysisMeasure.key = analysisItem.key;
                    analysisMeasure.displayName = analysisItem.displayName;
                    analysisMeasure.aggregation = AggregationTypes.COUNT;
                    analysisMeasure.concrete = analysisItem.concrete;
                    analysisMeasure.filters = analysisItem.filters;
                    analysisItem = analysisMeasure;
                }
            }
            
            super.analysisItem = analysisItem;

        }
	}
}