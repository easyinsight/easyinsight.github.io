package com.easyinsight.administration.feed {
import com.easyinsight.analysis.AnalysisDimension;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.VirtualDimension")]
public class VirtualDimension {

    public var virtualDimensionID:int;
    public var baseDimension:AnalysisDimension;
    public var virtualTransforms:ArrayCollection = new ArrayCollection();
    public var name:String;
    public var defaultTransform:VirtualTransform;

    public function VirtualDimension() {
    }
}
}