package com.easyinsight.framework {
import com.easyinsight.notifications.BroadcastInfo;
import com.easyinsight.notifications.BroadcastInfoEvent;
import com.easyinsight.notifications.RefreshEventInfo;

import com.easyinsight.notifications.TodoEventInfo;

import flash.events.EventDispatcher;

import mx.controls.Alert;
import mx.messaging.Consumer;
import mx.messaging.events.MessageEvent;
import mx.messaging.events.MessageFaultEvent;

public class EIMessageListener extends EventDispatcher {

    private var consumer:Consumer;

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
        }
        else if(event.message.body is TodoEventInfo) {
            var todoInfo:TodoEventInfo = event.message.body as TodoEventInfo;
            dispatchEvent(new TodoInfoEvent(todoInfo));
        }
        else if(event.message.body is BroadcastInfo) {
            var broadcastInfo:BroadcastInfo = event.message.body as BroadcastInfo;
            dispatchEvent(new BroadcastInfoEvent(broadcastInfo));
        }
    }
}
}