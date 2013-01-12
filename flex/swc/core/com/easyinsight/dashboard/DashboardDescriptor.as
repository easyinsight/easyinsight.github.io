package com.easyinsight.dashboard {
import com.easyinsight.quicksearch.EIDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardDescriptor")]
public class DashboardDescriptor extends EIDescriptor {

    public function DashboardDescriptor() {
        super();
    }

    override public function getType():int {
        return EIDescriptor.DASHBOARD;
    }

    override public function get typeString():String {
        return "Dashboard";
    }

    public function get size():int {
        return 0;
    }
}
}
