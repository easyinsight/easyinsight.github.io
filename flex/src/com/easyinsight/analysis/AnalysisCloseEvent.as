package com.easyinsight.analysis
{
import flash.display.DisplayObject;
import flash.events.Event;

	public class AnalysisCloseEvent extends Event
	{
		public static var ANALYSIS_CLOSE:String = "analysisClose";

        public var urlKey:String = null;
		
		public function AnalysisCloseEvent(urlKey:String = null)
		{
			super(ANALYSIS_CLOSE);
            this.urlKey = urlKey;
		}
		
		override public function clone():Event {
			return new AnalysisCloseEvent(urlKey);
		}
	}
}