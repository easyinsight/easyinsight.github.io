package com.easyinsight.genredata
{
import com.easyinsight.framework.ModuleAnalyzeSource;
import flash.events.Event;
	
	public class ModuleAnalyzeEvent extends Event
	{
		public static const MODULE_ANALYZE:String = "moduleAnalyze";
		
		public var analyzeSource:ModuleAnalyzeSource;
				
		public function ModuleAnalyzeEvent(analyzeSource:ModuleAnalyzeSource)
		{
			super(MODULE_ANALYZE, true);
			this.analyzeSource = analyzeSource;
		}
		
		override public function clone():Event {
			return new ModuleAnalyzeEvent(analyzeSource);
		}
	}
}