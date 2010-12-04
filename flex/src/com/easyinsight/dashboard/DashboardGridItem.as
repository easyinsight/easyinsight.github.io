package com.easyinsight.dashboard {
[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardGridItem")]
public class DashboardGridItem extends DashboardElement {

    public var dashboardElement:DashboardElement;
    public var rowIndex:int;
    public var columnIndex:int;

    public function DashboardGridItem() {
    }
}
}