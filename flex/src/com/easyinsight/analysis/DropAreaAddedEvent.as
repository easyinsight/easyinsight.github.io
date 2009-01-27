package com.easyinsight.analysis
{
	import flash.events.Event;

	public class DropAreaAddedEvent extends Event
	{
		public static const DROP_AREA_ADD:String = "dropAreaAdd";
		public var analysisItem:AnalysisItem;
		
		public function DropAreaAddedEvent(analysisItem:AnalysisItem)
		{
			super(DROP_AREA_ADD);
			this.analysisItem = analysisItem;
		}
		
		override public function clone():Event {
			return new DropAreaAddedEvent(analysisItem);
		}
	}
}