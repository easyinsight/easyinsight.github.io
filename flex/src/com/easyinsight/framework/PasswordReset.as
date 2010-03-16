package com.easyinsight.framework {
import com.easyinsight.listing.PasswordResetWindow;
import com.easyinsight.util.PopUpUtil;

import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class PasswordReset {

    private var userService:RemoteObject;
    private var passwordResetValue:String;
    private var workspace:PrimaryWorkspace;

    public function PasswordReset(passwordResetValue:String, workspace:PrimaryWorkspace) {
        this.passwordResetValue = passwordResetValue;
        this.workspace = workspace;
        userService = new RemoteObject();
        userService.destination = "login";
        userService.verifyPasswordReset.addEventListener(ResultEvent.RESULT, passwordResetCheck);
    }

    public function reset():void {
        userService.verifyPasswordReset.send(passwordResetValue);
    }

    private function passwordResetCheck(event:ResultEvent):void {
        var result:Boolean = userService.verifyPasswordReset.lastResult as Boolean;
        if (result) {
            var window:PasswordResetWindow = new PasswordResetWindow();
            window.passwordValidation = passwordResetValue;
            PopUpManager.addPopUp(window, workspace, true);
            PopUpUtil.centerPopUp(window);
        }
    }
}
}