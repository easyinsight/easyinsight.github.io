package com.easyinsight.feedassembly
{
import com.easyinsight.util.GraphLine;

import flash.events.Event;

	public class EdgeEditEvent extends Event
	{
		public static const EDGE_EDIT:String = "edgeEdit";
		public static const EDGE_DELETE:String = "edgeDelete";
		public static const EDGE_TEST:String = "edgeTest";

		public var sourceID:String;
		public var targetID:String;
        public var graphLine:GraphLine;
		
		public function EdgeEditEvent(type:String, sourceID:String, targetID:String, graphLine:GraphLine)
		{
			super(type, true);
			this.sourceID = sourceID;
			this.targetID = targetID;
            this.graphLine = graphLine;
		}
		
		override public function clone():Event {
			return new EdgeEditEvent(type, sourceID, targetID, graphLine);
		}
	}
}