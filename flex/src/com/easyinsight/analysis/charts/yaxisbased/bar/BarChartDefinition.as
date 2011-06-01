package com.easyinsight.analysis.charts.yaxisbased.bar {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.yaxisbased.YAxisDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSBarChartDefinition")]
public class BarChartDefinition extends YAxisDefinition{

    public var chartColor:uint;
    public var useChartColor:Boolean;
    public var columnSort:String = ChartDefinition.SORT_UNSORTED;

    public function BarChartDefinition() {
        super();
    }


    override public function get type():int {
        return AnalysisDefinition.BAR;
    }

    override public function getChartType():int {
        return ChartTypes.BAR_2D;
    }

    override public function getChartFamily():int {
        return ChartTypes.BAR_FAMILY;
    }
}
}