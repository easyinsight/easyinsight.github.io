package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.ComplexAnalysisMeasure")]
	public class ComplexAnalysisMeasure extends AnalysisMeasure
	{
		public var wrappedAggregation:int;
		
		public function ComplexAnalysisMeasure(aggregation:int=1, wrappedAggregation:int = 0)
		{
			super(aggregation);
			this.wrappedAggregation = wrappedAggregation;
		}
		
		override public function getType():int {
			return super.getType() | AnalysisItemTypes.COMPLEX_MEASURE;	
		}
	}
}