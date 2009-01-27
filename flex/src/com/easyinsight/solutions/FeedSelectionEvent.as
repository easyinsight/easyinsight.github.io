package com.easyinsight.solutions
{
	import com.easyinsight.listing.DataFeedDescriptor;
	
	import flash.events.Event;

	public class FeedSelectionEvent extends Event
	{
		public static const FEED_SELECTED:String = "feedSelected";
		public static const FEED_DELETED:String = "feedDeleted";
		
		public var feedDescriptor:DataFeedDescriptor;
		
		public function FeedSelectionEvent(type:String, feedDescriptor:DataFeedDescriptor)
		{
			super(type);
			this.feedDescriptor = feedDescriptor;
		}
		
		override public function clone():Event {
			return new FeedSelectionEvent(type, feedDescriptor);
		}
	}
}