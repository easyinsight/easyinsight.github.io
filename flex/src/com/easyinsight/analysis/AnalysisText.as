package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisText")]
public class AnalysisText extends AnalysisDimension {

    public var html:Boolean;

    public function AnalysisText() {
        super();
    }

    override public function getType():int {
        return super.getType() | AnalysisItemTypes.TEXT;
    }
}
}