package com.easyinsight.customupload {
import com.easyinsight.listing.DataFeedDescriptor;

import flash.events.Event;

public class DataSourceConfiguredEvent extends Event{

    public static const DATA_SOURCE_CONFIGURED:String = "dataSourceConfigured";

    public var descriptor:DataFeedDescriptor;

    public function DataSourceConfiguredEvent(descriptor:DataFeedDescriptor) {
        super(DATA_SOURCE_CONFIGURED);
        this.descriptor = descriptor;
    }

    override public function clone():Event {
        return new DataSourceConfiguredEvent(descriptor);
    }
}
}