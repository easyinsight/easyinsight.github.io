package com.easyinsight.analysis.treemap {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;

import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.WSTreeMapDefinition")]
public class TreeMapDefinition extends AnalysisDefinition{

    public function TreeMapDefinition() {
        super();
    }

    public var measure1:AnalysisItem;
    public var measure2:AnalysisItem;
    public var hierarchy:AnalysisItem;
    public var treeMapDefinitionID:int;

    public var highColor:uint = 0xAAAAFF;
    public var lowColor:uint = 0x333388;
    public var colorStrategy:String = "Linear";

    override public function get type():int {
        return AnalysisDefinition.TREEMAP;
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection([ measure1, measure2, hierarchy ]);
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.treeMapDefinitionID = TreeMapDefinition(savedDef).treeMapDefinitionID;
    }

    override public function populate(fields:ArrayCollection):void {
        var measures:ArrayCollection = findItems(fields, AnalysisItemTypes.MEASURE);
        if (measures.length > 0) {
            measure1 = measures.getItemAt(0) as AnalysisItem;
            if (measures.length > 1) {
                measure2 = measures.getItemAt(1) as AnalysisItem;
            }
        }
        var hierarchies:ArrayCollection = findItems(fields, AnalysisItemTypes.HIERARCHY);
        if (hierarchies.length > 0) {
            hierarchy = hierarchies.getItemAt(0) as AnalysisItem;
        }
    }

    override public function getValue(analysisItem:AnalysisItem, data:Object):Object {
        var hierarchyItem:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
        var index:int = hierarchyItem.hierarchyLevels.getItemIndex(hierarchyItem.hierarchyLevel) + 1;
        var hierarchyBase:AnalysisItem = hierarchyItem.hierarchyLevels.getItemAt(index).analysisItem;
        return data[hierarchyBase.qualifiedName()];
    }

    override public function getCoreAnalysisItem(analysisItem:AnalysisItem):AnalysisItem {
        var hierarchyItem:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
        var index:int = hierarchyItem.hierarchyLevels.getItemIndex(hierarchyItem.hierarchyLevel) + 1;
        return hierarchyItem.hierarchyLevels.getItemAt(index).analysisItem;
    }
}
}