package com.easyinsight.goals {
import com.easyinsight.administration.feed.FeedDefinitionData;
import flash.events.Event;
public class DataSourceChoiceEvent extends Event {
    public static const DATA_SOURCE_CHOICE:String = "dataSourceChoice";

    public var feedDefinition:FeedDefinitionData;

    public function DataSourceChoiceEvent(feedDefinition:FeedDefinitionData) {
        super(DATA_SOURCE_CHOICE);
        this.feedDefinition = feedDefinition;
    }


    override public function clone():Event {
        return new DataSourceChoiceEvent(feedDefinition);
    }
}
}