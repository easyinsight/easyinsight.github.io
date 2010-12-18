package com.easyinsight.analysis.charts.xaxisbased.pie {
import com.easyinsight.analysis.ComboBoxReportFormItem;
import com.easyinsight.analysis.FillProvider;
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.xaxisbased.XAxisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSPieChartDefinition")]
public class PieChartDefinition extends XAxisDefinition{

    public var labelPosition:String = "outside";

    public function PieChartDefinition() {
        super();
    }

    override public function get type():int {
        return AnalysisDefinition.PIE;
    }

    override public function getChartType():int {
        return ChartTypes.PIE_2D;
    }

    override public function getChartFamily():int {
        return ChartTypes.PIE_FAMILY;
    }


    override public function createFormItems():ArrayCollection {
        var items:ArrayCollection = super.createFormItems();
        items.addItem(new ComboBoxReportFormItem("Color Scheme", "colorScheme", colorScheme,
                    this, [FillProvider.radialGradients, FillProvider.highContrast]));
        items.addItem(new ComboBoxReportFormItem("Label Position", "labelPosition", labelPosition,
                this, ["callout", "insideWithCallout", "inside", "outside", "none"]));
        return items;
    }
}
}