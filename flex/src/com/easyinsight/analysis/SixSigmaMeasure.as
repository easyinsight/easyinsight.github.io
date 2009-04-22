package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.SixSigmaMeasure")]
public class SixSigmaMeasure extends AnalysisMeasure{

    public var sigmaType:int;

    public var totalDefectsMeasure:AnalysisMeasure;
    public var totalOpportunitiesMeasure:AnalysisMeasure;

    public function SixSigmaMeasure() {
        super();
    }

    override public function getType():int {
        return super.getType() | AnalysisItemTypes.SIX_SIGMA_MEASURE;
    }
}
}