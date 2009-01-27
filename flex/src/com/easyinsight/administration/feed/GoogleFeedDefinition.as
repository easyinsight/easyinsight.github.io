package com.easyinsight.administration.feed
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.google.GoogleFeedDefinition")]
	public class GoogleFeedDefinition extends FeedDefinitionData
	{
		public var worksheetURL:String;
		
		public function GoogleFeedDefinition()
		{
			super();
		}
		
	}
}