/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/26/13
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import flash.events.Event;

import mx.collections.ArrayCollection;

public class TagMetadataEvent extends Event {

    public static const TAG_CREATE:String = "tagCreate";
    public static const REFRESH_TAGS:String = "tagRefresh";

    public var tag:Tag;
    public var tags:ArrayCollection;

    public function TagMetadataEvent(type:String, tag:Tag, tags:ArrayCollection) {
        super(type);
        this.tag = tag;
        this.tags = tags;
    }

    override public function clone():Event {
        return new TagMetadataEvent(type, tag, tags);
    }
}
}
