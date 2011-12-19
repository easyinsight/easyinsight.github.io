/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/9/11
 * Time: 9:58 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import flash.events.Event;

public class LogoutEvent extends Event {

    public static const LOGOUT:String = "logout";

    public function LogoutEvent() {
        super(LOGOUT, true);
    }

    override public function clone():Event {
        return new LogoutEvent();
    }
}
}
