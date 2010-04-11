package com.easyinsight.analysis
{
	import flash.events.Event;

	public class SavedAnalysisEvent extends Event
	{
		public static const SAVED_ANALYSIS:String = "viewSaved";
		public var report:AnalysisDefinition;
		
		public function SavedAnalysisEvent(report:AnalysisDefinition)
		{
			super(SAVED_ANALYSIS);
			this.report = report;
		}
		
		override public function clone():Event {
			return new SavedAnalysisEvent(report);
		}
	}
}