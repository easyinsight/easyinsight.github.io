package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisZipCode")]
public class AnalysisZipCode extends AnalysisDimension {
    public function AnalysisZipCode() {
        super();
    }

    override public function getType():int {
        return super.getType() | AnalysisItemTypes.ZIP_CODE;
    }
}
}
