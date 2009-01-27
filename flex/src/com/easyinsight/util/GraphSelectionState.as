package com.easyinsight.util
{
	public class GraphSelectionState
	{
		public var nodeCount:Array = [];
		public var edgeCount:Array = [];
		
		public function GraphSelectionState()
		{
			this.nodeCount = nodeCount;
			this.edgeCount = edgeCount;
		}

		public function pushNode(node:GraphFeedNode):void {
			nodeCount.push(node);
		}
		
		public function pushEdge(edge:GraphLine):void {
			edgeCount.push(edge);
		}
	}
}