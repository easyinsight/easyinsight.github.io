package com.easyinsight.analysis.heatmap {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSHeatMap")]
public class HeatMapDefinition extends AnalysisDefinition {

    public var longitudeItem:AnalysisItem;
    public var latitudeItem:AnalysisItem;
    public var measure:AnalysisItem;
    public var latitude:Number;
    public var longitude:Number;
    public var maxLong:Number;
    public var minLong:Number;
    public var maxLat:Number;
    public var minLat:Number;
    public var zoomLevel:int;
    public var mapType:int;
    public var heatMapID:int;

    public function HeatMapDefinition() {
        super();
    }

    override public function get type():int {
        return AnalysisDefinition.HEATMAP;
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