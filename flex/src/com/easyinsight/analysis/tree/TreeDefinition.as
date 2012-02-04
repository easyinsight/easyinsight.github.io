package com.easyinsight.analysis.tree {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;

import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.WSTreeDefinition")]
public class TreeDefinition extends AnalysisDefinition{

    public var hierarchy:AnalysisItem;
    public var items:ArrayCollection = new ArrayCollection();
    public var treeDefinitionID:int;
    public var textColor:uint = 0x000000;
    public var headerTextColor:uint = 0x000000;
    public var rowColor1:uint = 0xF7F7F7;
    public var rowColor2:uint = 0xFFFFFF;
    public var headerColor1:uint = 0xFFFFFF;
    public var headerColor2:uint = 0xEFEFEF;
    public var autoExpandAll:Boolean = false;

    public function TreeDefinition() {
        super();
    }

    override public function supportsEmbeddedFonts():Boolean {
        return true;
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.treeDefinitionID = TreeDefinition(savedDef).treeDefinitionID;
    }

    override public function get type():int {
        return AnalysisDefinition.TREE;
    }

    override public function getFields():ArrayCollection {
       var fields:ArrayCollection = new ArrayCollection([ hierarchy ]);
        for each (var item:AnalysisItem in items) {
            fields.addItem(item);
        }
        return fields;
    }

    override public function showDrilldown(analysisItem:AnalysisItem):Boolean {
        return false;
    }

    override public function populate(fields:ArrayCollection):void {
        var hierarchies:ArrayCollection = findItems(fields, AnalysisItemTypes.HIERARCHY);
        if (hierarchies.length > 0) {
            hierarchy = hierarchies.getItemAt(0) as AnalysisHierarchyItem;
        }
        for each (var item:AnalysisItem in fields) {
            if (!item.hasType(AnalysisItemTypes.HIERARCHY)) {
                items.addItem(item);
            }
        }
    }
}
}