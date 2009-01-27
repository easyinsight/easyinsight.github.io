package com.easyinsight.feedassembly
{
	import flash.events.Event;

	public class EdgeEditEvent extends Event
	{
		public static const EDGE_EDIT:String = "edgeEdit";
		
		public var sourceID:String;
		public var targetID:String;
		
		public function EdgeEditEvent(sourceID:String, targetID:String)
		{
			super(EDGE_EDIT, true);
			this.sourceID = sourceID;
			this.targetID = targetID;
		}
		
		override public function clone():Event {
			return new EdgeEditEvent(sourceID, targetID);
		}
	}
}