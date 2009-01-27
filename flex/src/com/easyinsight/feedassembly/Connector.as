package com.easyinsight.feedassembly
{
	import com.easyinsight.analysis.Key;
	
	import mx.core.UIComponent;
	
	public class Connector extends UIComponent
	{
		private var feed1:Feed;
		private var attribute1:Key;
		private var feed2:Feed;
		private var attribute2:Key;
		
		public function Connector(feed1:Feed, attribute1:Key, feed2:Feed, attribute2:Key)
			{
			super();
			this.feed1 = feed1;
			this.attribute1 = attribute1;
			this.feed2 = feed2;
			this.attribute2 = attribute2;
		}
		
		public function createCompositeFeedConnection(feed:Feed):CompositeFeedConnection {
			var compositeFeedConnection:CompositeFeedConnection = new CompositeFeedConnection();
			if (feed == feed1) {
				compositeFeedConnection.sourceJoin = attribute1;
				compositeFeedConnection.targetJoin = attribute2;
				compositeFeedConnection.compositeFeedNode = feed2.createCompositeFeedNode(this);
			} else {
				compositeFeedConnection.sourceJoin = attribute2;
				compositeFeedConnection.targetJoin = attribute1;
				compositeFeedConnection.compositeFeedNode = feed1.createCompositeFeedNode(this);
			}
			return compositeFeedConnection;			
		}

		public function renderConnection():void {
			graphics.clear();
			graphics.lineStyle(2,0xCCCCCC,1);
			graphics.beginFill(0x666666);
			var sourceX:int = (feed1.x + (feed1.x + feed1.width)) / 2;
			var sourceY:int = (feed1.y + (feed1.y + feed1.height)) / 2;
			var targetX:int = (feed2.x + (feed2.x + feed2.width)) / 2;
			var targetY:int = (feed2.y + (feed2.y + feed2.height)) / 2;
			graphics.moveTo(sourceX, sourceY);
			graphics.lineTo(targetX, targetY);
			graphics.endFill();		
		}
		
		public function nuke():void {
			feed1.removeConnector(this);
			feed2.removeConnector(this);
			parent.removeChild(this);
		}
	}
}