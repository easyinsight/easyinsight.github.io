package com.easyinsight.analysis.heatmap {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSHeatMap")]
public class HeatMapDefinition extends AnalysisDefinition {

    public static const HEAT_MAP:int = 1;
    public static const POINT_MAP:int = 2;

    public var longitudeItem:AnalysisItem;
    public var latitudeItem:AnalysisItem;
    public var zipCode:AnalysisItem;
    public var measure:AnalysisItem;
    public var latitude:Number = 0;
    public var longitude:Number = 0;
    public var maxLong:Number = 0;
    public var minLong:Number = 0;
    public var maxLat:Number = 0;
    public var minLat:Number = 0;
    public var zoomLevel:int;
    public var precision:int;
    public var mapType:int;
    public var displayType:int = HEAT_MAP;
    public var heatMapID:int;

    public function HeatMapDefinition() {
        super();
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.heatMapID = HeatMapDefinition(savedDef).heatMapID;
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