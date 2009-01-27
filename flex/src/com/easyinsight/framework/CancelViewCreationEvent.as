package com.easyinsight.framework
{
	import flash.events.Event;
	
	public class CancelViewCreationEvent extends Event
	{
		public static const CANCEL_VIEW:String = "viewCreationCancelled";
		
		public function CancelViewCreationEvent() {
			super(CANCEL_VIEW);
		}
	}
}