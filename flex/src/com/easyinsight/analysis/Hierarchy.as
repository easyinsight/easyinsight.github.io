package com.easyinsight.analysis {
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.Hierarchy")]
public class Hierarchy {
    public var hierarchyID:int;
    public var hierarchyLevels:ArrayCollection;
    public var name:String;
}
}