package com.easyinsight.framework
{
	import flash.events.Event;
	
	public class NewViewCreationEvent extends Event
	{
		public static const NEW_VIEW:String = "viewCreated";
		public var dataFeedID:int;
		
		public function NewViewCreationEvent(dataFeedID:int) {
			super(NEW_VIEW);
			this.dataFeedID = dataFeedID;
		}
		
		override public function clone():Event {
			return new NewViewCreationEvent(this.dataFeedID);
		}
	}
}