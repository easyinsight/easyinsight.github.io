package com.easyinsight.dashboard {
import com.easyinsight.solutions.DataSourceDescriptor;

import flash.events.Event;

public class DashboardSetupEvent extends Event {

    public static const DASHBOARD_SETUP:String = "dashboardSetup";

    public var dataSource:DataSourceDescriptor;
    public var rows:int;
    public var columns:int;

    public function DashboardSetupEvent(dataSource:DataSourceDescriptor, rows:int, columns:int) {
        super(DASHBOARD_SETUP);
        this.dataSource = dataSource;
        this.rows = rows;
        this.columns = columns;
    }

    override public function clone():Event {
        return new DashboardSetupEvent(dataSource, rows, columns);
    }
}
}