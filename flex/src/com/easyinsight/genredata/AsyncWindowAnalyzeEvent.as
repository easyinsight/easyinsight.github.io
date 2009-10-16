package com.easyinsight.genredata
{
import com.easyinsight.listing.AnalyzeSource;

import flash.events.Event;
	
	public class AsyncWindowAnalyzeEvent extends Event
	{
		public static const ASYNC_WINDOW_ANALYZE:String = "asyncWindowAnalyze";
		
		public var analyzeSource:AnalyzeSource;
				
		public function AsyncWindowAnalyzeEvent(analyzeSource:AnalyzeSource)
		{
			super(ASYNC_WINDOW_ANALYZE, true);
			this.analyzeSource = analyzeSource;
		}
		
		override public function clone():Event {
			return new AsyncWindowAnalyzeEvent(analyzeSource);
		}
	}
}