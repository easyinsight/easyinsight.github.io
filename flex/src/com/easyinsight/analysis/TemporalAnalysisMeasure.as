package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.TemporalAnalysisMeasure")]
	
	public class TemporalAnalysisMeasure extends AnalysisMeasure
	{
		public var wrappedAggregation:int;
		public var analysisDimension:AnalysisDateDimension;
		
		public function TemporalAnalysisMeasure(aggregation:int=1, wrappedAggregation:int = 1, 
			analysisDimension:AnalysisDateDimension=null)
		{
			super(aggregation);
			this.wrappedAggregation = wrappedAggregation;
			this.analysisDimension = analysisDimension;
		}
		
		override public function getType():int {
			return super.getType() | AnalysisItemTypes.TEMPORAL_MEASURE;	
		}
	}
}