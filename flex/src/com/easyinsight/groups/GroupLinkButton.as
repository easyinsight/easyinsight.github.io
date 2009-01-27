package com.easyinsight.groups
{
	import flash.events.MouseEvent;
	
	import mx.controls.LinkButton;
	
	[Event(name="groupSelected", type="com.easyinsight.groups.GroupSelectedEvent")]

	public class GroupLinkButton extends LinkButton
	{
		private var groupID:int;
		private var groupName:String;
		
		public function GroupLinkButton()
		{
			super();
			setStyle("color", "#FFFFFF");
			setStyle("fontSize", 14);
			addEventListener(MouseEvent.CLICK, groupClick);
		}
		
		private function groupClick(event:MouseEvent):void {
			dispatchEvent(new GroupSelectedEvent(groupID));
		}
		
		public function set groupDescriptor(groupDescriptor:GroupDescriptor):void {
			this.groupID = groupDescriptor.groupID;			
			this.groupName = groupDescriptor.name;
			this.label = groupName;
		}
	}
}