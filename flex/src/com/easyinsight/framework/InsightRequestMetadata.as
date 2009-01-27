package com.easyinsight.framework
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.InsightRequestMetadata")]
	
	public class InsightRequestMetadata
	{
		public var now:Date;
		
		public function InsightRequestMetadata()
		{
		}

	}
}