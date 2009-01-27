package com.easyinsight.feedassembly
{
	import flash.events.Event;

	public class CompositeFeedCreatedEvent extends Event
	{
		public static const COMPOSITE_CREATED:String = "compositeCreated";
		public var compositeFeedID:int;
		
		public function CompositeFeedCreatedEvent(compositeFeedID:int)
		{
			super(COMPOSITE_CREATED);
			this.compositeFeedID = compositeFeedID;
		}
		
	}
}