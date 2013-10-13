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
    public var showLabel:Boolean = true;
    public var autoCalculateHeight:Boolean;
    public var spaceSides:Boolean = true;
    public var overridenFilters:Object;

    public function DashboardReport() {
        super();
    }
}
}