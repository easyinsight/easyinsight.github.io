package com.easyinsight.collaboration {

import mx.containers.HBox;
import mx.controls.Image;
import mx.controls.Label;
import mx.messaging.Consumer;
import mx.messaging.events.MessageEvent;

public class MessageDisplay extends HBox{

    [Bindable]
    [Embed(source="../../../../assets/messagex16.png")]
    private var messageIcon:Class;

    private var messageImage:Image;
    private var messageLabel:Label;
    private var consumer:Consumer;

    public function MessageDisplay() {
        super();
        messageImage = new Image();
        messageImage.source = messageIcon;
        consumer = new Consumer();
        consumer.destination = "generalNotification";
        consumer.addEventListener(MessageEvent.MESSAGE, onMessage);
    }

    private function onMessage(event:MessageEvent):void {
        event.message.body;
    }
}
}