package com.easyinsight.analysis.charts.xaxisbased.pie {
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.xaxisbased.XAxisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSPieChartDefinition")]
public class
PieChartDefinition extends XAxisDefinition{

    public var labelPosition:String = "outside";

    public function PieChartDefinition() {
        super();
    }

    override public function supportsEmbeddedFonts():Boolean {
        return true;
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
}
}