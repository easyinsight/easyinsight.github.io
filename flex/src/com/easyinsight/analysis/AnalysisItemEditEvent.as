package com.easyinsight.analysis
{
	import flash.events.Event;

	public class AnalysisItemEditEvent extends Event
	{
		public static const ANALYSIS_ITEM_EDIT:String = "analysisItemEdit";
		public var analysisItem:AnalysisItem;
		
		public function AnalysisItemEditEvent(analysisItem:AnalysisItem, bubbles:Boolean = false)
		{
			super(ANALYSIS_ITEM_EDIT, bubbles);
			this.analysisItem = analysisItem;
		}
		
		override public function clone():Event {
			return new AnalysisItemEditEvent(analysisItem, bubbles);
		}
	}
}