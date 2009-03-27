package com.easyinsight.analysis.charts.twoaxisbased.line {
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisDefinition;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSLineChartDefinition")]
public class LineChartDefinition extends TwoAxisDefinition{
    public function LineChartDefinition() {
        super();
    }

    override public function getLabel():String {
        return "2D Line";
    }

    override public function get controller():String {
        return "com.easyinsight.analysis.charts.twoaxisbased.line.LineChartController";
    }

    override public function getChartType():int {
        return ChartTypes.LINE_2D;
    }

    override public function getChartFamily():int {
        return ChartTypes.LINE_FAMILY;
    }
}
}