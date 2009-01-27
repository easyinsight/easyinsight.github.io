package com.easyinsight.administration.feed
{
	import com.easyinsight.groups.GroupDescriptor;
	
	import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Button;

	public class DeleteGroupButton extends HBox
	{
		private var groupDescriptor:GroupDescriptor;
		private var button:Button;
		
		[Bindable]
        [Embed(source="../../../../../assets/navigate_cross.png")]
        public var deleteIcon:Class;
		
		public function DeleteGroupButton()
		{
			super();
			setStyle("horizontalAlign", "center");
			this.percentWidth = 100;
		}
		
		override protected function createChildren():void {
			if (button == null) {
				button = new Button();
				button.toolTip = "Delete";
				button.setStyle("icon", deleteIcon);
				button.addEventListener(MouseEvent.CLICK, deleteGroup);
				addChild(button);
			}
		}
		
		private function deleteGroup(event:MouseEvent):void {
			parent.dispatchEvent(new GroupLinkEvent(GroupLinkEvent.DELETE_GROUP_LINK, groupDescriptor));
		}
		
		override public function set data(object:Object):void {
			this.groupDescriptor = object as GroupDescriptor;
		}
		
		override public function get data():Object {
			return this.groupDescriptor;
		}
	}
}