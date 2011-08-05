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
    public var alertPoint1:Number = 0;
    public var alertPoint2:Number = 0;
    public var color1:uint = 0xFF0000;
    public var color2:uint = 0xFFFF00;
    public var color3:uint = 0x00BB11;

    public function GaugeDefinition() {
        super();
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.gaugeDefinitionID = GaugeDefinition(savedDef).gaugeDefinitionID;
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