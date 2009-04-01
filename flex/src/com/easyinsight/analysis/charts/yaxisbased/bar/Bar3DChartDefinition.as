package com.easyinsight.analysis.charts.yaxisbased.bar {
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.yaxisbased.YAxisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;
[Bindable]
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WS3DBarChartDefinition")]
public class Bar3DChartDefinition extends YAxisDefinition{
    public function Bar3DChartDefinition() {
        super();
    }

    override public function get type():int {
        return AnalysisDefinition.BAR3D;
    }

    override public function getChartType():int {
        return ChartTypes.BAR_2D;
    }

    override public function getChartFamily():int {
        return ChartTypes.BAR_FAMILY;
    }
}
}