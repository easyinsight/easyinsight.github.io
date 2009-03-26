package com.easyinsight.analysis
{
	import com.easyinsight.listing.DataFeedDescriptor;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.FeedResponse")]
	public class FeedResponse
	{
        public static const SUCCESS:int = 1;
        public static const NEED_LOGIN:int = 2;
        public static const REJECTED:int = 3;


		public var feedDescriptor:DataFeedDescriptor;
		public var status:int;
		
		public function FeedResponse()
		{
		}

	}
}