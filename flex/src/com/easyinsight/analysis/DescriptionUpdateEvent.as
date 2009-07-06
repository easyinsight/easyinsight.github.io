package com.easyinsight.analysis {
import flash.events.Event;
import flash.events.Event;

public class DescriptionUpdateEvent extends Event{

    public static const DESCRIPTION_UPDATE:String = "descriptionUpdate";

    public var text:String;

    public function DescriptionUpdateEvent(text:String) {
        super(DESCRIPTION_UPDATE);
        this.text = text;
    }

    override public function clone():Event {
        return new DescriptionUpdateEvent(text);
    }
}
}