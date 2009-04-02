package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.FeedMetadata")]
	public class FeedMetadata
	{
		public var fields:Array;	
		public var dataFeedID:int;
        public var version:int;
        public var dataSourceName:String;
		
		public function FeedMetadata()
			{
			super();
		}

	}
}