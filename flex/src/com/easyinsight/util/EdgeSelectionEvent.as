package com.easyinsight.util
{
	import flash.events.Event;

	public class EdgeSelectionEvent extends Event
	{
		public static const EDGE_SELECTED:String = "edgeSelected";
		public static const EDGE_CTRL_SELECT:String = "edgeCtrlSelect";
		public static const EDGE_DESELECTED:String = "edgeDeselected";
		
		public var graphLine:GraphLine;
		
		public function EdgeSelectionEvent(type:String, graphLine:GraphLine)
		{
			super(type, true);
			this.graphLine = graphLine;
		}
		
		override public function clone():Event {
			return new EdgeSelectionEvent(type, graphLine);
		}
	}
}