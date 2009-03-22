package com.easyinsight.analysis.charts.twoaxisbased.area {
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisDefinition;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WS3DAreaChartDefinition")]
public class Area3DChartDefinition extends TwoAxisDefinition{
    public function Area3DChartDefinition() {
        super();
    }

    override public function getLabel():String {
        return "3D Area";
    }

    override public function get controller():String {
        return "com.easyinsight.analysis.charts.twoaxisbased.area.Area3DChartController";
    }
}
}