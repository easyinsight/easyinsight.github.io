package com.easyinsight.analysis
{
import com.easyinsight.datasources.DataSourceInfo;

import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.FeedMetadata")]
	public class FeedMetadata
	{
		public var fields:Array;	
		public var dataFeedID:int;
        public var urlKey:String;
        public var version:int;
        public var dataSourceName:String;
        public var dataSourceAdmin:Boolean;
        public var credentials:ArrayCollection;
        public var intrinsicFilters:ArrayCollection;
        public var fieldHierarchy:ArrayCollection;
        public var filterExampleMessage:String;
        public var dataSourceInfo:DataSourceInfo;
        public var exchangeSave:Boolean;
        public var customJoinsAllowed:Boolean;
    public var lookupTables:ArrayCollection;
    public var suggestions:ArrayCollection;
    public var dataSourceType:int;
    public var allowRefactor:Boolean;
		
		public function FeedMetadata()
			{
			super();
		}

	}
}