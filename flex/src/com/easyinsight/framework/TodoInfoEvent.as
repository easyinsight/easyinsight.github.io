package com.easyinsight.framework {
import com.easyinsight.notifications.RefreshEventInfo;

import com.easyinsight.notifications.TodoEventInfo;

import flash.events.Event;

public class TodoInfoEvent extends Event{

    public static const TODO_INFO:String = "todoInfo";

    public var info:TodoEventInfo;

    public function TodoInfoEvent(info:TodoEventInfo) {
        super(TODO_INFO);
        this.info = info;
    }


    override public function clone():Event {
        return new TodoInfoEvent(info);
    }
}
}