package com.easyinsight.analysis
{
	import flash.events.Event;

	public class SavedAnalysisEvent extends Event
	{
		public static const SAVED_ANALYSIS:String = "viewSaved";
		public var title:String;
		
		public function SavedAnalysisEvent(title:String)
		{
			super(SAVED_ANALYSIS);
			this.title = title;
		}
		
		override public function clone():Event {
			return new SavedAnalysisEvent(this.title);
		}
	}
}