package com.easyinsight.customupload {
import flash.events.Event;

public class TwitterTextEvent extends Event{

    public static const TWITTER_TEXT:String = "twitterText";

    public function TwitterTextEvent() {
        super(TWITTER_TEXT, true);
    }
}
}