package com.easyinsight.framework {
import com.easyinsight.analysis.AnalysisItemOverride;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.HierarchyOverride")]
public class HierarchyOverride extends AnalysisItemOverride {

    public var position:int;

    public function HierarchyOverride() {
    }
}
}