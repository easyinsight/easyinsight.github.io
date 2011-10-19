/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/11
 * Time: 3:52 PM
 * To change this template use File | Settings | File Templates.
 */
package com.anotherflexdev.diagrammer {
import flash.events.Event;

public class LinkEvent extends Event {

    public static const LINK_ADDED:String = "linkAdded";
    public static const LINK_REMOVED:String = "linkRemoved";

    public var link:Link;

    public function LinkEvent(type:String, link:Link) {
        super(type);
        this.link = link;
    }

    override public function clone():Event {
        return new LinkEvent(type, link);
    }
}
}
