package com.easyinsight.genredata
{
import com.easyinsight.framework.ModuleAnalyzeSource;
import flash.events.Event;
	
	public class AsyncWindowAnalyzeEvent extends Event
	{
		public static const ASYNC_WINDOW_ANALYZE:String = "asyncWindowAnalyze";
		
		public var analyzeSource:ModuleAnalyzeSource;
				
		public function AsyncWindowAnalyzeEvent(analyzeSource:ModuleAnalyzeSource)
		{
			super(ASYNC_WINDOW_ANALYZE, true);
			this.analyzeSource = analyzeSource;
		}
		
		override public function clone():Event {
			return new AsyncWindowAnalyzeEvent(analyzeSource);
		}
	}
}