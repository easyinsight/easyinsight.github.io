package com.easyinsight.framework {
import com.easyinsight.analysis.CredentialFulfillment;

import mx.collections.ArrayCollection;

public class CredentialsCache {

    private var cache:Object = new Object();

    private static var credentialsCache:CredentialsCache;

    public function CredentialsCache() {
    }

    public function addCredentials(dataSourceID:int, credentials:Credentials):void {
        cache[String(dataSourceID)] = credentials;
    }

    public function getCredentials(dataSourceID:int):Credentials {
        return cache[String(dataSourceID)];
    }

    public function createCredentials():ArrayCollection {
        var creds:ArrayCollection = new ArrayCollection();
        for (var dataSourceID:String in cache) {
            var credentialFulfillment:CredentialFulfillment = new CredentialFulfillment();
            credentialFulfillment.dataSourceID = int(dataSourceID);
            credentialFulfillment.credentials = cache[dataSourceID];
            creds.addItem(credentialFulfillment);
        }
        return creds;
    }

    public static function getCache():CredentialsCache {
        if (credentialsCache == null) {
            credentialsCache = new CredentialsCache();
        }
        return credentialsCache;
    }
}
}