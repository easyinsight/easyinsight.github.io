package com.easyinsight.feedassembly
{
	import com.easyinsight.administration.feed.FeedDefinitionData;
import com.easyinsight.datasources.DataSourceType;

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


        override public function getFeedType():int {
            return DataSourceType.COMPOSITE;
        }
    }
}