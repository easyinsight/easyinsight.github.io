package com.easyinsight.solutions
{

	
	import flash.events.Event;

	public class FeedSelectionEvent extends Event
	{
		public static const FEED_SELECTED:String = "feedSelected";
		public static const FEED_DELETED:String = "feedDeleted";
		
		public var feedDescriptor:DataSourceDescriptor;
		
		public function FeedSelectionEvent(type:String, feedDescriptor:DataSourceDescriptor)
		{
			super(type, true);
			this.feedDescriptor = feedDescriptor;
		}
		
		override public function clone():Event {
			return new FeedSelectionEvent(type, feedDescriptor);
		}
	}
}