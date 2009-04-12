package com.easyinsight.feedassembly {
import flash.events.Event;
public class CompositeFeedSaveEvent extends Event {
    public static const COMPOSITE_FEED_SAVE:String = "compositeFeedSave";

    public var feedDefinition:CompositeFeedDefinition;

    public function CompositeFeedSaveEvent(feedDefinition:CompositeFeedDefinition) {
        super(COMPOSITE_FEED_SAVE);
        this.feedDefinition = feedDefinition;
    }

    override public function clone():Event {
        return new CompositeFeedSaveEvent(feedDefinition);
    }
}
}