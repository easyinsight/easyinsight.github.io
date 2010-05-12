package com.easyinsight.framework {
import com.easyinsight.analysis.CredentialFulfillment;

import flash.display.DisplayObject;

import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;

public class WebCredentialsCache extends EventDispatcher implements ICredentialsCache {
    public function WebCredentialsCache() {
    }

    private var cache:Object = new Object();

    public function addCredentials(dataSourceID:int, credentials:Credentials):void {
        cache[String(dataSourceID)] = credentials;
    }

    public function nukeCredentials(dataSourceID:int):void {
        cache[String(dataSourceID)] = null;
    }

    public function getCredentials(dataSourceID:int):Credentials {
        return cache[String(dataSourceID)];
    }

    public function createCredentials():ArrayCollection {
        var creds:ArrayCollection = new ArrayCollection();
        for (var dataSourceID:String in cache) {
            if (cache[dataSourceID] != null) {
                var credentialFulfillment:CredentialFulfillment = new CredentialFulfillment();
                credentialFulfillment.dataSourceID = int(dataSourceID);
                credentialFulfillment.credentials = cache[dataSourceID];
                creds.addItem(credentialFulfillment);
            }
        }
        return creds;
    }

    public function obtainCredentials(displayObject:DisplayObject, credentials:ArrayCollection, successFunction:Function,
            ... callbackParams):void {
        var credentialRequirementState:CredentialRequirementState = new CredentialRequirementState(displayObject, credentials,
                successFunction, callbackParams);
        credentialRequirementState.act();
    }

    public function loadIfPossible(dataSourceID:int):void {
        var encryptedCredentials:Credentials = User.getCredentials(dataSourceID);
        if (encryptedCredentials != null) {
            CredentialsCache.getCache().addCredentials(dataSourceID, encryptedCredentials);
        }
    }
}
}