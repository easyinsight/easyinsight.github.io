package com.easyinsight.solutions {
import flash.events.Event;

public class SolutionDetailEvent extends Event {

    public static const INSTALL:String = "install";
    public static const EXTERNAL_SITE:String = "externalSite";

    public var externalURL:String;

    public function SolutionDetailEvent(type:String, externalURL:String) {
        super(type);
        this.externalURL = externalURL;
    }

    override public function clone():Event {
        return new SolutionDetailEvent(type, externalURL);
    }
}
}