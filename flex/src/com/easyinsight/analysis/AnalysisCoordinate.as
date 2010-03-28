package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisCoordinate")]
public class AnalysisCoordinate extends AnalysisDimension {
    
    public var precision:int;
    public var analysisZipCode:AnalysisZipCode;

    public function AnalysisCoordinate() {
        super();
    }
}
}
