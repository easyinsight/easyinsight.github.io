/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/25/13
 * Time: 8:40 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import flash.events.Event;

public class TagEvent extends Event {

    public static const TAG_SELECTED:String = "tagSelect";
    public static const TAG_UNSELECTED:String = "tagUnselect";

    public var tag:Tag;

    public function TagEvent(type:String, tag:Tag) {
        super(type, true);
        this.tag = tag;
    }

    override public function clone():Event {
        return new TagEvent(type, tag);
    }
}
}
