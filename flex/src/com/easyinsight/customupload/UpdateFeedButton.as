package com.easyinsight.customupload
{
	import com.easyinsight.listing.DataFeedDescriptor;

import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;
	
	import mx.controls.Button;
	import mx.managers.PopUpManager;

	public class UpdateFeedButton extends Button
	{
		private var feedDescriptor:DataFeedDescriptor;
		
		public function UpdateFeedButton()
		{
			super();
			addEventListener(MouseEvent.CLICK, refreshData);
		}
		
		override public function set data(value:Object):void {
			this.feedDescriptor = value as DataFeedDescriptor;	
			if (this.feedDescriptor != null) {			
				switch (this.feedDescriptor.feedType) {
					case DataFeedDescriptor.STATIC:
						this.label = "Upload New Data";						
						break;
					default:					
						this.label = "Refresh";
						break;
				}
			}
		}
		
		private function clicked(event:MouseEvent):void {
			switch (this.feedDescriptor.feedType) {
				case DataFeedDescriptor.STATIC:
					refreshData();
					break;
				default:
					fileData();
					break;
			}
		}
		
		private function refreshData():void {
			var refreshWindow:RefreshWindow = RefreshWindow(PopUpManager.createPopUp(this.parent, RefreshWindow, true));
			refreshWindow.feedID = feedDescriptor.dataFeedID;
			PopUpUtil.centerPopUp(refreshWindow);
		}
		
		private function fileData():void {
			var feedUpdateWindow:FileFeedUpdateWindow = FileFeedUpdateWindow(PopUpManager.createPopUp(this.parent, FileFeedUpdateWindow, true));
			feedUpdateWindow.feedID = feedDescriptor.dataFeedID;
			PopUpUtil.centerPopUp(feedUpdateWindow);
				
		}
		
		override public function get data():Object {
			return this.feedDescriptor;
		}				
	}
}