package com.easyinsight.analysis
{
	import flash.events.Event;

	public class SubscriptionEvent extends Event
	{
		public static const USER_TO_FEED:String = "userToFeed";
		public static const USER_TO_INSIGHT:String = "userToInsight";
		public static const GROUP_TO_FEED:String = "groupToFeed";
		public static const GROUP_TO_INSIGHT:String = "groupToInsight";
		
		public var insightID:int;
		public var feedID:int;
		
		public function SubscriptionEvent(type:String, feedID:int, insightID:int)
		{
			super(type);
			this.insightID = insightID;
			this.feedID = feedID; 
		}
		
		override public function clone():Event {
			return new SubscriptionEvent(type, insightID, feedID);
		}
	}
}