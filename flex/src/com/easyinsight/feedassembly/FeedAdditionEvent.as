package com.easyinsight.feedassembly
{
	import com.easyinsight.administration.feed.FeedDefinitionData;
	
	import flash.events.Event;

	public class FeedAdditionEvent extends Event
	{
		public static const FEED_ADDED:String = "feedAdded";
		
		public var feedDescriptor:FeedDefinitionData;
		
		public function FeedAdditionEvent(feedDescriptor:FeedDefinitionData)
		{
			super(FEED_ADDED);
			this.feedDescriptor = feedDescriptor;
		}
		
		override public function clone():Event {
			return new FeedAdditionEvent(feedDescriptor);
		}		
	}
}