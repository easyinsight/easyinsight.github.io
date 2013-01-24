package com.easyinsight.analysis.charts.twoaxisbased.area {
import com.easyinsight.analysis.ComboBoxReportFormItem;
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.twoaxisbased.TwoAxisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSAreaChartDefinition")]
public class AreaChartDefinition extends TwoAxisDefinition{

    public var stackingType:String = "stacked";

    public var legendMaxWidth:int = 200;

    public var multiColors:ArrayCollection = new ArrayCollection();

    public function AreaChartDefinition() {
        super();
    }

    override public function supportsEmbeddedFonts():Boolean {
        return true;
    }

    override public function get type():int {
        return AnalysisDefinition.AREA;
    }

    override public function getChartType():int {
        return ChartTypes.AREA_2D;
    }

    override public function getChartFamily():int {
        return ChartTypes.AREA_FAMILY;
    }
}
}