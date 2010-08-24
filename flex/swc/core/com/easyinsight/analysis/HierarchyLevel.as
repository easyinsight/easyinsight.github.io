package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.HierarchyLevel")]
public class HierarchyLevel {
    public var hierarchyLevelID:int;
    public var analysisItem:AnalysisItem;
    public var position:int;

    public function HierarchyLevel() {

    }

    public function get display():String {
        return analysisItem.display;
    }
}
}