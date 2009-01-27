package com.easyinsight.feedassembly
{
	import com.easyinsight.analysis.Key;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.PotentialJoin")]
	public class PotentialJoin
	{
		public var sourceFeedID:int;
		public var targetFeedID:int;
		public var sourceKey:Key;
		public var targetKey:Key;
		
		public function PotentialJoin()
		{
		}

	}
}