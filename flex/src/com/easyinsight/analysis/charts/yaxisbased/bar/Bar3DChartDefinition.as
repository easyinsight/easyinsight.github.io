package com.easyinsight.analysis.charts.yaxisbased.bar {
import com.easyinsight.analysis.charts.yaxisbased.YAxisDefinition;
[Bindable]
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WS3DBarChartDefinition")]
public class Bar3DChartDefinition extends YAxisDefinition{
    public function Bar3DChartDefinition() {
        super();
    }

    override public function getLabel():String {
        return "3D Bar";
    }

    override public function get controller():String {
        return "com.easyinsight.analysis.charts.yaxisbased.bar.Bar3DChartController";
    }
}
}