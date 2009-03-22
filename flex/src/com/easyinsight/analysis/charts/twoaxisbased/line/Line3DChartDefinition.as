package com.easyinsight.analysis.charts.twoaxisbased.line {
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisDefinition;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WS3DLineChartDefinition")]
public class Line3DChartDefinition extends TwoAxisDefinition{
    public function Line3DChartDefinition() {
        super();
    }

    override public function getLabel():String {
        return "3D Line";
    }

    override public function get controller():String {
        return "com.easyinsight.analysis.charts.twoaxisbased.line.Line3DChartController";
    }
}
}