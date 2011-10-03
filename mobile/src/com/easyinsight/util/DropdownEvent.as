/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/1/11
 * Time: 10:01 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.events.Event;

public class DropdownEvent extends Event {

    public static const DROPDOWN:String = "dropdown";

    public function DropdownEvent() {
        super(DROPDOWN);
    }

    override public function clone():Event {
        return new DropdownEvent();
    }
}
}
