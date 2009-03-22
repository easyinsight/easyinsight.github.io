package com.easyinsight.analysis.charts.xaxisbased.pie {
import com.easyinsight.analysis.charts.xaxisbased.XAxisDefinition;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSPieChartDefinition")]
public class PieChartDefinition extends XAxisDefinition{
    public function PieChartDefinition() {
        super();
    }

    override public function getLabel():String {
        return "2D Pie";
    }

    override public function get controller():String {
        return "com.easyinsight.analysis.charts.xaxisbased.pie.PieChartController";
    }
}
}