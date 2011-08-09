package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.AnalysisCalculation")]
	public class AnalysisCalculation extends AnalysisMeasure
	{
		public var calculationString:String;
        public var applyBeforeAggregation:Boolean;
        public var recalculateSummary:Boolean;
		
		public function AnalysisCalculation(aggregation:int=1)
		{
			super(aggregation);
		}
		
		override public function getType():int {
			return super.getType() | AnalysisItemTypes.CALCULATION;	
		}
	}
}