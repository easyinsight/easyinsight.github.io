package com.easyinsight.framework {
import com.easyinsight.analysis.CredentialFulfillment;

import mx.collections.ArrayCollection;

public class CredentialsCache {

    private var cache:Object = new Object();

    private static var credentialsCache:ICredentialsCache;

    public function CredentialsCache() {
    }

    public static function initialize(cache:ICredentialsCache):void {
        credentialsCache = cache;
    }

    public static function getCache():ICredentialsCache {
        return credentialsCache;
    }
}
}