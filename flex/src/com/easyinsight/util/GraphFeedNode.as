package com.easyinsight.util
{
	import com.adobe.flex.extras.controls.springgraph.Item;
	
	import flash.events.MouseEvent;
	
	import mx.containers.ApplicationControlBar;
	import mx.containers.VBox;
	import mx.controls.Image;
	import mx.controls.Label;

	public class GraphFeedNode extends VBox
	{
		public var feedID:int;
		[Bindable]
		private var feedName:String = "Sample Name";
		private var labelBar:ApplicationControlBar;
		private var labelImage:Image;
		
		private var item:Item;
		
		private var selected:Boolean = false;		
		
		[Bindable]
        [Embed(source="../../../../assets/data.png")]
        public var yahooMapIcon:Class;
		
		public function GraphFeedNode()
		{
			super();
			setStyle("horizontalAlign", "center");
			setStyle("borderThickness", 2);			
			addEventListener(MouseEvent.CLICK, gotClicked);
		}
		
		private function gotClicked(event:MouseEvent):void {			
			if (selected) {
				clearSelection();				
				dispatchEvent(new NodeSelectionEvent(NodeSelectionEvent.NODE_DESELECTED, this));				
			} else {
				setStyle("borderStyle", "solid");											
				selected = true;
				if (!event.ctrlKey) {
					dispatchEvent(new NodeSelectionEvent(NodeSelectionEvent.NODE_SELECTED, this));
				} else {
					dispatchEvent(new NodeSelectionEvent(NodeSelectionEvent.NODE_CTRL_SELECT, this));
				}				
			}
		}
		
		public function isSelected():Boolean {
			return selected;
		}
		
		public function clearSelection():void {			
			setStyle("borderStyle", "none");
			selected = false;					
		}
		
		override protected function createChildren():void {
			super.createChildren();
			if (labelBar == null) {
				labelBar = new ApplicationControlBar();
				var label:Label = new Label();
				label.text = feedName;
				labelBar.addChild(label);
			}
			addChild(labelBar);
			if (labelImage == null) {
				labelImage = new Image();
				labelImage.source = yahooMapIcon;
			}
			addChild(labelImage);
		}
		
		override public function set data(value:Object):void {
			this.item = value as Item;
			this.feedID = this.item.data["feedID"];
			this.feedName = this.item.data["feedName"];
		}
		
		override public function get data():Object {
			return this.item;
		}
	}
}