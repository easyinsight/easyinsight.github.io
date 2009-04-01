package com.easyinsight.analysis.charts.twoaxisbased.line {
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSLineChartDefinition")]
public class LineChartDefinition extends TwoAxisDefinition{
    public function LineChartDefinition() {
        super();
    }

    override public function get type():int {
        return AnalysisDefinition.LINE;
    }

    override public function getChartType():int {
        return ChartTypes.LINE_2D;
    }

    override public function getChartFamily():int {
        return ChartTypes.LINE_FAMILY;
    }
}
}