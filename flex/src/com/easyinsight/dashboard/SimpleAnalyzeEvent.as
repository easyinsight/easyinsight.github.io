package com.easyinsight.dashboard
{
	import flash.events.Event;

	public class SimpleAnalyzeEvent extends Event
	{
		public static const SIMPLE_ANALYZE:String = "simpleAnalyze";
		public var analysisID:int;
		
		public function SimpleAnalyzeEvent(analysisID:int)
		{
			super(SIMPLE_ANALYZE);
			this.analysisID = analysisID;
		}
		
		override public function clone():Event {
			return new SimpleAnalyzeEvent(analysisID);
		}
	}
}