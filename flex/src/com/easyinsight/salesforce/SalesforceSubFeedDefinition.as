package com.easyinsight.salesforce
{
	import com.easyinsight.administration.feed.FeedDefinitionData;

	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.salesforce.SalesforceSubFeedDefinition")]
	public class SalesforceSubFeedDefinition extends FeedDefinitionData
	{
		public var subQueryType:int;
		
		public function SalesforceSubFeedDefinition()
		{
			super();
		}
		
	}
}