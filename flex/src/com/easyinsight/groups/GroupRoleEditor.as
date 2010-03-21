package com.easyinsight.groups
{
import flash.events.Event;

import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.controls.ComboBox;
	import mx.events.ListEvent;

	public class GroupRoleEditor extends HBox
	{
		private var userStub:GroupUser;
		private var comboBox:ComboBox;
		
		private var ownerRole:Object;
		private var editorRole:Object;
		private var viewerRole:Object;
		
		[Bindable]
		private var roles:ArrayCollection = new ArrayCollection();
		
		[Bindable]
        [Embed(source="../../../../assets/navigate_cross.png")]
        public var deleteIcon:Class;
		
		public function GroupRoleEditor()
		{
			super();
			setStyle("horizontalAlign", "center");
			this.percentWidth = 100;
			ownerRole = new Object();
			ownerRole["label"] = "Owner";
			ownerRole["data"] = 1;
			roles.addItem(ownerRole);
			editorRole = new Object();
			editorRole["label"] = "Editor";
			editorRole["data"] = 2;
			roles.addItem(editorRole);
			viewerRole = new Object();
			viewerRole["label"] = "Viewer";
			viewerRole["data"] = 3;
			roles.addItem(viewerRole);						
		}
		
		override protected function createChildren():void {
			if (comboBox == null) {
				comboBox = new ComboBox();
				comboBox.dataProvider = roles;			
				comboBox.addEventListener(ListEvent.CHANGE, change);
			}
			addChild(comboBox);
		}
		
		private function change(event:ListEvent):void {				
			userStub.role = event.currentTarget.selectedItem.data;
            dispatchEvent(new Event("groupInvalidation", true));
		}
		
		[Bindable]
		override public function set data(object:Object):void {
			this.userStub = object as GroupUser;
			switch (userStub.role) {
				case 1:
					comboBox.selectedItem = ownerRole;
					break;
				case 2:
					comboBox.selectedItem = editorRole;
					break;
				case 3:
					comboBox.selectedItem = viewerRole;
					break;
			}
		}
		
		override public function get data():Object {
			return this.userStub;
		}
		
	}
}