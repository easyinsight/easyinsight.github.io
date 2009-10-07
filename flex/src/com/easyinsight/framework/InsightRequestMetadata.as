package com.easyinsight.framework
{
import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.InsightRequestMetadata")]
	
	public class InsightRequestMetadata
	{
		public var now:Date = new Date();
        public var utcOffset:int;
        public var version:int;
        public var credentialFulfillmentList:ArrayCollection;
        public var refreshAllSources:Boolean;
        public var noCache:Boolean;
        public var hierarchyOverrides:ArrayCollection;
		
		public function InsightRequestMetadata()
		{
		}

	}
}