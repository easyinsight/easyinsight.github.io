package com.easyinsight.analysis.charts.xaxisbased.column {
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.xaxisbased.XAxisDefinition;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSColumnChartDefinition")]
public class ColumnChartDefinition extends XAxisDefinition{
    public function ColumnChartDefinition() {
        super();
    }

    override public function getLabel():String {
        return "2D Column";
    }

    override public function get controller():String {
        return "com.easyinsight.analysis.charts.xaxisbased.column.ColumnChartController";
    }

    override public function getChartType():int {
        return ChartTypes.COLUMN_2D;
    }

    override public function getChartFamily():int {
        return ChartTypes.COLUMN_FAMILY;
    }
}
}