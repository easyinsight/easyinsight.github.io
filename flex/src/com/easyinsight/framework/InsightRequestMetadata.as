package com.easyinsight.framework
{
import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.InsightRequestMetadata")]
	
	public class InsightRequestMetadata
	{
		public var now:Date = new Date();
        public var version:int;
        public var credentialFulfillmentList:ArrayCollection;
		
		public function InsightRequestMetadata()
		{
		}

	}
}