package com.easyinsight.util
{
	import flash.events.Event;

	public class NodeSelectionEvent extends Event
	{
		public static const NODE_SELECTED:String = "nodeSelected";
		public static const NODE_CTRL_SELECT:String = "nodeCtrlSelect";
		public static const NODE_DESELECTED:String = "nodeDeselected";
		
		public var node:GraphFeedNode;
		
		public function NodeSelectionEvent(type:String, node:GraphFeedNode)
		{
			super(type, true);
			this.node = node;
		}
		
		override public function clone():Event {
			return new NodeSelectionEvent(type, node);
		}
	}
}