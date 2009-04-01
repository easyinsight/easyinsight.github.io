package com.easyinsight.analysis.charts.bubble {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.ChartDefinition;
import com.easyinsight.analysis.charts.ChartTypes;
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSBubbleChartDefinition")]
public class BubbleChartDefinition extends ChartDefinition{
    public var dimension:AnalysisItem;
    public var xaxisMeasure:AnalysisItem;
    public var yaxisMeasure:AnalysisItem;
    public var zaxisMeasure:AnalysisItem;

    public function BubbleChartDefinition() {
        super();
    }

    override public function populate(fields:ArrayCollection):void {
        var measures:ArrayCollection = findItems(fields, AnalysisItemTypes.MEASURE);
        if (measures.length > 0) {
            xaxisMeasure = measures.getItemAt(0) as AnalysisItem;
            if (measures.length > 1) {
                yaxisMeasure = measures.getItemAt(1) as AnalysisItem;
                if (measures.length > 2) {
                    zaxisMeasure = measures.getItemAt(2) as AnalysisItem;
                }
            }
        }
        var dimensions:ArrayCollection = findItems(fields, AnalysisItemTypes.DIMENSION);
        if (dimensions.length > 0) {
            dimension = dimensions.getItemAt(0) as AnalysisItem;
        }
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection([ dimension, xaxisMeasure, yaxisMeasure, zaxisMeasure]);
    }

    override public function get type():int {
        return AnalysisDefinition.BUBBLE;
    }

    override public function getChartType():int {
        return ChartTypes.BUBBLE;
    }

    override public function getChartFamily():int {
        return ChartTypes.BUBBLE_FAMILY;
    }
}
}