package com.easyinsight.account
{
import com.easyinsight.framework.User;
import com.easyinsight.skin.ImageConstants;
import com.easyinsight.util.PopUpUtil;

import flash.display.DisplayObject;

import mx.controls.Alert;
import mx.managers.PopUpManager;
import com.easyinsight.framework.UserTransferObject;
import flash.events.MouseEvent;
import mx.containers.HBox;
	import mx.controls.Button;

	public class EditUserButton extends HBox
	{



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
            editButton.setStyle("icon", ImageConstants.EDIT_ICON);
            editButton.addEventListener(MouseEvent.CLICK, onEdit);
            deleteButton = new Button();
            deleteButton.toolTip = "Delete";
            deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
            deleteButton.addEventListener(MouseEvent.CLICK, onDelete);
		}

        private function onEdit(event:MouseEvent):void {
            if (user.analyst) {
                var userProfileEditor:NewUserDialog = new NewUserDialog();
                userProfileEditor.user = user;
                userProfileEditor.addEventListener(RefreshAccountEvent.REFRESH_ACCOUNT, refreshAccount, false, 0, true);
                PopUpManager.addPopUp(userProfileEditor, parentWindow, true);
                PopUpUtil.centerPopUp(userProfileEditor);
            } else {
                var basicEditor:NamedUserDialog = new NamedUserDialog();
                basicEditor.user = user;
                basicEditor.addEventListener(RefreshAccountEvent.REFRESH_ACCOUNT, refreshAccount, false, 0, true);
                PopUpManager.addPopUp(basicEditor, parentWindow, true);
                PopUpUtil.centerPopUp(basicEditor);
            }
        }

        private function refreshAccount(event:RefreshAccountEvent):void {
            dispatchEvent(event);
        }

        private function onDelete(event:MouseEvent):void {
            if (user.userID == User.getInstance().userID) {
                Alert.show("You can't delete yourself.");
                return;
            }
            dispatchEvent(new DeleteUserEvent(user.userID, user.userName));
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