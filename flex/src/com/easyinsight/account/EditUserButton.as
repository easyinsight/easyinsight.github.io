package com.easyinsight.account
{
import flash.display.DisplayObject;
import mx.managers.PopUpManager;
import com.easyinsight.framework.UserTransferObject;
import flash.events.MouseEvent;
import mx.containers.HBox;
	import mx.controls.Button;

	public class EditUserButton extends HBox
	{

        [Bindable]
        [Embed(source="../../../../assets/navigate_cross.png")]
        private var deleteIcon:Class;

        [Bindable]
        [Embed(source="../../../../assets/pencil.png")]
        public var editIcon:Class;

        private var user:UserTransferObject;
        private var editButton:Button;
        private var deleteButton:Button;

        private var parentWindow:DisplayObject;
        private var editable:Boolean;

		public function EditUserButton(parentWindow:DisplayObject, editable:Boolean)
		{
			super();
            this.parentWindow = parentWindow;
            this.editable = editable;
            this.percentWidth = 100;
            setStyle("horizontalAlign", "center");
            editButton = new Button();
            editButton.toolTip = "Edit...";
            editButton.setStyle("icon", editIcon);
            editButton.addEventListener(MouseEvent.CLICK, onEdit);
            deleteButton = new Button();
            deleteButton.toolTip = "Delete";
            deleteButton.setStyle("icon", deleteIcon);
            deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
		}

        private function onEdit(event:MouseEvent):void {
            var userProfileEditor:NewUserDialog = new NewUserDialog();
            userProfileEditor.user = user;
            userProfileEditor.addEventListener(RefreshAccountEvent.REFRESH_ACCOUNT, refreshAccount);
            PopUpManager.addPopUp(userProfileEditor, parentWindow, true);
            PopUpManager.centerPopUp(userProfileEditor);
        }

        private function refreshAccount(event:RefreshAccountEvent):void {
            dispatchEvent(event);
        }

        private function onDelete(event:MouseEvent):void {
            dispatchEvent(new DeleteUserEvent(user.userID));
        }

        override protected function createChildren():void {
            super.createChildren();
            addChild(editButton);
            addChild(deleteButton);
        }

        override public function set data(value:Object):void {
            this.user = value as UserTransferObject;
        }

        override public function get data():Object {
            return this.user;
        }
	}
}