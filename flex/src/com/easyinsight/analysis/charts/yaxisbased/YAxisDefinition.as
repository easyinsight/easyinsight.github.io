package com.easyinsight.analysis.charts.yaxisbased {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.FillProvider;

import mx.collections.ArrayCollection;

public class YAxisDefinition extends ChartDefinition{

    public var measures:ArrayCollection;
    public var yaxis:AnalysisItem;

    public function YAxisDefinition() {
        super();
    }

    override public function populate(fields:ArrayCollection):void {
        this.measures = findItems(fields, AnalysisItemTypes.MEASURE);
        var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DIMENSION);
        if (dimensions.length > 0) {
            yaxis = dimensions.getItemAt(0) as AnalysisItem;
        }
    }

    override public function getFields():ArrayCollection {
        var fields:Array = [];
        fields.push(yaxis);
        for each (var measure:AnalysisMeasure in measures) {
            fields.push(measure);
        }
        return new ArrayCollection(fields);
    }
}
}