package com.easyinsight.solutions {


import flash.events.Event;

public class DataSourceSolutionEvent extends Event{

    public static const DELETE_DATA_SOURCE:String = "deleteDataSource";

    public var descriptor:DataSourceDescriptor;

    public function DataSourceSolutionEvent(type:String, descriptor:DataSourceDescriptor) {
        super(type, true);
        this.descriptor = descriptor;
    }

    override public function clone():Event {
        return new DataSourceSolutionEvent(type, descriptor);
    }
}
}