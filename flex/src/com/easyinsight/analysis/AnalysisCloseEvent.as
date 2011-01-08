package com.easyinsight.analysis
{
import flash.display.DisplayObject;
import flash.events.Event;

	public class AnalysisCloseEvent extends Event
	{
		public static var ANALYSIS_CLOSE:String = "analysisClose";
		
		public function AnalysisCloseEvent()
		{
			super(ANALYSIS_CLOSE);
		}
		
		override public function clone():Event {
			return new AnalysisCloseEvent();
		}
	}
}