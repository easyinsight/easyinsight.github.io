package com.easyinsight.datasources {
import flash.events.Event;

public class HighriseTokenEvent extends Event {

    public static const ADD_TOKEN:String = "addToken";
    public static const DELETE_TOKEN:String = "deleteToken";

    public var token:HighriseAdditionalToken;

    public function HighriseTokenEvent(type:String, token:HighriseAdditionalToken) {
        super(type, true);
        this.token = token;
    }

    override public function clone():Event {
        return new HighriseTokenEvent(type, token);
    }
}
}