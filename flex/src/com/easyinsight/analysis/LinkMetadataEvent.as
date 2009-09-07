package com.easyinsight.analysis {
import flash.events.Event;

public class LinkMetadataEvent extends Event{

    public static const LINK_DEFINED:String = "linkDefined";
    public static const LINK_EDITED:String = "linkEdited";
    public static const LINK_DELETED:String = "linkDeleted";

    public var link:Link;
    public var previousLink:Link;

    public function LinkMetadataEvent(type:String, link:Link, previousLink:Link = null) {
        super(type, true);
        this.link = link;
        this.previousLink = previousLink;
    }

    override public function clone():Event {
        return new LinkMetadataEvent(type, link, previousLink);
    }
}
}