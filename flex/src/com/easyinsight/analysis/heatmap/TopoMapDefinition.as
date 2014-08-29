package com.easyinsight.analysis.heatmap {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSMap")]
public class TopoMapDefinition extends AnalysisDefinition {


    public var region:AnalysisItem;
    public var measure:AnalysisItem;
    public var longitude:AnalysisItem;
    public var latitude:AnalysisItem;
    public var pointMeasure:AnalysisItem;
    public var pointGrouping:AnalysisItem;
    public var precision:int;
    public var regionFillStart:int;
    public var regionFillEnd:int;
    public var noDataFill:int;
    public var pointColors:ArrayCollection = new ArrayCollection();
    public var map:String = "US States";
    public var mapID:int;

    public function TopoMapDefinition() {
        super();
    }

    override public function useHTMLInFlash():Boolean {
        return true;
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.mapID = TopoMapDefinition(savedDef).mapID;
    }

    override public function get type():int {
        return AnalysisDefinition.TOPO;
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection([ measure, region ]);
    }

    override public function populate(fields:ArrayCollection):void {
        var measures:ArrayCollection = findItems(fields, AnalysisItemTypes.MEASURE);
        if (measures.length > 0) {
            measure = measures.getItemAt(0) as AnalysisItem;
        }
    }
}
}