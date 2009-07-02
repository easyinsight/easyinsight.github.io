package com.easyinsight.solutions {
import com.easyinsight.listing.DataFeedDescriptor;

import flash.events.Event;

public class DataSourceSolutionEvent extends Event{

    public static const DELETE_DATA_SOURCE:String = "deleteDataSource";

    public var descriptor:DataFeedDescriptor;

    public function DataSourceSolutionEvent(type:String, descriptor:DataFeedDescriptor) {
        super(type, true);
        this.descriptor = descriptor;
    }

    override public function clone():Event {
        return new DataSourceSolutionEvent(type, descriptor);
    }
}
}