package com.easyinsight.solutions {
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.solutions.SolutionContents")]
public class SolutionContents {
    public var feedDescriptors:ArrayCollection;
    public var goalTreeDescriptors:ArrayCollection;
    public var insightDescriptors:ArrayCollection;

    public function SolutionContents() {
        
    }
}
}