/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/25/12
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.framework {
import flash.events.EventDispatcher;

import mx.controls.Alert;

import mx.messaging.config.ServerConfig;
import mx.rpc.AsyncResponder;
import mx.rpc.AsyncToken;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;

import mx.rpc.remoting.RemoteObject;

public class LoginObject extends EventDispatcher {

    private var authService:RemoteObject;

    public function LoginObject() {
    }

    public function authenticate(tokenString:String, userIDString:String, authResult:UserServiceResponse):void {
        authService = new RemoteObject();
        authService.destination = "login";
        if (authService.channelSet == null) {
            authService.channelSet = ServerConfig.getChannelSet(authService.destination);
        }
        var token:AsyncToken = authService.channelSet.login(tokenString, userIDString);
        token.addResponder(new AsyncResponder(
                function (event:ResultEvent, token:Object = null):void {
                    switch (event.result) {
                        case "success":
                            dispatchEvent(new LoginEvent(LoginEvent.LOGIN, authResult));
                            break;
                        default:
                            trace(event.result);
                    }
                },
                function (event:FaultEvent, token:Object = null):void {
                    switch (event.fault.faultCode) {
                        case "Client.Authentication":
                        default:

                    }
                }
        ));
    }
}
}
