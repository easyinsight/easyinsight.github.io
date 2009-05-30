package com.easyinsight.framework {
import com.easyinsight.notifications.RefreshEventInfo;

import flash.events.Event;

public class AsyncInfoEvent extends Event{

    public static const ASYNC_INFO:String = "asyncInfo";

    public var info:RefreshEventInfo;

    public function AsyncInfoEvent(info:RefreshEventInfo) {
        super(ASYNC_INFO);
        this.info = info;
    }


    override public function clone():Event {
        return new AsyncInfoEvent(info);
    }
}
}