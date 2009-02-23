package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.AnalysisRangeDimension")]
	public class AnalysisRangeDimension extends AnalysisDimension
	{
		public function AnalysisRangeDimension()
		{
			super();
		}
		
		override public function getType():int {
			return super.getType() | AnalysisItemTypes.RANGE;	
		}
	}
}