package com.easyinsight.analysis.charts.xaxisbased.column {
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.xaxisbased.XAxisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WS3DColumnChartDefinition")]
public class Column3DChartDefinition extends XAxisDefinition{
    public function Column3DChartDefinition() {
        super();
    }

    override public function get type():int {
        return AnalysisDefinition.COLUMN3D;
    }

    override public function getChartType():int {
        return ChartTypes.COLUMN_3D;
    }

    override public function getChartFamily():int {
        return ChartTypes.COLUMN_FAMILY;
    }
}
}