package com.easyinsight.administration.feed
{

import com.easyinsight.skin.ImageConstants;

import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Button;

	public class DeleteUserButton extends HBox
	{
		private var feedConsumer:FeedConsumer;
		private var button:Button;
		
		public function DeleteUserButton()
		{
			super();
			setStyle("horizontalAlign", "center");
			this.percentWidth = 100;			
		}
		
		override protected function createChildren():void {
			if (button == null) {
				button = new Button();
				button.toolTip = "Delete";
				button.setStyle("icon", ImageConstants.DELETE_ICON);
				button.addEventListener(MouseEvent.CLICK, deleteUser);

			}
			addChild(button);
		}
		
		private function deleteUser(event:MouseEvent):void {
			dispatchEvent(new UserLinkEvent(UserLinkEvent.DELETE_USER_LINK, feedConsumer));
		}
		
		override public function set data(object:Object):void {
			this.feedConsumer = object as FeedConsumer;
		}
		
		override public function get data():Object {
			return this.feedConsumer;
		}
	}
}