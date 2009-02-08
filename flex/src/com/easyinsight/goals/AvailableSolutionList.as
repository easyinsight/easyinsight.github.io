package com.easyinsight.goals {
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.goals.AvailableSolutionList")]
public class AvailableSolutionList {
    public var tagMatchedSolutions:ArrayCollection;
    public var allSolutions:ArrayCollection;

    public function AvailableSolutionList() {
        
    }
}
}