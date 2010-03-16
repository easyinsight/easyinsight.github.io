package com.easyinsight.framework {
import com.easyinsight.LoginDialog;
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.ProgressAlert;

import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class AccountActivator {

    private var activationString:String;
    private var accountService:RemoteObject;
    private var workspace:PrimaryWorkspace;

    public function AccountActivator(activationString:String, workspace:PrimaryWorkspace) {
        this.activationString = activationString;
        this.workspace = workspace;
        accountService = new RemoteObject();
        accountService.destination = "accountAdmin";
        accountService.activateAccount.addEventListener(ResultEvent.RESULT, activated);
    }

    public function activate():void {
        ProgressAlert.alert(workspace, "Activating account", null, accountService.activateAccount);
        accountService.activateAccount.send(activationString);
    }

    private function activated(event:ResultEvent):void {
        var targetURL:String = accountService.activateAccount.lastResult as String;
        var loginDialog:LoginDialog = new LoginDialog();
        loginDialog.targetURL = targetURL;
        loginDialog.showActivation = true;
        PopUpManager.addPopUp(loginDialog, workspace, true);
        PopUpUtil.centerPopUp(loginDialog);
        User.getEventNotifier().addEventListener(LoginEvent.LOGIN, workspace.onLogin);        
    }
}
}