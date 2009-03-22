package com.easyinsight.analysis {
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.WSColumnChartDefinition")]
public class ColumnChartDefinition extends ChartDefinition{

    public var measure:AnalysisItem;
    public var xAxis:AnalysisItem;
    public var yAxis:AnalysisItem;

    public function ColumnChartDefinition() {
        super();
    }


    override public function populate(fields:ArrayCollection):void {
        var measures:ArrayCollection = findItems(fields, AnalysisItemTypes.MEASURE);
        if (measures.length > 0) {
            measure = measures.getItemAt(0) as AnalysisItem;
        }
        var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DIMENSION);
        if (dimensions.length > 0) {
            xAxis = dimensions.getItemAt(0) as AnalysisItem;
            if (dimensions.length > 1) {
                yAxis = dimensions.getItemAt(1) as AnalysisItem;
            }
        }
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection([ xAxis, yAxis, measure]);
    }
}
}