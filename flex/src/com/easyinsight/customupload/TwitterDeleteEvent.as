package com.easyinsight.customupload {
import flash.events.Event;

public class TwitterDeleteEvent extends Event{

    public static const TWITTER_TEXT:String = "twitterDelete";

    public function TwitterDeleteEvent() {
        super(TWITTER_TEXT, true);
    }
}
}