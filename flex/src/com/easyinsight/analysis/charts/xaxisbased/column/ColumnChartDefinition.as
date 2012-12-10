package com.easyinsight.analysis.charts.xaxisbased.column {
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.charts.ChartTypes;
import com.easyinsight.analysis.charts.xaxisbased.XAxisDefinition;
import com.easyinsight.analysis.AnalysisDefinition;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSColumnChartDefinition")]
public class ColumnChartDefinition extends XAxisDefinition{

    public var chartColor:uint;
    public var gradientColor:uint;
    public var useChartColor:Boolean;
    public var columnSort:String = ChartDefinition.SORT_UNSORTED;
    public var axisType:String = "Linear";
    public var labelPosition:String = "none";
    public var labelFontSize:int = 12;
    public var labelFontWeight:String = "none";
    public var labelInsideFontColor:int = 0x222222;
    public var labelOutsideFontColor:int = 0;
    public var useInsideLabelFontColor:Boolean = true;
    public var useOutsideLabelFontColor:Boolean = true;

    public function ColumnChartDefinition() {
        super();
    }

    override public function supportsEmbeddedFonts():Boolean {
        return true;
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
}
}