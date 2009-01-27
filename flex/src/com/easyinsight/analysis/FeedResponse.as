package com.easyinsight.analysis
{
	import com.easyinsight.listing.DataFeedDescriptor;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.FeedResponse")]
	public class FeedResponse
	{
		public var feedDescriptor:DataFeedDescriptor;
		public var successful:Boolean;
		
		public function FeedResponse()
		{
		}

	}
}