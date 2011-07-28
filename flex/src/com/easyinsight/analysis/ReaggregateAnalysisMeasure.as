package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.ReaggregateAnalysisMeasure")]
	public class ReaggregateAnalysisMeasure extends AnalysisMeasure
	{
		public var wrappedMeasure:AnalysisItem;
        public var aggregationItem:AnalysisItem;

		public function ReaggregateAnalysisMeasure(aggregation:int=1)
		{
			super(aggregation);
		}
		
		override public function getType():int {
			return super.getType() | AnalysisItemTypes.REAGGREGATE_MEASURE;
		}
	}
}