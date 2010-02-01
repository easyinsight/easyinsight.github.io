package com.easyinsight.tour {
import flash.events.Event;

public class ActionTutorialElement extends TutorialElement {

    private var event:Event;

    public function ActionTutorialElement(event:Event) {
        super();
        this.event = event;
    }

    override public function forwardExecute():void {
        dispatchEvent(new NoteEvent(NoteEvent.EVENT_NOTE, null, event));
    }
}
}