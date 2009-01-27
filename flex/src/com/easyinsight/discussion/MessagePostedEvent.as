package com.easyinsight.discussion {
import flash.events.Event;
public class MessagePostedEvent extends Event {
    public static const MESSAGE_POSTED:String = "messagePosted";

    public var message:String;

    public function MessagePostedEvent(message:String) {
        super(MESSAGE_POSTED);
        this.message = message;
    }


    override public function clone():Event {
        return new MessagePostedEvent(message);
    }
}
}