package com.easyinsight.analysis
{
	import flash.events.Event;

	public class AnalysisItemUpdateEvent extends Event
	{
		public static const ANALYSIS_LIST_UPDATE:String = "analysisItemUpdate";
		
		public function AnalysisItemUpdateEvent()
		{
			super(ANALYSIS_LIST_UPDATE);
		}
	}
}