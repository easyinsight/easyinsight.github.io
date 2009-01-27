package com.easyinsight.administration.feed
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.UserFeedLink")]
	public class UserFeedLink
	{
		public var userID:int;
		public var userName:String;
		public var role:int;
		
		public function UserFeedLink()
		{
		}

	}
}