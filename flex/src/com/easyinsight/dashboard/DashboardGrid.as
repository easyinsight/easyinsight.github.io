package com.easyinsight.dashboard {
import com.easyinsight.analysis.ColorReportFormItem;
import com.easyinsight.analysis.NumericReportFormItem;
import com.easyinsight.analysis.TextReportFormItem;

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

    // 713-595-1352
    public var showLabel:Boolean;
    public var gridItems:ArrayCollection;

    public function DashboardGrid() {
        super();
    }
}
}