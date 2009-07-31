package com.easyinsight.analysis
{
import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.FeedMetadata")]
	public class FeedMetadata
	{
		public var fields:Array;	
		public var dataFeedID:int;
        public var version:int;
        public var dataSourceName:String;
        public var dataSourceAdmin:Boolean;
        public var credentials:ArrayCollection;
        public var intrinsicFilters:ArrayCollection;
        public var fieldHierarchy:ArrayCollection;
        public var dataSourceInfos:ArrayCollection;
		
		public function FeedMetadata()
			{
			super();
		}

	}
}