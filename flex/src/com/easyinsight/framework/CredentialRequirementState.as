package com.easyinsight.framework {
import com.easyinsight.analysis.CredentialRequirement;
import com.easyinsight.analysis.CredentialsEvent;
import com.easyinsight.analysis.RuntimeCredentialsWindow;
import com.easyinsight.util.PopUpUtil;

import flash.display.DisplayObject;

import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;
import mx.managers.PopUpManager;

public class CredentialRequirementState extends EventDispatcher {

    private var neededCredentialCount:int = 0;
    private var receivedCredentialCount:int = 0;

    private var credentials:ArrayCollection;
    private var successFunction:Function;
    private var displayObject:DisplayObject;

    private var callbackParams:Array;

    public function CredentialRequirementState(displayObject:DisplayObject, credentials:ArrayCollection, successFunction:Function,
            ... callbackParams) {
        this.displayObject = displayObject;
        this.credentials = credentials;
        this.successFunction = successFunction;
        this.callbackParams = callbackParams;
    }

    public function act():void {
        receivedCredentialCount = 0;
        neededCredentialCount = 0;
        for each (var credential:CredentialRequirement in credentials) {
            var encryptedCredentials:Credentials = User.getCredentials(credential.dataSourceID);
            if (encryptedCredentials == null) {
                neededCredentialCount++;
                var window:RuntimeCredentialsWindow = new RuntimeCredentialsWindow();
                window.dataSourceID = credential.dataSourceID;
                window.addEventListener(CredentialsEvent.CREDENTIALS_SAVED, onCredentials);
                PopUpManager.addPopUp(window, displayObject, true);
                PopUpUtil.centerPopUp(window);
            } else {
                CredentialsCache.getCache().addCredentials(credential.dataSourceID, encryptedCredentials);                
            }
        }
        if (neededCredentialCount == 0) {
            successFunction.call(displayObject);
        }
    }

    private function onCredentials(event:CredentialsEvent):void {        
        CredentialsCache.getCache().addCredentials(event.dataSourceID, event.credentials);
        receivedCredentialCount++;
        if (receivedCredentialCount == neededCredentialCount) {            
            successFunction.call(displayObject);  
        }
    }
}
}