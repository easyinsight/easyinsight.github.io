package com.easyinsight.administration.sharing
{
	import com.easyinsight.administration.feed.FeedConsumer;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.email.UserStub")]
	public class UserStub extends FeedConsumer
	{
		public var userID:int;
		public var fullName:String;
		public var email:String;
		
		public function UserStub()
		{
		}

	}
}