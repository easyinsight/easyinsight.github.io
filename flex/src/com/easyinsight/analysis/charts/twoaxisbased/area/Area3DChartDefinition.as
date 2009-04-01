package com.easyinsight.analysis.charts.twoaxisbased.area {
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WS3DAreaChartDefinition")]
public class Area3DChartDefinition extends TwoAxisDefinition{
    public function Area3DChartDefinition() {
        super();
    }

    override public function get type():int {
        return AnalysisDefinition.AREA3D;
    }

    override public function getChartType():int {
        return ChartTypes.AREA_3D;
    }

    override public function getChartFamily():int {
        return ChartTypes.AREA_FAMILY;
    }
}
}