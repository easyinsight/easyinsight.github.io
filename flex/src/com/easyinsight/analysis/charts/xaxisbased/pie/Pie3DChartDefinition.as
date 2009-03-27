package com.easyinsight.analysis.charts.xaxisbased.pie {
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.xaxisbased.XAxisDefinition;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WS3DPieChartDefinition")]
public class Pie3DChartDefinition extends XAxisDefinition{
    public function Pie3DChartDefinition() {
        super();
    }

    override public function getLabel():String {
        return "3D Pie";
    }

    override public function get controller():String {
        return "com.easyinsight.analysis.charts.xaxisbased.pie.Pie3DChartController";
    }

    override public function getChartType():int {
        return ChartTypes.PIE_3D;
    }

    override public function getChartFamily():int {
        return ChartTypes.PIE_FAMILY;
    }
}
}