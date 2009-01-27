package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.FeedMetadata")]
	public class FeedMetadata
	{
		public var fields:Array;	
		public var dataFeedID:int;
		
		public function FeedMetadata()
			{
			super();
		}

	}
}