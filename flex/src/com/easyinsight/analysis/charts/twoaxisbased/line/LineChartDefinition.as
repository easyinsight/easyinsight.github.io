package com.easyinsight.analysis.charts.twoaxisbased.line {
import com.easyinsight.analysis.NumericReportFormItem;
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSLineChartDefinition")]
public class LineChartDefinition extends TwoAxisDefinition{

    public var autoScale:Boolean = true;
    public var autoScaled:Boolean = true;
    public var xAxisMaximum:Date = null;

    public var strokeWeight:int = 2;



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

    override public function createFormItems():ArrayCollection {
        var items:ArrayCollection = super.createFormItems();
        items.addItem(new NumericReportFormItem("Stroke Weight", "strokeWeight", strokeWeight, this, 1, 10));
        return items;
    }
}
}