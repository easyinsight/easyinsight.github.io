package com.easyinsight.analysis
{	
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.AnalysisMeasure")]
	public class AnalysisMeasure extends AnalysisItem
	{
		public var aggregation:int;
		public var analysisMeasureID:int;
        public var rowCountField:Boolean;
		
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
	}
}