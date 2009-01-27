package com.easyinsight.analysis
{
	import flash.events.Event;

	public class AnalysisItemCreationEvent extends Event
	{
		public static const ANALYSIS_ITEM_CREATED:String = "analysisItemCreated";
		
		public var analysisItem:AnalysisItem;
		
		public function AnalysisItemCreationEvent(analysisItem:AnalysisItem)
		{
			super(ANALYSIS_ITEM_CREATED);
			this.analysisItem = analysisItem;
		}
		
		override public function clone():Event {
			return new AnalysisItemCreationEvent(analysisItem);
		}
	}
}