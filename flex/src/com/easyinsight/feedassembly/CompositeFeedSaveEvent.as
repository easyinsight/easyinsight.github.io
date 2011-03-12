package com.easyinsight.feedassembly {
import com.easyinsight.administration.feed.FeedDefinitionData;

import flash.events.Event;
public class CompositeFeedSaveEvent extends Event {
    public static const COMPOSITE_FEED_SAVE:String = "compositeFeedSave";

    public var feedDefinition:FeedDefinitionData;

    public function CompositeFeedSaveEvent(feedDefinition:FeedDefinitionData) {
        super(COMPOSITE_FEED_SAVE);
        this.feedDefinition = feedDefinition;
    }

    override public function clone():Event {
        return new CompositeFeedSaveEvent(feedDefinition);
    }
}
}