package com.easyinsight.analysis.feed
{
	import com.easyinsight.administration.feed.DataFeedType;
	import com.easyinsight.administration.feed.FeedDefinitionData;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.AnalysisBasedFeedDefinition")]
	public class AnalysisBasedFeedDefinition extends FeedDefinitionData
	{
		
		public function AnalysisBasedFeedDefinition()
		{
			super();
		}
		
		override public function getFeedType():int {
			return DataFeedType.ANALYSIS;
		}
	}
}