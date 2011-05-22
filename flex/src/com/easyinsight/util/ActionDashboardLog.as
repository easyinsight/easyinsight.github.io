/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/16/11
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import com.easyinsight.dashboard.DashboardDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.audit.ActionDashboardLog")]
public class ActionDashboardLog extends ActionLog {

    public static const EDIT:int = 1;
    public static const VIEW:int = 2;

    public var dashboardID:int;

    public var dashboardDescriptor:DashboardDescriptor;

    public function ActionDashboardLog(actionType:int = 0, dashboardID:int = 0) {
        this.actionType = actionType;
        this.dashboardID = dashboardID;
    }

    override public function get display():String {
        if (actionType == EDIT) {
            return "Edit " + dashboardDescriptor.name;
        } else if (actionType == VIEW) {
            return "View " + dashboardDescriptor.name;
        }
        return null;
    }
}
}
