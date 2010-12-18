package com.easyinsight.analysis.charts.yaxisbased {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.ComboBoxReportFormItem;
import com.easyinsight.analysis.FillProvider;

import mx.collections.ArrayCollection;

public class YAxisDefinition extends ChartDefinition{

    public var measure:AnalysisItem;
    public var yaxis:AnalysisItem;
    public var colorScheme:String = FillProvider.defaultFill;;

    public function YAxisDefinition() {
        super();
    }

    override public function populate(fields:ArrayCollection):void {
        var measures:ArrayCollection = findItems(fields, AnalysisItemTypes.MEASURE);
        if (measures.length > 0) {
            measure = measures.getItemAt(0) as AnalysisItem;
        }
        var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DIMENSION);
        if (dimensions.length > 0) {
            yaxis = dimensions.getItemAt(0) as AnalysisItem;
        }
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection([ yaxis, measure]);
    }
}
}