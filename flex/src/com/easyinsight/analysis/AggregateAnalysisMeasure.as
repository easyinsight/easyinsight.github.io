package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.AggregateAnalysisMeasure")]
	public class AggregateAnalysisMeasure extends AnalysisMeasure
	{
		public var wrappedAggregation:int;
        public var ourAggregation:int;

		public function AggregateAnalysisMeasure(aggregation:int=1, wrappedAggregation:int = 0)
		{
			super(aggregation);
			this.wrappedAggregation = wrappedAggregation;
		}
		
		override public function getType():int {
			return super.getType() | AnalysisItemTypes.COMPLEX_MEASURE;	
		}
	}
}