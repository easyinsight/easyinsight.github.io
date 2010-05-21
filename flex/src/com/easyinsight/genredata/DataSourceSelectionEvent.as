package com.easyinsight.genredata {
import com.easyinsight.solutions.DataSourceDescriptor;

import flash.events.Event;

public class DataSourceSelectionEvent extends Event {

    public static const DATA_SOURCE_SELECTION:String = "dataSourceSelection";

    public var dataSource:DataSourceDescriptor;

    public function DataSourceSelectionEvent(dataSource:DataSourceDescriptor) {
        super(DATA_SOURCE_SELECTION);
        this.dataSource = dataSource;
    }

    override public function clone():Event {
        return new DataSourceSelectionEvent(dataSource);
    }
}
}