package com.easyinsight.groups
{
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.ComboBox;
import mx.controls.Label;
import mx.events.ListEvent;

	public class GroupRoleRenderer extends HBox
	{
		private var userStub:GroupUser;
		private var roleLabel:Label;		
		
		public function GroupRoleRenderer()
		{
			super();
			setStyle("horizontalAlign", "center");
			this.percentWidth = 100;
            if (roleLabel == null) {
				roleLabel = new Label();
			}
		}
		
		override protected function createChildren():void {

			addChild(roleLabel);
		}

		[Bindable]
		override public function set data(object:Object):void {
			this.userStub = object as GroupUser;
			switch (userStub.role) {
				case 1:
					roleLabel.text = "Owner";
					break;
				case 2:
					roleLabel.text = "Editor";
					break;
				case 3:
					roleLabel.text = "Viewer";
					break;
			}
		}
		
		override public function get data():Object {
			return this.userStub;
		}
		
	}
}