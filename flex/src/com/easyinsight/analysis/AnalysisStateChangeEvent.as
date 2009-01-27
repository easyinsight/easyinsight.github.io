package com.easyinsight.analysis
{
	import flash.events.Event;

	public class AnalysisStateChangeEvent extends Event
	{
		public static const ANALYSIS_STATE_CHANGE:String = "analysisStateChange";
		
		public var newState:String;
		public var subState:int;
		
		public function AnalysisStateChangeEvent(newState:String, subState:int)
		{
			super(ANALYSIS_STATE_CHANGE);
			this.newState = newState;
			this.subState = subState;
		}
		
		override public function clone():Event {
			return new AnalysisStateChangeEvent(newState, subState);
		}
	}
}