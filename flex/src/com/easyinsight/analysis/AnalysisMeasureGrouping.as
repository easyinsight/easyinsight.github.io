package com.easyinsight.analysis {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisMeasureGrouping")]
public class AnalysisMeasureGrouping extends AnalysisDimension{
    
    public var measures:ArrayCollection;

    public function AnalysisMeasureGrouping() {
        super();
    }
}
}