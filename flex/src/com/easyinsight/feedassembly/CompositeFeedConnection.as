package com.easyinsight.feedassembly
{
	import com.easyinsight.analysis.Key;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.CompositeFeedConnection")]
	public class CompositeFeedConnection
	{
		public var sourceJoin:Key;
		public var targetJoin:Key;
		public var sourceFeedID:int;
		public var targetFeedID:int;
		
		public function CompositeFeedConnection()
			{
			super();
		}

	}
}