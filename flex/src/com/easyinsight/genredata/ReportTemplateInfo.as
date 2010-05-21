package com.easyinsight.genredata {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.solutions.ReportTemplateInfo")]
public class ReportTemplateInfo {

    public var reportID:int;
    public var solutionID:int;
    public var dataSources:ArrayCollection;

    public function ReportTemplateInfo() {
    }
}
}