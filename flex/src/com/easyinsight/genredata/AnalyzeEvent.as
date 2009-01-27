package com.easyinsight.genredata
{
	import flash.events.Event;
	
	import com.easyinsight.listing.AnalyzeSource;

	public class AnalyzeEvent extends Event
	{
		public static const ANALYZE:String = "analyze";
		
		public var analyzeSource:AnalyzeSource;
				
		public function AnalyzeEvent(analyzeSource:AnalyzeSource)
		{
			super(ANALYZE, true);
			this.analyzeSource = analyzeSource;
		}
		
		override public function clone():Event {
			return new AnalyzeEvent(analyzeSource);
		}
	}
}