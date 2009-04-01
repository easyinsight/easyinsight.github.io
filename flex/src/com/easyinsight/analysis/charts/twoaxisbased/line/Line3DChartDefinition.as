package com.easyinsight.analysis.charts.twoaxisbased.line {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisDefinition;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WS3DLineChartDefinition")]
public class Line3DChartDefinition extends TwoAxisDefinition{
    public function Line3DChartDefinition() {
        super();
    }

    override public function get type():int {
        return AnalysisDefinition.LINE3D;
    }

    override public function getChartType():int {
        return ChartTypes.LINE_3D;
    }

    override public function getChartFamily():int {
        return ChartTypes.LINE_FAMILY;
    }
}
}