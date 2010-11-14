package com.easyinsight.framework {
import com.easyinsight.outboundnotifications.BroadcastInfo;
import com.easyinsight.outboundnotifications.BroadcastInfoEvent;
import com.easyinsight.outboundnotifications.RefreshEventInfo;

import com.easyinsight.scorecard.DataSourceAsyncEvent;
import com.easyinsight.scorecard.DataSourceMessageEvent;
import com.easyinsight.scorecard.ScorecardAsyncEvent;
import com.easyinsight.scorecard.ScorecardMessageEvent;

import flash.events.EventDispatcher;

import mx.messaging.Consumer;
import mx.messaging.events.MessageEvent;
import mx.messaging.events.MessageFaultEvent;

public class EIMessageListener extends EventDispatcher {

    private var consumer:Consumer;

    private static var _instance:EIMessageListener;

    public static function initialize():void {
        _instance = new EIMessageListener();
    }

    public static function instance():EIMessageListener {
        return _instance;
    }

    public function EIMessageListener() {
        consumer = new Consumer();
        consumer.destination = "generalNotifications";
        consumer.addEventListener(MessageEvent.MESSAGE, handleMessage);
        consumer.addEventListener(MessageFaultEvent.FAULT, handleFault);
        User.getEventNotifier().addEventListener(LoginEvent.LOGIN, onLogin);
        User.getEventNotifier().addEventListener(LoginEvent.LOGOUT, onLogout);
    }

    private function handleFault(event:mx.messaging.events.MessageFaultEvent):void {
        //Alert.show(event.message.faultString);
    }

    private function onLogin(event:LoginEvent):void {
        consumer.subscribe();
    }

    private function onLogout(event:LoginEvent):void {
        consumer.unsubscribe();
    }

    private function handleMessage(event:mx.messaging.events.MessageEvent):void {

        if(event.message.body is RefreshEventInfo) {
            var info:RefreshEventInfo = event.message.body as RefreshEventInfo;
            dispatchEvent(new AsyncInfoEvent(info));
        } else if(event.message.body is BroadcastInfo) {
            var broadcastInfo:BroadcastInfo = event.message.body as BroadcastInfo;
            dispatchEvent(new BroadcastInfoEvent(broadcastInfo));
        } else if (event.message.body is ScorecardAsyncEvent) {
            dispatchEvent(new ScorecardMessageEvent(ScorecardAsyncEvent(event.message.body)));
        } else if (event.message.body is DataSourceAsyncEvent) {
            dispatchEvent(new DataSourceMessageEvent(DataSourceAsyncEvent(event.message.body)));
        }
    }
}
}