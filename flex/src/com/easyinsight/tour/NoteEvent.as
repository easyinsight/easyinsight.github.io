package com.easyinsight.tour {
import flash.events.Event;

public class NoteEvent extends Event {

    public static const NEXT_NOTE:String = "nextNote";
    public static const PREVIOUS_NOTE:String = "previousNote";
    public static const CLOSE_NOTE:String = "closeNote";
    public static const EVENT_NOTE:String = "eventNote";

    public var note:Note;
    public var event:Event;

    public function NoteEvent(type:String, note:Note, event:Event = null) {
        super(type);
        this.note = note;
        this.event = event;
    }

    override public function clone():Event {
        return new NoteEvent(type, note, event);
    }
}
}