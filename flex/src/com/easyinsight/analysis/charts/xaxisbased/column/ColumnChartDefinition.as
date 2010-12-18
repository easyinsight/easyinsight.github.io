package com.easyinsight.analysis.charts.xaxisbased.column {
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.CheckBoxReportFormItem;
import com.easyinsight.analysis.ColorReportFormItem;
import com.easyinsight.analysis.ComboBoxReportFormItem;
import com.easyinsight.analysis.FillProvider;
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.xaxisbased.XAxisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;

import mx.collections.ArrayCollection;


[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSColumnChartDefinition")]
public class ColumnChartDefinition extends XAxisDefinition{

    public var chartColor:uint;
    public var useChartColor:Boolean;
    public var columnSort:String = ChartDefinition.SORT_UNSORTED;

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
        items.addItem(new ComboBoxReportFormItem("Color Scheme", "colorScheme", colorScheme,
                    this, [FillProvider.ocean, FillProvider.linearGradients, FillProvider.highContrast]));
        items.addItem(new CheckBoxReportFormItem("Use Custom Chart Color", "useChartColor", useChartColor, this));
        items.addItem(new ColorReportFormItem("Custom Chart Color", "chartColor", chartColor, this));
        items.addItem(new ComboBoxReportFormItem("Chart Sort", "columnSort", columnSort, this,
                [ChartDefinition.SORT_UNSORTED, ChartDefinition.SORT_X_ASCENDING, ChartDefinition.SORT_X_DESCENDING,
                ChartDefinition.SORT_Y_ASCENDING, ChartDefinition.SORT_Y_DESCENDING]));
        return items;
    }
}
}