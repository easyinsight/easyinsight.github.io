package com.easyinsight.analysis.charts.twoaxisbased.line {
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.twoaxisbased.MultiMeasureDefinition;
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSMultiMeasureLineChartDefinition")]
public class MultiMeasureLineChartDefinition extends MultiMeasureDefinition {
    public function MultiMeasureLineChartDefinition() {
        super();
    }

    override public function get type():int {
        return AnalysisDefinition.MMLINE;
    }

    override public function getChartType():int {
        return ChartTypes.MM_LINE;
    }

    override public function getChartFamily():int {
        return ChartTypes.LINE_FAMILY;
    }
}
}