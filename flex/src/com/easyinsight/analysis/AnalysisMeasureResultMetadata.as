package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.AnalysisMeasureResultMetadata")]
	public class AnalysisMeasureResultMetadata extends AnalysisItemResultMetadata
	{
		public var lowestValue:Number;
		public var highestValue:Number;
		public function AnalysisMeasureResultMetadata()
		{
			super();
		}
		
	}
}