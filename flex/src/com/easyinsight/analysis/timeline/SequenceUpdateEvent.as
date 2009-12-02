package com.easyinsight.analysis.timeline {
import flash.events.Event;

public class SequenceUpdateEvent extends Event {

    public static const SEQUENCE_UPDATE:String = "sequenceUpdate";

    public function SequenceUpdateEvent() {
        super(SEQUENCE_UPDATE);
    }

    override public function clone():Event {
        return new SequenceUpdateEvent();
    }
}
}