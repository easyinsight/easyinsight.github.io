/**
 * Created by jamesboe on 1/8/15.
 */
package com.easyinsight.schedule {
import flash.events.Event;

public class SequenceEvent extends Event {

    public static const ADD_TO_SEQUENCE:String = "addToSequence";

    public var activity:ScheduledActivity;

    public function SequenceEvent(eventType:String, activity:ScheduledActivity = null) {
        super(eventType);
        this.activity = activity;
    }

    override public function clone():Event {
        return new SequenceEvent(type, activity);
    }
}
}
