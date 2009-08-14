package com.easyinsight.framework {
import flash.display.DisplayObject;

import mx.collections.ArrayCollection;
import mx.rpc.remoting.Operation;

public interface ICredentialsCache {
    function addCredentials(dataSourceID:int, credentials:Credentials):void;
    function getCredentials(dataSourceID:int):Credentials;
    function createCredentials():ArrayCollection;
    function obtainCredentials(displayObject:DisplayObject, credentials:ArrayCollection, successFunction:Function, ... callbackParams):void;
}
}