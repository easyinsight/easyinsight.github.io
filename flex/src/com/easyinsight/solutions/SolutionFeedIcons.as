package com.easyinsight.solutions
{
	import com.easyinsight.listing.DataFeedDescriptor;
	
	import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Button;

	public class SolutionFeedIcons extends HBox
	{
		[Embed(source="../../../../assets/refresh.png")]
        public var refreshIcon:Class;
        
        private var feedDescriptor:DataFeedDescriptor;
        
        private var deleteButton:Button;
        
		public function SolutionFeedIcons()
		{
			super();
			deleteButton = new Button();
			deleteButton.setStyle("icon", refreshIcon);
			deleteButton.toolTip = "Delete";
			deleteButton.addEventListener(MouseEvent.CLICK, deleteCalled);
			addChild(deleteButton);
			this.setStyle("paddingLeft", 5);
			this.setStyle("paddingRight", 5);
		}
		
		private function deleteCalled(event:MouseEvent):void {
			
		}
		
		override public function set data(value:Object):void {
			this.feedDescriptor = value as DataFeedDescriptor;
		}
		
		override public function get data():Object {
			return this.feedDescriptor;
		}
	}
}