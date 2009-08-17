package com.easyinsight.dashboard {
import com.easyinsight.analysis.CredentialFulfillment;
import com.easyinsight.framework.CredentialRequirementState;
import com.easyinsight.framework.Credentials;
import com.easyinsight.framework.ICredentialsCache;

import flash.display.DisplayObject;
import flash.utils.ByteArray;

import flash.data.EncryptedLocalStore;

import mx.collections.ArrayCollection;

public class AirCredentialsCache implements ICredentialsCache {
                                                                 
    public function AirCredentialsCache() {
    }

    public function addCredentials(dataSourceID:int, credentials:Credentials):void {
        var credentialsArray:ByteArray = EncryptedLocalStore.getItem("credentials");
        var credObject:Object = new Object();
        if (credentialsArray != null) {
            var credString:String = credentialsArray.readUTFBytes(credentialsArray.length);
            var credArray:Array = credString.split(",");
            for each (var idString:String in credArray) {
                credObject[idString] = idString;
            }
        }
        var credentialsUserName:ByteArray = new ByteArray();
        credentialsUserName.writeUTFBytes(credentials.userName);
        EncryptedLocalStore.setItem(dataSourceID + "-" + "userName", credentialsUserName);
        var credentialsPassword:ByteArray = new ByteArray();
        credentialsPassword.writeUTFBytes(credentials.password);
        EncryptedLocalStore.setItem(dataSourceID + "-" + "password", credentialsPassword);
        credObject[String(dataSourceID)] = String(dataSourceID);
        var buf:String = "";
        for each (var compileString:String in credObject) {
            buf += compileString += ",";
        }
        buf = buf.substring(0, buf.length - 1);
        var bufArray:ByteArray = new ByteArray();
        bufArray.writeUTFBytes(buf);
        EncryptedLocalStore.setItem("credentials", bufArray);
    }

    public function getCredentials(dataSourceID:int):Credentials {
        var userNameBytes:ByteArray = EncryptedLocalStore.getItem(dataSourceID + "-userName");
        if (userNameBytes != null) {
            var userName:String = userNameBytes.readUTFBytes(userNameBytes.length);
            var passwordBytes:ByteArray = EncryptedLocalStore.getItem(dataSourceID + "-password");
            if (passwordBytes != null) {
                var password:String = passwordBytes.readUTFBytes(passwordBytes.length);
                var credentials:Credentials = new Credentials();
                credentials.userName = userName;
                credentials.password = password;
                return credentials;
            }
        }
        return null;
    }

    public function createCredentials():ArrayCollection {
        var credentialsCollection:ArrayCollection = new ArrayCollection();
        var credentialsArray:ByteArray = EncryptedLocalStore.getItem("credentials");
        var credObject:Object = new Object();
        if (credentialsArray != null) {
            var credString:String = credentialsArray.readUTFBytes(credentialsArray.length);
            var credArray:Array = credString.split(",");
            for each (var idString:String in credArray) {
                var credentials:Credentials = getCredentials(int(idString));
                if (credentials != null) {
                    var credentialsFulfillment:CredentialFulfillment = new CredentialFulfillment();
                    credentialsFulfillment.credentials = credentials;
                    credentialsFulfillment.dataSourceID = int(idString);
                    credentialsCollection.addItem(credentialsFulfillment);
                }
            }
        }
        return credentialsCollection;
    }

    public function obtainCredentials(displayObject:DisplayObject, credentials:ArrayCollection, successFunction:Function,
            ... callbackParams):void {
        var credentialRequirementState:CredentialRequirementState = new CredentialRequirementState(displayObject, credentials,
                successFunction, callbackParams);
        credentialRequirementState.act();
    }
}
}