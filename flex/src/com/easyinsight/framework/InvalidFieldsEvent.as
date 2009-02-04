package com.easyinsight.framework {
import com.easyinsight.analysis.FeedMetadata;
import flash.events.Event;
import flash.events.Event;
import mx.collections.ArrayCollection;
public class InvalidFieldsEvent extends Event{
    public static const INVALID_FIELDS:String = "invalidFields";

    public var invalidAnalysisItemIDs:ArrayCollection;
    public var feedMetadata:FeedMetadata;

    public function InvalidFieldsEvent(invalidAnalysisItemIDs:ArrayCollection, feedMetadata:FeedMetadata) {
        super(INVALID_FIELDS);
        this.invalidAnalysisItemIDs = invalidAnalysisItemIDs;
        this.feedMetadata = feedMetadata;
    }


    override public function clone():Event {
        return new InvalidFieldsEvent(invalidAnalysisItemIDs, feedMetadata);
    }
}
}