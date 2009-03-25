package com.easyinsight.groups {
import flash.events.Event;
public class GroupRefreshEvent extends Event{

    public static const GROUP_REFRESH:String = "groupRefresh";

    public function GroupRefreshEvent() {
        super(GROUP_REFRESH);
    }


    override public function clone():Event {
        return new GroupRefreshEvent();
    }
}
}