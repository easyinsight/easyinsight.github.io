package com.easyinsight.customupload
{
	import com.easyinsight.administration.feed.FeedAdministrationWindow;

import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;
	
	import com.easyinsight.listing.DataFeedDescriptor;
	
	import mx.controls.Button;
	import mx.managers.PopUpManager;

	public class AdministerButton extends Button
	{
		private var _data:DataFeedDescriptor;
		
		public function AdministerButton()
		{
			super();
			label = "Administer";
			addEventListener(MouseEvent.CLICK, administer);
		}
		
		override public function set data(value:Object):void {			
			var descriptor:DataFeedDescriptor = value as DataFeedDescriptor;
			_data = descriptor;
		}
		
		override public function get data():Object {
			return _data;
		}
		
		private function administer(event:MouseEvent):void {
			var feedAdministration:FeedAdministrationWindow = FeedAdministrationWindow(PopUpManager.createPopUp(
				this.parent.parent.parent.parent, FeedAdministrationWindow, true));
			PopUpUtil.centerPopUp(feedAdministration);
			feedAdministration.setDataFeedDescriptor(_data);
		}
	}
}