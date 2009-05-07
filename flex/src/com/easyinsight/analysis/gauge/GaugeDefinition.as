package com.easyinsight.analysis.gauge {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.WSGaugeDefinition")]
public class GaugeDefinition extends AnalysisDefinition {

    public static const CIRCULAR_GAUGE:int = 1;
    public static const HORIZONTAL_GAUGE:int = 2;

    public var measure:AnalysisItem;
    public var gaugeType:int = CIRCULAR_GAUGE;
    public var gaugeDefinitionID:int;
    public var maxValue:int;

    public function GaugeDefinition() {
        super();
    }

    override public function get type():int {
        return AnalysisDefinition.GAUGE;
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection([ measure ]);
    }

    override public function populate(fields:ArrayCollection):void {
        var measures:ArrayCollection = findItems(fields, AnalysisItemTypes.MEASURE);
        if (measures.length > 0) {
            measure = measures.getItemAt(0) as AnalysisItem;
        }
    }
}
}