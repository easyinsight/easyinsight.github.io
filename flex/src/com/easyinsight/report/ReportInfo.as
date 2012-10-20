package com.easyinsight.report {
import com.easyinsight.analysis.AnalysisDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.ReportInfo")]
public class ReportInfo {

    public var admin:Boolean;
    public var report:AnalysisDefinition;
    public var accessDenied:Boolean;

    public function ReportInfo() {
    }
}
}