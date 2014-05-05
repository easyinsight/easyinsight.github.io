/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 4/28/14
 * Time: 11:20 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.events.Event;

public class JoinChangeEvent extends Event {

    public static const JOIN_CHANGE:String = "joinChange";

    public function JoinChangeEvent() {
        super(JOIN_CHANGE, true);
    }

    override public function clone():Event {
        return new JoinChangeEvent();
    }
}
}
