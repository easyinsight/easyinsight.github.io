package com.easyinsight.datasources {

import flash.events.Event;

public class DataSourceDisplayEvent extends Event{

    public static const CHILD_DATA:String = "childData";
    public static const PARENT_DATA:String = "parentData";

    public var dataSource:DataSourceDisplay;

    public function DataSourceDisplayEvent(type:String, dataSource:DataSourceDisplay) {
        super(type);
        this.dataSource = dataSource;
    }

    override public function clone():Event {
        return new DataSourceDisplayEvent(type, dataSource);
    }
}
}