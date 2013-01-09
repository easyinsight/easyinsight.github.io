package com.easyinsight.dashboard {
[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardStackItem")]
public class DashboardStackItem extends DashboardElement {

    public var dashboardElement:DashboardElement;
    public var position:int;

    public var stackLabel:String;

    public function DashboardStackItem() {
    }
}
}