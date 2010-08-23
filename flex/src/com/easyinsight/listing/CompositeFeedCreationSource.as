package com.easyinsight.listing
{
import com.easyinsight.framework.PerspectiveInfo;

import mx.collections.ArrayCollection;

public class CompositeFeedCreationSource extends PerspectiveInfo
	{
		
		public function CompositeFeedCreationSource(descriptors:ArrayCollection)
		{
            super(PerspectiveInfo.COMPOSITE_WORKSPACE, new Object());
            properties.feedList = descriptors;
		}
	}
}