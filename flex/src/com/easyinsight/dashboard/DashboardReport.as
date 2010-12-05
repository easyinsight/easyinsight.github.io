package com.easyinsight.dashboard {
import com.easyinsight.analysis.CheckBoxReportFormItem;
import com.easyinsight.solutions.InsightDescriptor;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardReport")]
public class DashboardReport extends DashboardElement {

    public var report:InsightDescriptor;
    public var labelPlacement:int;
    public var showLabel:Boolean;

    public function DashboardReport() {
        super();
    }

    override public function createEditorComponent():UIComponent {
        var comp:DashboardReportEditorComponent = new DashboardReportEditorComponent();
        comp.report = this;
        return comp;
    }

    override public function createViewComponent():UIComponent {
        var comp:DashboardReportViewComponent = new DashboardReportViewComponent();
        comp.dashboardReport = this;
        return comp;
    }

    override public function editableProperties():ArrayCollection {
        var properties:ArrayCollection = new ArrayCollection();
        properties.addItem(new CheckBoxReportFormItem("Show Label", "showLabel", showLabel, this));
        return properties;
    }
}
}