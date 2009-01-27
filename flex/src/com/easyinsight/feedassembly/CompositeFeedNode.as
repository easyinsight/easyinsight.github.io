package com.easyinsight.feedassembly
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.CompositeFeedNode")]
	public class CompositeFeedNode
	{
		public var dataFeedID:int;
		
		public function CompositeFeedNode()
			{
			super();
		}

	}
}