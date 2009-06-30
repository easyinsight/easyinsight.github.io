package com.easyinsight.goals {
import com.easyinsight.solutions.DataSourceDescriptor;

import flash.events.Event;
public class DataSourceChoiceEvent extends Event {
    public static const DATA_SOURCE_CHOICE:String = "dataSourceChoice";

    public var feedDefinition:DataSourceDescriptor;

    public function DataSourceChoiceEvent(feedDefinition:DataSourceDescriptor) {
        super(DATA_SOURCE_CHOICE);
        this.feedDefinition = feedDefinition;
    }


    override public function clone():Event {
        return new DataSourceChoiceEvent(feedDefinition);
    }
}
}