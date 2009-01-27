package com.easyinsight.listing
{
	import flash.events.Event;

	public class TriggerAnalyzeEvent extends Event
	{
		public static const TRIGGER_ANALYZE:String = "triggerAnalyze";
		
		public function TriggerAnalyzeEvent()
		{
			super(TRIGGER_ANALYZE);
		}
		
		override public function clone():Event {
			return new TriggerAnalyzeEvent();
		}
	}
}