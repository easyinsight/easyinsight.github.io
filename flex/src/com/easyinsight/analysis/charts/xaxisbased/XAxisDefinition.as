package com.easyinsight.analysis.charts.xaxisbased {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.FillProvider;

import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSXAxisChartDefinition")]
public class XAxisDefinition extends ChartDefinition{

    public var measures:ArrayCollection;
    public var xaxis:AnalysisItem;

    public function XAxisDefinition() {
        super();
    }

    override public function populate(fields:ArrayCollection):void {
        this.measures = findItems(fields, AnalysisItemTypes.MEASURE);
        var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DIMENSION);
        if (dimensions.length > 0) {
            xaxis = dimensions.getItemAt(0) as AnalysisItem;
        }
    }

    override public function getFields():ArrayCollection {
        var fields:Array = [];
        fields.push(xaxis);
        for each (var measure:AnalysisMeasure in measures) {
            fields.push(measure);
        }
        return new ArrayCollection(fields);
    }
}
}