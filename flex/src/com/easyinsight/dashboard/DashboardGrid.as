package com.easyinsight.dashboard {
import mx.collections.ArrayCollection;
import mx.core.UIComponent;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardGrid")]
public class DashboardGrid extends DashboardElement {

    public var rows:int;
    public var columns:int;

    public var gridItems:ArrayCollection;

    public function DashboardGrid() {
        super();
    }


    override public function createEditorComponent():UIComponent {
        var comp:DashboardGridEditorComponent = new DashboardGridEditorComponent();
        comp.dashboardGrid = this;
        return comp;
    }

    override public function createViewComponent():UIComponent {
        var comp:DashboardGridViewComponent = new DashboardGridViewComponent();
        comp.dashboardGrid = this;
        return comp;
    }
}
}