package com.easyinsight.account
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.BriefFeedInfo")]
	
	public class BriefFeedInfo
	{
		public var name:String;
		public var feedID:int;
		
		public function BriefFeedInfo()
		{
		}

	}
}