package com.easyinsight.dashboard
{
	import flash.events.Event;

	public class ReturnToListEvent extends Event
	{
		public static const RETURN_TO_LIST:String = "returnToList";
		
		public function ReturnToListEvent()
		{
			super(RETURN_TO_LIST);
		}
		
		override public function clone():Event {
			return new ReturnToListEvent();
		}
	}
}