package com.easyinsight {
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.HierarchyLevels")]
public class HierarchyLevels {
    public var hierarchyLevels:ArrayCollection;
    public var hierachyLevelsID:int; 

    public function HierarchyLevels() {
    }
}
}