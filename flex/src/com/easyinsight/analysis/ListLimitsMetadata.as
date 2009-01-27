package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.ListLimitsMetadata")]
	public class ListLimitsMetadata extends LimitsMetadata
	{
		public var analysisItem:AnalysisItem;
		
		public function ListLimitsMetadata()
		{
			super();
		}
		
	}
}