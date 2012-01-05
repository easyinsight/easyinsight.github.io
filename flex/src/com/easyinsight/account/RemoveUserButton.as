package com.easyinsight.account {
import com.easyinsight.framework.UserTransferObject;
import com.easyinsight.skin.ImageConstants;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.controls.Button;

public class RemoveUserButton extends HBox{



    private var user:UserTransferObject;

    private var deleteButton:Button;

    public function RemoveUserButton() {
        super();
        deleteButton = new Button();
        deleteButton.setStyle("icon", ImageConstants.DELETE_ICON);
        deleteButton.addEventListener(MouseEvent.CLICK, removeUser);
        setStyle("horizontalAlign", "center");
        percentWidth = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(deleteButton);
    }

    private function removeUser(event:MouseEvent):void {
        var userEvent:Event = new Event("removeUser", true);
        userEvent["user"] = user;
        dispatchEvent(userEvent);
    }


    override public function set data(val:Object):void {
        user = val as UserTransferObject;

    }

    override public function get data():Object {
        return user;
    }
}
}