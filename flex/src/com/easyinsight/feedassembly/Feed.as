package com.easyinsight.feedassembly
{
	import com.easyinsight.administration.feed.FeedDefinitionData;
	
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	
	import mx.collections.ArrayCollection;
	import mx.containers.VBox;
	import mx.controls.Label;
	import mx.controls.List;
	import mx.core.DragSource;
	import mx.managers.DragManager;
	
	public class Feed extends VBox
	{
		private var connectors:ArrayCollection = new ArrayCollection();
		public var list:List;
		private var feedDefinition:FeedDefinitionData;
		
		public function Feed(feedDefinition:FeedDefinitionData)
			{
			super();
			this.feedDefinition = feedDefinition;			
			addEventListener(KeyboardEvent.KEY_UP, keyHandler);			
			list = new List();
			list.dataProvider = feedDefinition.fields;
			list.labelField = "display";			
			var label:Label = new Label();			
			label.text = feedDefinition.feedName;
			addChild(label);
			addChild(list);
			label.addEventListener(MouseEvent.MOUSE_MOVE, beginDragAndDrop);
		}
		
		private function keyHandler(event:KeyboardEvent):void {
			if (event.keyCode == Keyboard.DELETE) {
				var connectorCopy:ArrayCollection = new ArrayCollection(connectors.toArray());
				for (var i:int = 0; i < connectorCopy.length; i++) {
					var connector:Connector = connectorCopy.getItemAt(i) as Connector;
					connector.nuke();
				}
				dispatchEvent(new FeedDeletionEvent(this));
			}
		}

		public function addConnector(connector:Connector):void {
			connectors.addItem(connector);
		}
		
		public function createCompositeFeedNode(fromConnector:Connector):CompositeFeedNode {
			var compositeFeedNode:CompositeFeedNode = new CompositeFeedNode();
			compositeFeedNode.dataFeedID = feedDefinition.dataFeedID;
			var connectorArray:Array = [];
			for (var i:int = 0; i < connectors.length; i++) {
				var connector:Connector = connectors.getItemAt(i) as Connector;
				if (connector != fromConnector) {
					connectorArray[i] = connector.createCompositeFeedConnection(this);
				}
			}
			compositeFeedNode.compositeFeedConnections = new ArrayCollection(connectorArray);
			return compositeFeedNode;
		}
		
		public function removeConnector(connector:Connector):void {
			var index:int = connectors.getItemIndex(connector);
			connectors.removeItemAt(index);
		}
		
		private function beginDragAndDrop(event:MouseEvent):void {
			if (event.buttonDown) {							
				var dragSource:DragSource = new DragSource();
				var proxy:Feed = new Feed(this.feedDefinition);
				proxy.list = this.list;				
				DragManager.doDrag(this, dragSource, event, proxy);
			}
		}
		
		public function moved():void {
			for (var i:int = 0; i < connectors.length; i++) {
				var connector:Connector = connectors.getItemAt(i) as Connector;
				connector.renderConnection();
			}
		}				
	}
}