package com.easyinsight.account {
import com.easyinsight.framework.UserTransferObject;
import com.easyinsight.util.ProgressAlert;

import flash.events.MouseEvent;
import flash.system.System;

import mx.containers.HBox;
import mx.controls.Button;
import mx.rpc.remoting.RemoteObject;

public class AdminUsersEmailControls extends HBox {

    private var initEmailButton:Button;
    private var copyButton:Button;

    [Embed(source="../../../../assets/copy.png")]
    private var copyImage:Class;
    [Embed(source="../../../../assets/mail.png")]
    private var emailImage:Class;

    private var user:UserTransferObject;

    private var adminService:RemoteObject;

    public function AdminUsersEmailControls() {
        super();
        adminService = new RemoteObject();
        adminService.destination = "admin";
        initEmailButton = new Button();
        initEmailButton.setStyle("icon", emailImage);
        initEmailButton.toolTip = "Send Welcome Email";
        initEmailButton.addEventListener(MouseEvent.CLICK, welcomeEmail);
        copyButton = new Button();
        copyButton.setStyle("icon", copyImage);
        copyButton.toolTip = "Copy to Clipboard";
        copyButton.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
            System.setClipboard(user.email);
        });
    }

    private function welcomeEmail(event:MouseEvent):void {
        ProgressAlert.alert(this, "Sending...", null, adminService.welcomeEmail);
        adminService.welcomeEmail.send(user.userID);
    }

    protected override function createChildren():void {
        super.createChildren();
        addChild(initEmailButton);
        addChild(copyButton);
    }

    override public function set data(val:Object):void {
        user = val as UserTransferObject;
    }

    override public function get data():Object {
        return user;
    }
}
}