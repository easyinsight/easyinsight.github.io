package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.InsightResponse")]
	public class InsightResponse
	{
		public var successful:Boolean;
		public var definition:AnalysisDefinition;
		
		public function InsightResponse()
		{
		}

	}
}