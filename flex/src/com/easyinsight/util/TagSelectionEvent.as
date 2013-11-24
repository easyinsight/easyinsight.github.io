/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/18/13
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.events.Event;

public class TagSelectionEvent extends Event {

    public static const TAG_SELECT:String = "onTagSelect";

    public function TagSelectionEvent(type:String) {
        super(type);
    }

    override public function clone():Event {
        return new TagSelectionEvent(type);
    }
}
}
