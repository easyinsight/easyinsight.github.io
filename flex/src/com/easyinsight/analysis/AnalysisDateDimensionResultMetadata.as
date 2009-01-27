package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.AnalysisDateDimensionResultMetadata")]
	public class AnalysisDateDimensionResultMetadata extends AnalysisItemResultMetadata
	{
		public var earliestDate:Date;
		public var latestDate:Date;
		
		public function AnalysisDateDimensionResultMetadata()
		{
			super();
		}
		
	}
}