package com.easyinsight.analysis.charts.yaxisbased.bar {
import com.easyinsight.analysis.charts.yaxisbased.YAxisDefinition;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSBarChartDefinition")]
public class BarChartDefinition extends YAxisDefinition{
    public function BarChartDefinition() {
        super();
    }

    override public function getLabel():String {
        return "2D Bar";
    }

    override public function get controller():String {
        return "com.easyinsight.analysis.charts.yaxisbased.bar.BarChartController";
    }
}
}