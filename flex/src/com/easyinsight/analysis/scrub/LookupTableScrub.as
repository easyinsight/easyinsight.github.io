package com.easyinsight.analysis.scrub
{
	import com.easyinsight.analysis.Key;
	
	import mx.collections.ArrayCollection;

[	Bindable]
	[RemoteClass(alias="com.easyinsight.scrubbing.LookupTableScrub")]
	public class LookupTableScrub extends DataScrub
	{
		public var lookupTablePairs:ArrayCollection;
		public var sourceKey:Key;
		public var targetKey:Key;
		
		public function LookupTableScrub()
		{
			super();
		}
		
		public function get sourceName():String {
			return sourceKey.createString();
		}
		
		public function get targetName():String {
			return targetKey.createString();
		}
		
		override public function windowClass():Class {
			return LookupTableScrubWindow;
		}
	}
}