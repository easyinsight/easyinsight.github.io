package com.easyinsight.framework {
import mx.collections.ArrayCollection;

public interface ICredentialsCache {
    function addCredentials(dataSourceID:int, credentials:Credentials):void;
    function getCredentials(dataSourceID:int):Credentials;
    function createCredentials():ArrayCollection;
}
}