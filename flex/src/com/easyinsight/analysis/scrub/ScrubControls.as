package com.easyinsight.analysis.scrub
{
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.managers.PopUpManager;

	public class ScrubControls extends HBox
	{
		private var dataScrub:DataScrub;
		
		[Bindable]
        [Embed(source="../../../../../assets/navigate_cross.png")]
        private var deleteIcon:Class;
        
        [Bindable]
        [Embed(source="../../../../../assets/pencil.png")]
        private var editIcon:Class;
        
        private var deleteButton:Button;
        
        private var editButton:Button;
        
        private var fields:ArrayCollection;
        
        private var feedID:int;
		
		public function ScrubControls(fields:ArrayCollection, feedID:int)
		{
			super();
			this.fields = fields;
			this.feedID = feedID;
			editButton = new Button();
			editButton.toolTip = "Edit";
			editButton.setStyle("icon", editIcon);
			editButton.addEventListener(MouseEvent.CLICK, editScrub);
			addChild(editButton);
			deleteButton = new Button();
			deleteButton.toolTip = "Delete";
			deleteButton.setStyle("icon", deleteIcon);
			deleteButton.addEventListener(MouseEvent.CLICK, deleteScrub);
			setStyle("percentWidth", 100);
			setStyle("horizontalAlign", "center");
			addChild(deleteButton);
		}
		
		private function editScrub(event:MouseEvent):void {
			var windowClass:Class = dataScrub.windowClass();
			var window:ScrubEditWindow = ScrubEditWindow(PopUpManager.createPopUp(this, windowClass));
			window.addEventListener(DataScrubEvent.UPDATED_SCRUB, passThrough);
			window.scrub = dataScrub;
			window.feedID = feedID;
			window.availableFields = fields;
			window.updateState();
            PopUpManager.centerPopUp(window);
		}
		
		private function passThrough(event:DataScrubEvent):void {
			dispatchEvent(event);
		}
		
		private function deleteScrub(event:MouseEvent):void {
			dispatchEvent(new DataScrubEvent(DataScrubEvent.DELETED_SCRUB, dataScrub));
		}
		
		override public function set data(value:Object):void {
			this.dataScrub = value as DataScrub;
		}
		
		override public function get data():Object {
			return dataScrub;
		}
	}
}