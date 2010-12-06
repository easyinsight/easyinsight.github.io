package com.easyinsight.analysis.charts.xaxisbased.column {
import com.easyinsight.analysis.CheckBoxReportFormItem;
import com.easyinsight.analysis.ColorReportFormItem;
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.xaxisbased.XAxisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;

import mx.collections.ArrayCollection;


[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSColumnChartDefinition")]
public class ColumnChartDefinition extends XAxisDefinition{

    public var chartColor:uint;
    public var useChartColor:Boolean;

    public function ColumnChartDefinition() {
        super();
    }

    override public function get type():int {
        return AnalysisDefinition.COLUMN;
    }

    override public function getChartType():int {
        return ChartTypes.COLUMN_2D;
    }

    override public function getChartFamily():int {
        return ChartTypes.COLUMN_FAMILY;
    }

    override public function createFormItems():ArrayCollection {
        var items:ArrayCollection = super.createFormItems();
        items.addItem(new CheckBoxReportFormItem("Use Custom Chart Color", "useChartColor", useChartColor, this));
        items.addItem(new ColorReportFormItem("Custom Chart Color", "chartColor", chartColor, this));
        return items;
    }
}
}