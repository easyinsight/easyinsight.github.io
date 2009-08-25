package com.easyinsight.framework {
import mx.controls.Alert;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class TokenRedirector {

    private var tokenService:RemoteObject;

    private var _type:int;

    public function TokenRedirector() {
        tokenService = new RemoteObject();
        tokenService.destination = "tokenService";
        tokenService.setToken.addEventListener(ResultEvent.RESULT, onTokenResult);
        tokenService.setToken.addEventListener(FaultEvent.FAULT, onFault);
    }

    public function set type(value:int):void {
        _type = value;
    }

    private function onFault(event:FaultEvent):void {
        Alert.show(event.fault.faultString);
    }

    private function onTokenResult(event:ResultEvent):void {
        var result:String = tokenService.setToken.lastResult as String;
        if (result == null) {
            Alert.show("redirect at this point");
        } else {
            Alert.show(result);
        }
    }

    public function onURL(url:String):void {
        Alert.show("url = " + url);
        tokenService.setToken.send(_type, url);
    }
}
}