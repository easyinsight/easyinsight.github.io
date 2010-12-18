package com.easyinsight.analysis.charts.yaxisbased.bar {
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.CheckBoxReportFormItem;
import com.easyinsight.analysis.ColorReportFormItem;
import com.easyinsight.analysis.ComboBoxReportFormItem;
import com.easyinsight.analysis.FillProvider;
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.yaxisbased.YAxisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;

import mx.collections.ArrayCollection;

[Bindable]
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WS3DBarChartDefinition")]
public class Bar3DChartDefinition extends YAxisDefinition{

    public var chartColor:uint;
    public var useChartColor:Boolean;
    public var columnSort:String = ChartDefinition.SORT_UNSORTED;

    public function Bar3DChartDefinition() {
        super();
    }

    override public function get type():int {
        return AnalysisDefinition.BAR3D;
    }

    override public function getChartType():int {
        return ChartTypes.BAR_2D;
    }

    override public function getChartFamily():int {
        return ChartTypes.BAR_FAMILY;
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