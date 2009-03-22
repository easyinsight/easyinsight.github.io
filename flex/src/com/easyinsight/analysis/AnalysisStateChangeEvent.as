package com.easyinsight.analysis
{
	import flash.events.Event;

	public class AnalysisStateChangeEvent extends Event
	{
		public static const ANALYSIS_STATE_CHANGE:String = "analysisStateChange";

        public var controller:IReportController;
		
		public function AnalysisStateChangeEvent(controller:IReportController)
		{
			super(ANALYSIS_STATE_CHANGE);
            this.controller = controller;
		}
		
		override public function clone():Event {
			return new AnalysisStateChangeEvent(controller);
		}
	}
}