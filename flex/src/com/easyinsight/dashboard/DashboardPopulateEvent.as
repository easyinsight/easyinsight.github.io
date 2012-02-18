/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/17/12
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import flash.events.Event;

public class DashboardPopulateEvent extends Event {

    public static const DASHBOARD_POPULATE:String = "dashboardPopulate";

    public var dashboardBox:DashboardBox;

    public function DashboardPopulateEvent(type:String, dashboardBox:DashboardBox = null) {
        super(type);
        this.dashboardBox = dashboardBox;
    }

    override public function clone():Event {
        return new DashboardPopulateEvent(type, dashboardBox);
    }
}
}
