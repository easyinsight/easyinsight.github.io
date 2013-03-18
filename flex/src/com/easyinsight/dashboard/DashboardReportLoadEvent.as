/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/13/13
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.dashboard {
import flash.events.Event;

public class DashboardReportLoadEvent extends Event {

    public static const DASHBOARD_REPORT_LOAD:String = "dashboardReportLoad";
    public static const DASHBOARD_LOAD:String = "dashboardLoad";

    public function DashboardReportLoadEvent(type:String) {
        super(type, true);
    }
}
}
