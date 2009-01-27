package com.easyinsight.analysis {
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisHierarchyItem")]
public class AnalysisHierarchyItem extends AnalysisDimension {

    public var hierarchyLevels:ArrayCollection;
    public var hierarchyLevel:HierarchyLevel;

    override public function getType():int {
        return super.getType() | AnalysisItemTypes.HIERARCHY;
    }
}
}