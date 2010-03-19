package com.easyinsight.scorecard {
import flash.events.Event;

public class DataSourceMessageEvent extends Event {

    public static const DATA_SOURCE_MESSAGE:String = "dataSourceMessage";

    public var dataSourceAsyncEvent:DataSourceAsyncEvent;

    public function DataSourceMessageEvent(dataSourceAsyncEvent:DataSourceAsyncEvent) {
        super(DATA_SOURCE_MESSAGE);
        this.dataSourceAsyncEvent = dataSourceAsyncEvent;
    }

    override public function clone():Event {
        return new DataSourceMessageEvent(dataSourceAsyncEvent);
    }
}
}