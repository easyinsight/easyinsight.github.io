package com.easyinsight.dashboard {
import flash.events.Event;

public class DashboardChangedEvent extends Event {

    public static const DASHBOARD_CHANGED:String = "dashboardChanged";

    public function DashboardChangedEvent() {
        super(DASHBOARD_CHANGED, true);
    }

    override public function clone():Event {
        return new DashboardChangedEvent();
    }
}
}