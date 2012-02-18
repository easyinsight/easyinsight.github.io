/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/17/12
 * Time: 1:11 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import flash.events.Event;

public class DashboardStackEvent extends Event {

    public static const DELETE_PAGE:String = "deletePage";
    public static const CLICK:String = "pageClick";

    public function DashboardStackEvent(type:String) {
        super(type);
    }

    override public function clone():Event {
        return new DashboardStackEvent(type);
    }
}
}
