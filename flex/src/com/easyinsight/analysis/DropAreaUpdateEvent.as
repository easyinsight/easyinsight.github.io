package com.easyinsight.analysis
{
	import flash.events.Event;

	public class DropAreaUpdateEvent extends Event
	{
		public static const DROP_AREA_UPDATE:String = "dropAreaUpdate";
		public var analysisItem:AnalysisItem;
		public var previousAnalysisItem:AnalysisItem;
		
		public function DropAreaUpdateEvent(analysisItem:AnalysisItem, previousAnalysisItem:AnalysisItem = null)
		{
			super(DROP_AREA_UPDATE);
			this.analysisItem = analysisItem;
			this.previousAnalysisItem = previousAnalysisItem;
		}
		
		override public function clone():Event {
			return new DropAreaUpdateEvent(this.analysisItem, this.previousAnalysisItem);
		}
	}
}