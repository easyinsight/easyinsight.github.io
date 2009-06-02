package com.easyinsight.analysis {
import com.easyinsight.HierarchyComboBox;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisHierarchyItem")]
public class AnalysisHierarchyItem extends AnalysisDimension {

    public var hierarchyLevels:ArrayCollection;
    public var hierarchyLevel:HierarchyLevel;
    public var analysisHierarchyItemID:int;

    public function AnalysisHierarchyItem() {
        super();
    }

    override public function getType():int {
        return super.getType() | AnalysisItemTypes.HIERARCHY;
    }
}
}