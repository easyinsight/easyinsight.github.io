package com.easyinsight.administration.feed
{
	import flash.events.Event;

	public class FeedUpdateEvent extends Event
	{
		public static var FEED_UPDATE:String = "feedUpdate";
		
		public function FeedUpdateEvent() {
			super(FEED_UPDATE);
		}
		
		override public function clone():Event {
			return new FeedUpdateEvent();
		}
	}
}