package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisStep")]
public class AnalysisStep extends AnalysisDateDimension{

    public var startDate:AnalysisDateDimension;
    public var endDate:AnalysisDateDimension;
    public var correlationDimension:AnalysisDimension;

    public function AnalysisStep() {
        super();
        dateLevel = AnalysisItemTypes.DAY_LEVEL;
    }

    override public function getType():int {
        return super.getType() | AnalysisItemTypes.STEP;
    }
}
}