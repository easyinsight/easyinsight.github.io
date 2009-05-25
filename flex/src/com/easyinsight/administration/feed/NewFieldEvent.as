package com.easyinsight.administration.feed {
import flash.events.Event;

public class NewFieldEvent extends Event{

    public static const NEW_FIELD:String = "newField";

    public var name:String;

    public function NewFieldEvent(name:String) {
        super(NEW_FIELD);
        this.name = name;
    }

    override public function clone():Event {
        return new NewFieldEvent(name);
    }
}
}