package com.easyinsight.framework
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.InsightRequestMetadata")]
	
	public class InsightRequestMetadata
	{
		public var now:Date;
        public var version:int;
		
		public function InsightRequestMetadata()
		{
		}

	}
}