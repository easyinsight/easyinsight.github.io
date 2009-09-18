package com.easyinsight.feedassembly
{
	import com.easyinsight.administration.feed.FeedDefinitionData;
	
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.CompositeFeedDefinition")]
	public class CompositeFeedDefinition extends FeedDefinitionData
	{
		public var compositeFeedNodes:ArrayCollection = new ArrayCollection();
		public var connections:ArrayCollection = new ArrayCollection();
		
		public function CompositeFeedDefinition()
		{
			super();
		}
	}
}