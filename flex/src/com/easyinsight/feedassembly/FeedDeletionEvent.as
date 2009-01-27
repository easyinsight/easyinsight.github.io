package com.easyinsight.feedassembly
{
	import flash.events.Event;

	public class FeedDeletionEvent extends Event
	{
		public static const FEED_DELETION:String = "feedDeletion";
		public var feed:Feed;
		
		public function FeedDeletionEvent(feed:Feed)
		{
			super(FEED_DELETION);
			this.feed = feed;
		}		
	}
}