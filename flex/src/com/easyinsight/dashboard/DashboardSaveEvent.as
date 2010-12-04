package com.easyinsight.dashboard {
import flash.events.Event;

public class DashboardSaveEvent extends Event {

    public static const DASHBOARD_SAVE:String = "dashboardSave";

    public var dashboard:Dashboard;

    public function DashboardSaveEvent(dashboard:Dashboard) {
        super(DASHBOARD_SAVE);
        this.dashboard = dashboard;
    }

    override public function clone():Event {
        return new DashboardSaveEvent(dashboard);
    }
}
}