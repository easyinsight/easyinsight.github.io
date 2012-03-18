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
        public var uniqueFields:Object;
		
		public function CompositeFeedDefinition()
		{
			super();
		}

        override public function createAdminPages():ArrayCollection {
            var pages:ArrayCollection = new ArrayCollection();
            var compositeWorkspace:CompositeWorkspace = new CompositeWorkspace();
            compositeWorkspace.addExistingDef(compositeFeedNodes, connections, dataFeedID);
            compositeWorkspace.label = "Composite Details";
            pages.addItem(compositeWorkspace);
            return pages;
        }

        override public function getFeedType():int {
            return DataSourceType.COMPOSITE;
        }
    }
}