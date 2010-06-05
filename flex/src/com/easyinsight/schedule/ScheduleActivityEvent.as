package com.easyinsight.schedule {
import flash.events.Event;

public class ScheduleActivityEvent extends Event {

    public static const NEW_ACTIVITY:String = "newActivity";
    public static const EDIT_ACTIVITY:String = "editActivity";
    public static const DELETE_ACTIVITY:String = "deleteActivity";

    public var activity:ScheduledActivity;

    public function ScheduleActivityEvent(type:String, activity:ScheduledActivity) {
        super(type, true);
        this.activity = activity;
    }

    override public function clone():Event {
        return new ScheduleActivityEvent(type, activity);
    }
}
}