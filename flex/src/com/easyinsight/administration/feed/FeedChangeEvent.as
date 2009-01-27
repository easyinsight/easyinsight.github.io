package com.easyinsight.administration.feed
{
	import flash.events.Event;

	public class FeedChangeEvent extends Event
	{
		public static const FEED_CHANGE:String = "feedChange";
		
		public function FeedChangeEvent()
		{
			super(FEED_CHANGE, true);
		}
		
		override public function clone():Event {
			return new FeedChangeEvent();
		}
	}
}