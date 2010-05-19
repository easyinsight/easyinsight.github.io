package com.easyinsight.analysis
{
	import flash.events.Event;

	public class AnalysisItemEditEvent extends Event
	{
		public static const ANALYSIS_ITEM_EDIT:String = "analysisItemEdit";
		public var analysisItem:AnalysisItem;
        public var previousItemWrapper:AnalysisItemWrapper;
		
		public function AnalysisItemEditEvent(analysisItem:AnalysisItem, previousItemWrapper:AnalysisItemWrapper, bubbles:Boolean = false)
		{
			super(ANALYSIS_ITEM_EDIT, bubbles);
			this.analysisItem = analysisItem;
            this.previousItemWrapper = previousItemWrapper;
		}
		
		override public function clone():Event {
			return new AnalysisItemEditEvent(analysisItem, previousItemWrapper, bubbles);
		}
	}
}