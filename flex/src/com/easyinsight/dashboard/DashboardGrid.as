package com.easyinsight.dashboard {
import com.easyinsight.analysis.ColorReportFormItem;

import com.easyinsight.analysis.NumericReportFormItem;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardGrid")]
public class DashboardGrid extends DashboardElement {

    public var rows:int;
    public var columns:int;

    public var width:int = 0;
    public var backgroundColor:uint = 0xFFFFFF;
    public var backgroundAlpha:Number = 0;

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

    override public function editableProperties():ArrayCollection {
        var properties:ArrayCollection = new ArrayCollection();
        properties.addItem(new NumericReportFormItem("Width", "width", width, this, 0, 2000));
        properties.addItem(new ColorReportFormItem("Background Color", "backgroundColor", backgroundColor, this));
        properties.addItem(new NumericReportFormItem("Background Alpha", "backgroundAlpha", backgroundAlpha, this, 0, 1));
        return properties;
    }
}
}