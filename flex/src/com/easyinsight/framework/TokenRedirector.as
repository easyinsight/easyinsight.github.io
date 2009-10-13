package com.easyinsight.framework {
import com.easyinsight.listing.ListingChangeEvent;
import com.easyinsight.solutions.DelayedSolutionLink;
import com.easyinsight.solutions.RevisedSolutionDetail;

import flash.events.EventDispatcher;

import mx.controls.Alert;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class TokenRedirector extends EventDispatcher {

    private var tokenService:RemoteObject;

    private var _type:int;

    private var _solutionID:int;

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


    public function set solutionID(value:int):void {
        _solutionID = value;
    }

    private function onTokenResult(event:ResultEvent):void {
        var result:String = tokenService.setToken.lastResult as String;
        if (result == null) {
            Alert.show("Successfully authorized Easy Insight! You can now proceed with your previous action.");
            if (_solutionID == 0) {
                Alert.show("Successfully authorized Easy Insight! You can now proceed with your previous action.");
                //User.getEventNotifier().dispatchEvent(new NavigationEvent("My Data", null, { toGDocs: true }));
            } else {
                var delayedSolutionLink:DelayedSolutionLink = new DelayedSolutionLink(_solutionID);
                delayedSolutionLink.addEventListener(ListingChangeEvent.LISTING_CHANGE, onEvent);
                delayedSolutionLink.execute();
            }
        } else {
            Alert.show(result);
        }
    }

    private function onEvent(event:ListingChangeEvent):void {
        dispatchEvent(event);
    }

    public function onURL(url:String):void {
        tokenService.setToken.send(_type, url);
    }
}
}