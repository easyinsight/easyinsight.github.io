package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.FunctionExplanation")]
public class FunctionExplanation {

    public var signature:String;
    public var description:String;
    public var parameters:ArrayCollection;

    public function FunctionExplanation() {
    }
}
}