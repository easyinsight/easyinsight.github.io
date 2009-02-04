package com.easyinsight.dbservice {
import flash.events.Event;
import flash.events.Event;
public class QueryPublishedEvent extends Event{

    public static const QUERY_PUBLISHED:String = "queryPublished";



    public function QueryPublishedEvent() {
        super(QUERY_PUBLISHED);
    }


    override public function clone():Event {
        return new QueryPublishedEvent();
    }
}
}