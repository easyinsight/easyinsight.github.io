package com.easyinsight.util
{
	import flash.events.Event;
	
	public class NodeDeletionEvent extends Event
	{
		public static const nodeDeleted:String = "NODE_DELETED";
		public var graphFeedNode:GraphFeedNode;
		
		public function NodeDeletionEvent(graphFeedNode:GraphFeedNode)
		{
			this.graphFeedNode = graphFeedNode;
		}

		override public function clone():Event {
			return new NodeDeletionEvent(graphFeedNode);
		}
	}
}