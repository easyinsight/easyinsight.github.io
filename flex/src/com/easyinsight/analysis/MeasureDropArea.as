package com.easyinsight.analysis
{
	import mx.controls.ComboBox;
	
	public class MeasureDropArea extends DropArea
	{
		private var aggregationBox:ComboBox;
		
		public function MeasureDropArea()
		{
			super();		
		}
		
		override public function getDropAreaType():String {
			return "Measure";
		}
		
		override public function getItemEditorClass():Class {
			return MeasureItemEditor;
		}
		
		override protected function getNoDataLabel():String {
			return "Drop Measure Here";
		}

        override public function set analysisItem(analysisItem:AnalysisItem):void {

            if (analysisItem != null) {
                if (!analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                    var analysisMeasure:AnalysisMeasure = new AnalysisMeasure();
                    analysisMeasure.key = analysisItem.key;
                    analysisMeasure.displayName = analysisItem.displayName;
                    analysisMeasure.aggregation = AggregationTypes.SUM;
                    analysisItem = analysisMeasure;
                }
            }
            
            super.analysisItem = analysisItem;

        }
	}
}