package com.easyinsight.util
{
	import com.adobe.flex.extras.controls.springgraph.Graph;
	import com.adobe.flex.extras.controls.springgraph.IEdgeRenderer;
	import com.adobe.flex.extras.controls.springgraph.SpringGraph;
	
	import flash.display.Graphics;
	
	import mx.core.UIComponent;

	public class TestEdgeRenderer implements IEdgeRenderer
	{
		private var edgeMap:Object = new Object();
		private var canvas:SpringGraph;
		
		public function TestEdgeRenderer(canvas:SpringGraph) {
			this.canvas = canvas;
		}
		
		public function related(fromID:int, toID:int):Boolean {
			var fromKey:String = fromID + ":" + toID;
			var toKey:String = fromID + ":" + toID;
			return edgeMap[fromKey] != null || edgeMap[toKey] != null;
		}		

		public function draw(g:Graphics, fromView:UIComponent, toView:UIComponent, fromX:int, fromY:int, toX:int, toY:int, graph:Graph):Boolean
		{	
			var fromNode:GraphFeedNode = fromView as GraphFeedNode;
			var toNode:GraphFeedNode = toView as GraphFeedNode;
			var edgeKey:String = fromNode.feedID + ":" + toNode.feedID;		
			var graphLine:GraphLine = edgeMap[edgeKey];
			if (graphLine == null) {
				graphLine = new GraphLine(fromX, fromY, toX, toY, fromNode.feedID, toNode.feedID);
				edgeMap[edgeKey] = graphLine;
				canvas.addChildAt(graphLine, 0);
			} else {
				graphLine.updateCoordinates(fromX, fromY, toX, toY);
			}			
			return true;			
		}
		
	}
}