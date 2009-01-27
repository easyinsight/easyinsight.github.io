package com.easyinsight.feedassembly {
import flash.events.Event;
public class CompositeFeedSaveEvent extends Event {
    public static const COMPOSITE_FEED_SAVE:String = "compositeFeedSave";

    public var feedID:int;

    public function CompositeFeedSaveEvent(feedID:int) {
        super(COMPOSITE_FEED_SAVE);
        this.feedID = feedID;
    }

    override public function clone():Event {
        return new CompositeFeedSaveEvent(feedID);
    }
}
}