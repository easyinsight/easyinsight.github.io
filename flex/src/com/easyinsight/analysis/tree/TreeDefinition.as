package com.easyinsight.analysis.tree {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.WSTreeDefinition")]
public class TreeDefinition extends AnalysisDefinition{

    public var hierarchy:AnalysisItem;
    public var items:ArrayCollection = new ArrayCollection();
    public var treeDefinitionID:int;

    public function TreeDefinition() {
        super();
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

    override public function populate(fields:ArrayCollection):void {
        
    }
}
}