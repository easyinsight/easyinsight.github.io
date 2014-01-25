/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/18/13
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.events.Event;

import mx.collections.ArrayCollection;

public class TagSelectionEvent extends Event {

    public static const TAG_SELECT:String = "onTagSelect";
    public var tags:ArrayCollection;

    public function TagSelectionEvent(type:String, tags:ArrayCollection = null) {
        super(type);
        this.tags = tags;
    }

    override public function clone():Event {
        return new TagSelectionEvent(type, tags);
    }
}
}
