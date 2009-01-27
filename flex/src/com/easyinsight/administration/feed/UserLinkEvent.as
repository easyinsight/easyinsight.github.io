package com.easyinsight.administration.feed
{
	import flash.events.Event;

	public class UserLinkEvent extends Event
	{
		public static const DELETE_USER_LINK:String = "DELETE_USER_LINK";
		public static const ADD_USER_LINK:String = "ADD_USER_LINK";
		
		public var userStub:FeedConsumer;
		
		public function UserLinkEvent(type:String, userStub:FeedConsumer)
		{
			super(type);
			this.userStub = userStub;
		}
		
		override public function clone():Event {
			return new UserLinkEvent(this.type, this.userStub);
		}
	}
}