package com.easyinsight.feedassembly
{
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.CompositeFeedNode")]
	public class CompositeFeedNode
	{
		public var dataFeedID:int;
        public var x:int;
        public var y:int;
        public var dataSourceName:String;
        public var dataSourceType:int;
		
		public function CompositeFeedNode()
			{
			super();
		}

	}
}